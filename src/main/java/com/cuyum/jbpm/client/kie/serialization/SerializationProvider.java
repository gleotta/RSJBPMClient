package com.cuyum.jbpm.client.kie.serialization;

public interface SerializationProvider {

    Object serialize(Object objectInput);

    Object deserialize(Object serializedInput);
    
}
