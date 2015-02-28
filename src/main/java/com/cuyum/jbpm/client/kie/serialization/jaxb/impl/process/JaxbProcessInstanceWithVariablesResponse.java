package org.kie.services.client.serialization.jaxb.impl.process;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.kie.api.runtime.process.ProcessInstance;
import org.kie.services.client.serialization.jaxb.rest.AbstractJaxbResponse;

@XmlRootElement(name="process-instance-with-vars")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value={JaxbProcessInstanceResponse.class})
public class JaxbProcessInstanceWithVariablesResponse extends AbstractJaxbResponse {

    @XmlElement
    private Map<String, String> variables;
    
    @XmlElement
    private JaxbProcessInstanceResponse processInstance;
    
    public JaxbProcessInstanceWithVariablesResponse() { 
        // Default Constructor
    }

    public JaxbProcessInstanceWithVariablesResponse(ProcessInstance processInstance, Map<String, String> vars) { 
        initialize(processInstance, vars);
    }

    public JaxbProcessInstanceWithVariablesResponse(ProcessInstance processInstance, Map<String, String> vars, String requestUrl) { 
        super(requestUrl);
        initialize(processInstance, vars);
    }
    
    protected void initialize(ProcessInstance processInstance, Map<String, String> vars) { 
        this.setProcessInstance(processInstance);
        this.variables = vars;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        JaxbProcessInstanceResponse xmlProcessInstance;
        if( ! (processInstance instanceof JaxbProcessInstanceResponse) ) { 
            xmlProcessInstance = new JaxbProcessInstanceResponse(processInstance);
        }  else { 
            xmlProcessInstance = (JaxbProcessInstanceResponse) processInstance;
        }
        
        this.processInstance = xmlProcessInstance;
    }

}
