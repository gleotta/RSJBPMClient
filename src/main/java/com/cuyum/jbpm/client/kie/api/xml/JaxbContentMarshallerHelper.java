/*
 * Copyright 2012 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.services.task.impl.model.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.drools.core.marshalling.impl.ClassObjectMarshallingStrategyAcceptor;
import org.drools.core.marshalling.impl.MarshallerReaderContext;
import org.drools.core.marshalling.impl.MarshallerWriteContext;
import org.drools.core.marshalling.impl.MarshallingConfigurationImpl;
import org.drools.core.marshalling.impl.PersisterHelper;
import org.drools.core.marshalling.impl.ProtobufMessages.Header;
import org.drools.core.marshalling.impl.SerializablePlaceholderResolverStrategy;
import org.jbpm.marshalling.impl.JBPMMessages;
import org.jbpm.marshalling.impl.JBPMMessages.Variable;
import org.jbpm.marshalling.impl.ProtobufProcessMarshaller;
import org.kie.api.marshalling.ObjectMarshallingStrategy;
import org.kie.api.marshalling.ObjectMarshallingStrategyStore;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.EnvironmentName;
import org.kie.internal.task.api.TaskModelProvider;
import org.kie.internal.task.api.model.AccessType;
import org.kie.internal.task.api.model.ContentData;
import org.kie.internal.task.api.model.FaultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ExtensionRegistry;

public class JaxbContentMarshallerHelper {

    private static final Logger logger = LoggerFactory.getLogger(JaxbContentMarshallerHelper.class);

    public static ContentData marshal(Object o, Environment env) {
        if (o == null) {
            return null;
        }
        
        ContentData content = null;
        byte[] toByteArray = marshallContent(env, o);
        content = TaskModelProvider.getFactory().newContentData();
        content.setContent(toByteArray);
        content.setType(o.getClass().getCanonicalName());
        content.setAccessType(AccessType.Inline); 

        return content;
    }
    
     public static FaultData marshalFault(Map<String, Object> fault, Environment env) {
        
        FaultData content = null;
        byte[] toByteArray = marshallContent(env, fault);
        content = TaskModelProvider.getFactory().newFaultData();
        content.setContent(toByteArray);
        content.setType(fault.getClass().getCanonicalName());
        content.setAccessType(AccessType.Inline);
        content.setFaultName((String)fault.get("faultName"));
        content.setType((String)fault.get("faultType"));

        return content;
    }
    

    public static Object unmarshall(byte[] content, Environment env) {
        return unmarshall(content, env, null);
    }

    public static Object unmarshall(byte[] content, Environment env, ClassLoader classloader) {
        MarshallerReaderContext context = null;
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(content);
            MarshallingConfigurationImpl marshallingConfigurationImpl = null;
            if (env != null) {
                marshallingConfigurationImpl = new MarshallingConfigurationImpl((ObjectMarshallingStrategy[]) env.get(EnvironmentName.OBJECT_MARSHALLING_STRATEGIES), false, false);
            } else {
                marshallingConfigurationImpl = new MarshallingConfigurationImpl(new ObjectMarshallingStrategy[]{new SerializablePlaceholderResolverStrategy(ClassObjectMarshallingStrategyAcceptor.DEFAULT)}, false, false);
            }
            ObjectMarshallingStrategyStore objectMarshallingStrategyStore = marshallingConfigurationImpl.getObjectMarshallingStrategyStore();
            context = new MarshallerReaderContext(stream, null, null, objectMarshallingStrategyStore, null, env);
            if (classloader != null) {
                context.classLoader = classloader;
            } else {
                context.classLoader = JaxbContentMarshallerHelper.class.getClassLoader();
            }
            ExtensionRegistry registry = PersisterHelper.buildRegistry(context, null);
            Header _header = PersisterHelper.readFromStreamWithHeaderPreloaded(context, registry);
            Variable parseFrom = JBPMMessages.Variable.parseFrom(_header.getPayload(), registry);
            Object value = ProtobufProcessMarshaller.unmarshallVariableValue(context, parseFrom);

            if (value instanceof Map) {
                Map result = new HashMap();
                Map<String, Variable> variablesMap = (Map<String, Variable>) value;
                for (String key : variablesMap.keySet()) {
                    result.put(key, ProtobufProcessMarshaller.unmarshallVariableValue(context, variablesMap.get(key)));
                }
                return result;
            }
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static byte[] marshallContent(Environment env, Object o) {
        MarshallerWriteContext context;
        try {
            MarshallingConfigurationImpl marshallingConfigurationImpl = null;
            if (env != null) {
                marshallingConfigurationImpl = new MarshallingConfigurationImpl((ObjectMarshallingStrategy[]) env.get(EnvironmentName.OBJECT_MARSHALLING_STRATEGIES), false, false);
            } else {
                marshallingConfigurationImpl = new MarshallingConfigurationImpl(new ObjectMarshallingStrategy[]{new SerializablePlaceholderResolverStrategy(ClassObjectMarshallingStrategyAcceptor.DEFAULT)}, false, false);
            }
            ObjectMarshallingStrategyStore objectMarshallingStrategyStore = marshallingConfigurationImpl.getObjectMarshallingStrategyStore();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            context = new MarshallerWriteContext(stream, null, null, null, objectMarshallingStrategyStore, env);
            Variable marshallVariable = null;
            if (o instanceof Map) {
                marshallVariable = ProtobufProcessMarshaller.marshallVariablesMap(
                        context,
                        (Map<String, Object>) o);
            } else {
                marshallVariable = ProtobufProcessMarshaller.marshallVariable(
                        context,
                        "results",
                        o);
            }
            PersisterHelper.writeToStreamWithHeader(
                    context,
                    marshallVariable);

            context.close();



            return stream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
