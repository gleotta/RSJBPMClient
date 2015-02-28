package com.cuyum.jbpm.client.kie.serialization.rest;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractJaxbResponse {

    @XmlElement
    protected JaxbRequestStatus status;
    
    @XmlElement
    @XmlSchemaType(name="anyURI")
    protected String url;
    
    public AbstractJaxbResponse() { 
       // Default constructor 
    }
    
    public AbstractJaxbResponse(String requestUrl ) { 
        this.url = requestUrl;
        this.status = JaxbRequestStatus.SUCCESS;
    }
    
    public String prettyPrint() throws JAXBException {
        StringWriter writer = new StringWriter();

        JAXBContext jc = JAXBContext.newInstance(this.getClass());
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this, writer);
        return writer.toString();
    }
    
    public JaxbRequestStatus getStatus() {
        return status;
    }

    public void setStatus(JaxbRequestStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
