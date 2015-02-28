package com.cuyum.jbpm.client.kie.serialization.jaxb.impl.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.cuyum.jbpm.client.kie.api.Command;
import com.cuyum.jbpm.client.kie.api.ProcessInstance;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.AbstractJaxbCommandResponse;

@XmlRootElement(name="process-instance-list")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value={JaxbProcessInstanceResponse.class})
public class JaxbProcessInstanceListResponse extends AbstractJaxbCommandResponse<List<ProcessInstance>> {

    @XmlElements({
        @XmlElement(name="process-instance",type=JaxbProcessInstanceResponse.class)
    })
    private List<ProcessInstance> processInstanceList;

    public JaxbProcessInstanceListResponse() { 
        this.processInstanceList = new ArrayList<ProcessInstance>();
    }
    
    public JaxbProcessInstanceListResponse(Collection<JaxbProcessInstanceResponse> taskSummaryCollection) { 
       this.processInstanceList = new ArrayList<ProcessInstance>(taskSummaryCollection);
    }
    
    public JaxbProcessInstanceListResponse(List<JaxbProcessInstanceResponse> taskSummaryCollection, int i, Command<?> cmd ) { 
        super(i, cmd);
        this.processInstanceList = new ArrayList<ProcessInstance>(taskSummaryCollection);
    }
    
    public void setResult(List<ProcessInstance> result) {
        this.processInstanceList = result;
    }
    
    public List<ProcessInstance> getResult() {
        return processInstanceList;
    }
}
