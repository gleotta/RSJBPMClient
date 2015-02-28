package com.cuyum.jbpm.client.kie.serialization;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.JaxbCommandsRequest;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.JaxbCommandsResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.JaxbLongListResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.JaxbOtherResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.JaxbPrimitiveResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.JaxbVariablesResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbHistoryLogList;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbNodeInstanceLog;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbProcessInstanceLog;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbVariableInstanceLog;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.deploy.JaxbDeploymentJobResult;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.deploy.JaxbDeploymentUnit;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.deploy.JaxbDeploymentUnitList;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.process.JaxbProcessInstanceListResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.process.JaxbProcessInstanceResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.process.JaxbProcessInstanceWithVariablesResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.process.JaxbWorkItem;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.task.JaxbContentResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.task.JaxbTaskResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.task.JaxbTaskSummaryListResponse;
import com.cuyum.jbpm.client.kie.serialization.rest.JaxbExceptionResponse;
import com.cuyum.jbpm.client.kie.serialization.rest.JaxbGenericResponse;
import com.cuyum.jbpm.client.kie.serialization.rest.JaxbRequestStatus;

public class JaxbSerializationProvider implements SerializationProvider {

    public final static int JMS_SERIALIZATION_TYPE = 0;

    public static Set<Class<?>> KIE_JAXB_CLASS_SET;
    static { 
        Class<?> [] kieJaxbClasses = { 
                // Command Request/Response
                JaxbCommandsRequest.class, 
                JaxbCommandsResponse.class,

                // command response
                JaxbContentResponse.class,
                JaxbTaskResponse.class,
                JaxbTaskSummaryListResponse.class,
                JaxbProcessInstanceListResponse.class,
                JaxbProcessInstanceResponse.class,
                JaxbProcessInstanceWithVariablesResponse.class,

                // REST other
                JaxbGenericResponse.class,
                JaxbLongListResponse.class,
                JaxbOtherResponse.class,
                JaxbPrimitiveResponse.class,
                JaxbVariablesResponse.class,
                JaxbExceptionResponse.class,
                JaxbGenericResponse.class,
                JaxbRequestStatus.class,

                // deployment
                JaxbDeploymentJobResult.class,
                JaxbDeploymentUnit.class,
                JaxbDeploymentUnitList.class,

                // workitem
                JaxbWorkItem.class,

                // history
                JaxbHistoryLogList.class,
                JaxbNodeInstanceLog.class,
                JaxbProcessInstanceLog.class,
                JaxbVariableInstanceLog.class
        };
        KIE_JAXB_CLASS_SET = new CopyOnWriteArraySet<Class<?>>(Arrays.asList(kieJaxbClasses));
    };

    public static Set<Class<?>> PRIMITIVE_ARRAY_CLASS_SET;
    static { 
        Class<?> [] primitiveClasses = { 
                new Boolean[]{}.getClass(),
                new Byte[]{}.getClass(),
                new Character[]{}.getClass(),
                new Double[]{}.getClass(),
                new Float[]{}.getClass(),
                new Integer[]{}.getClass(),
                new Long[]{}.getClass(),
                new Math[]{}.getClass(),
                new Number[]{}.getClass(),
                new Short[]{}.getClass(),
                new String[]{}.getClass()
        };
        PRIMITIVE_ARRAY_CLASS_SET = new CopyOnWriteArraySet<Class<?>>(Arrays.asList(primitiveClasses));
    };
    
    private Set<Class<?>> jaxbClasses;
    {
        jaxbClasses = new HashSet<Class<?>>(KIE_JAXB_CLASS_SET);
        jaxbClasses.addAll(PRIMITIVE_ARRAY_CLASS_SET);
    }
    
    private Set<Class<?>> extraJaxbClasses = new HashSet<Class<?>>();
    
    private JAXBContext jaxbContext;
    private boolean prettyPrint = false;

    public JaxbSerializationProvider() {
        initializeJaxbContext();
    }

    public JaxbSerializationProvider(Collection<Class<?>> extraJaxbClassList) {
        extraJaxbClassList.addAll(extraJaxbClassList);
        jaxbClasses.addAll(extraJaxbClassList);
        initializeJaxbContext();
    }

    private void initializeJaxbContext() {
        try {
            jaxbContext = JAXBContext.newInstance(jaxbClasses.toArray(new Class[jaxbClasses.size()]));
        } catch (JAXBException jaxbe) {
            throw new UnsupportedOperationException("Unsupported JAXB Class during initialization: " + jaxbe.getMessage(), jaxbe);
        }
    }
    
    public JAXBContext getJaxbContext() { 
        return this.jaxbContext;
    }

    public String serialize(Object object) {
        Marshaller marshaller = null;
        try { 
            marshaller = jaxbContext.createMarshaller();
            if( prettyPrint ) { 
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            }
        } catch( JAXBException jaxbe ) { 
            throw new SerializationException("Unable to create JAXB marshaller.", jaxbe);
        }
        StringWriter stringWriter = new StringWriter();

        try {
            marshaller.marshal(object, stringWriter);
        } catch( JAXBException jaxbe ) { 
            throw new SerializationException("Unable to marshall " + object.getClass().getSimpleName() + " instance.", jaxbe);
        }
        String output = stringWriter.toString();

        return output;
    }

    public Object deserialize(Object xmlStrObject) {
        if( ! (xmlStrObject instanceof String) ) { 
            throw new UnsupportedOperationException(JaxbSerializationProvider.class.getSimpleName() + " can only deserialize Strings");
        }
        String xmlStr = (String) xmlStrObject;
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch( JAXBException jaxbe ) { 
            throw new SerializationException("Unable to create unmarshaller.", jaxbe);
        }
        ByteArrayInputStream xmlStrInputStream = new ByteArrayInputStream(xmlStr.getBytes(Charset.forName("UTF-8")));

        Object jaxbObj = null;
        try { 
            jaxbObj = unmarshaller.unmarshal(xmlStrInputStream);
        } catch( JAXBException jaxbe ) { 
           throw new SerializationException("Unable to unmarshal string.", jaxbe);
        }

        return jaxbObj;
    }

    public void addJaxbClasses(Class<?>... jaxbClass) {
        for (int i = 0; i < jaxbClass.length; ++i) {
            jaxbClasses.add(jaxbClass[i]);
            extraJaxbClasses.add(jaxbClass[i]);
        }
        initializeJaxbContext();
    }

    public void addJaxbClasses(Collection<Class<?>> jaxbClassList) {
        for (Class<?> jaxbClass : jaxbClassList) {
            jaxbClasses.add(jaxbClass);
            extraJaxbClasses.add(jaxbClass);
        }
        initializeJaxbContext();
    }

    public Collection<Class<?>> getExtraJaxbClasses() { 
        return new HashSet<Class<?>>(extraJaxbClasses);
    }
    
    public static Set<Class<?>> commaSeperatedStringToClassSet(String extraClassNames) throws SerializationException { 
        return commaSeperatedStringToClassSet(JaxbSerializationProvider.class.getClassLoader(), extraClassNames);
    }
    
    public static Set<Class<?>> commaSeperatedStringToClassSet(ClassLoader classloader, String extraClassNames) throws SerializationException { 
        Set<Class<?>> classList = new HashSet<Class<?>>();
        
        extraClassNames = extraClassNames.trim();
        if( extraClassNames.isEmpty() ) { 
            return classList;
        }
        String [] extraClassNameList = split(extraClassNames);
        if( extraClassNameList.length == 0 ) { 
            return classList;
        }

        // non-empty string/list
        for( String extraClassName : extraClassNameList ) { 
            if( extraClassName.endsWith("[]") ) {
                continue;
            }
            try { 
                classList.add(classloader.loadClass(extraClassName));
            } catch( ClassNotFoundException cnfe ) { 
                throw new SerializationException("Unable to load JAXB class '" + extraClassName, cnfe);
            }
        }
        return classList;
    } 

    public static String classSetToCommaSeperatedString(Collection<Class<?>> extraClassList) throws SerializationException { 
        StringBuilder out = new StringBuilder("");
        Set<Class<?>> extraClassSet = new HashSet<Class<?>>();
        extraClassSet.addAll(extraClassList);
        for( Class<?> extraClass : extraClassSet ) { 
            if (out.length() > 0) {
                out.append(",");
            }
            String extraClassName = extraClass.getCanonicalName();
            if( extraClassName == null ) { 
                throw new SerializationException("Only classes with canonical names can be used for serialization");
            }
            out.append(extraClassName);
        }
        return out.toString();
    }
    
    public static String[] split(String in) {
        String[] splitIn = in.split(",");
        List<String> outList = new ArrayList<String>();
        for (int i = 0; i < splitIn.length; ++i) {
            splitIn[i] = splitIn[i].trim();
            if (!splitIn[i].isEmpty()) {
                outList.add(splitIn[i]);
            }
        }
        return outList.toArray(new String[outList.size()]);
    }

    public void setPrettyPrint(boolean prettyPrint) { 
        this.prettyPrint = prettyPrint;
    }
    
    public boolean getPrettyPrint() { 
        return this.prettyPrint;
    }
}