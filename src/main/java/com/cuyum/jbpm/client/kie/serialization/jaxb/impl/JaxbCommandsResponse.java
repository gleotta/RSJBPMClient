package com.cuyum.jbpm.client.kie.serialization.jaxb.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;

import com.cuyum.jbpm.client.kie.api.Command;
import com.cuyum.jbpm.client.kie.api.ProcessInstance;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbHistoryLogList;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbNodeInstanceLog;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbProcessInstanceLog;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.audit.JaxbVariableInstanceLog;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.process.JaxbProcessInstanceListResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.process.JaxbProcessInstanceResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.task.JaxbContentResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.task.JaxbTaskContentResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.task.JaxbTaskResponse;
import com.cuyum.jbpm.client.kie.serialization.jaxb.impl.task.JaxbTaskSummaryListResponse;
import com.cuyum.jbpm.client.kie.serialization.rest.JaxbExceptionResponse;
import com.cuyum.jbpm.client.kie.serialization.rest.JaxbRequestStatus;

@XmlRootElement(name = "command-response")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("rawtypes")
public class JaxbCommandsResponse {

    @XmlElement(name = "deployment-id")
    @XmlSchemaType(name = "string")
    private String deploymentId;

    @XmlElement(name = "process-instance-id")
    @XmlSchemaType(name = "long")
    private Long processInstanceId;

    @XmlElement(name = "ver")
    @XmlSchemaType(name = "String")
    private String version;

    @XmlElements({ 
            @XmlElement(name = "exception", type = JaxbExceptionResponse.class),
            @XmlElement(name = "long-list", type = JaxbLongListResponse.class),
            @XmlElement(name = "primitive", type = JaxbPrimitiveResponse.class),
            @XmlElement(name = "process-instance", type = JaxbProcessInstanceResponse.class),
            @XmlElement(name = "process-instance-list", type = JaxbProcessInstanceListResponse.class),
            @XmlElement(name = "task-response", type = JaxbTaskResponse.class),
            @XmlElement(name = "content-response", type = JaxbContentResponse.class ),
            @XmlElement(name = "task-content-response", type = JaxbTaskContentResponse.class ),
            @XmlElement(name = "task-summary-list", type = JaxbTaskSummaryListResponse.class),
            @XmlElement(name = "work-item", type = JaxbWorkItem.class),
            @XmlElement(name = "variables", type = JaxbVariablesResponse.class),
            @XmlElement(name = "other", type = JaxbOtherResponse.class),
            @XmlElement(name = "history-log-list", type = JaxbHistoryLogList.class),
            @XmlElement(name = "proc-inst-log", type = JaxbProcessInstanceLog.class),
            @XmlElement(name = "node-inst-log", type = JaxbNodeInstanceLog.class),
            @XmlElement(name = "var-inst-log", type = JaxbVariableInstanceLog.class)
            })
    private List<JaxbCommandResponse<?>> responses;

    @XmlTransient
    private static Map<Class, Class> cmdListTypes;
    static { 
        cmdListTypes = new HashMap<Class, Class>();
        // tasksummary
        cmdListTypes.put(GetTaskAssignedAsBusinessAdminCommand.class, TaskSummary.class);
        cmdListTypes.put(GetTaskAssignedAsPotentialOwnerCommand.class, TaskSummary.class);
        cmdListTypes.put(GetTasksByStatusByProcessInstanceIdCommand.class, TaskSummary.class);
        cmdListTypes.put(GetTasksOwnedCommand.class, TaskSummary.class);
        
        // long
        cmdListTypes.put(GetTaskByWorkItemIdCommand.class, Long.class);
        cmdListTypes.put(GetTasksByProcessInstanceIdCommand.class, Long.class);
        
        // string
        cmdListTypes.put(GetProcessIdsCommand.class, String.class);
        
        // processInstance
        cmdListTypes.put(GetProcessInstancesCommand.class, ProcessInstance.class);
        
        // processInstanceLog
        cmdListTypes.put(FindProcessInstancesCommand.class, ProcessInstanceLog.class);
        cmdListTypes.put(FindActiveProcessInstancesCommand.class, ProcessInstanceLog.class);
        cmdListTypes.put(FindSubProcessInstancesCommand.class, ProcessInstanceLog.class);
        
        // variableInstanceLog
        cmdListTypes.put(FindVariableInstancesByNameCommand.class, VariableInstanceLog.class);
        cmdListTypes.put(FindVariableInstancesCommand.class, VariableInstanceLog.class);
       
        // nodeInstanceLog
        cmdListTypes.put(FindNodeInstancesCommand.class, NodeInstanceLog.class);
    }

    public JaxbCommandsResponse() {
        // Default constructor
    }

    public JaxbCommandsResponse(JaxbCommandsRequest request) {
        super();
        this.deploymentId = request.getDeploymentId();
        this.processInstanceId = request.getProcessInstanceId();
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private void lazyInitResponseList() { 
        if( this.responses == null ) { 
            this.responses = new ArrayList<JaxbCommandResponse<?>>();
        }
    }

    public List<JaxbCommandResponse<?>> getResponses() {
        lazyInitResponseList();
        return responses;
    }

    public void setResponses(List<JaxbCommandResponse<?>> responses) {
        this.responses = responses;
    }

    public void addException(Exception exception, int i, Command<?> cmd, JaxbRequestStatus status) {
        lazyInitResponseList();
        this.responses.add(new JaxbExceptionResponse(exception, i, cmd, status));
    }

    @SuppressWarnings("unchecked")
    public void addResult(Object result, int i, Command<?> cmd) {
        lazyInitResponseList();
        boolean unknownResultType = false;
        
        String className = result.getClass().getName();
        if (result instanceof ProcessInstance) {
            this.responses.add(new JaxbProcessInstanceResponse((ProcessInstance) result, i, cmd));
        } else if (result instanceof JaxbTask) {
            this.responses.add(new JaxbTaskResponse((JaxbTask) result, i, cmd));
        } else if (result instanceof JaxbContent) {
            if (((JaxbContent) result).getId() == -1) {
                this.responses.add(new JaxbTaskContentResponse((JaxbContent) result, i, cmd));
            } else {
                this.responses.add(new JaxbContentResponse((JaxbContent) result, i, cmd));
            }
        } else if (List.class.isInstance(result)) { 
            // Neccessary to determine return type of empty lists
            Class listType = cmdListTypes.get(cmd.getClass());
            if( listType == null ) { 
                unknownResultType = true;
            } else if( listType.equals(TaskSummary.class) ) { 
                this.responses.add(new JaxbTaskSummaryListResponse((List<TaskSummary>) result, i, cmd));
            } else if( listType.equals(Long.class) ) {
                this.responses.add(new JaxbLongListResponse((List<Long>)result, i, cmd));
            } else if( listType.equals(ProcessInstance.class) ) {
               List<JaxbProcessInstanceResponse> procInstList = new ArrayList<JaxbProcessInstanceResponse>();
               for( ProcessInstance procInst : (List<ProcessInstance>) result) { 
                   procInstList.add(new JaxbProcessInstanceResponse(procInst));
               }
               this.responses.add(new JaxbProcessInstanceListResponse(procInstList, i, cmd));
            } else if( listType.equals(ProcessInstanceLog.class) 
                    || listType.equals(NodeInstanceLog.class)
                    || listType.equals(VariableInstanceLog.class) ) {
                this.responses.add(new JaxbHistoryLogList((List<AuditEvent>) result));
            } else { 
                throw new IllegalStateException(listType.getSimpleName() + " should be handled but is not in " + this.getClass().getSimpleName() + "!" );
            }
        } else if (result.getClass().isPrimitive() 
        		|| Boolean.class.getName().equals(className)
        		|| Byte.class.getName().equals(className)
        		|| Short.class.getName().equals(className)
        		|| Integer.class.getName().equals(className)
        		|| Character.class.getName().equals(className)
        		|| Long.class.getName().equals(className)
        		|| Float.class.getName().equals(className)
        		|| Double.class.getName().equals(className) ) {
            this.responses.add(new JaxbPrimitiveResponse(result, i, cmd));
        } else if( result instanceof WorkItem ) { 
           this.responses.add(new JaxbWorkItem((WorkItem) result, i, cmd));
        } else if( result instanceof ProcessInstanceLog ) { 
            this.responses.add(new JaxbProcessInstanceLog((ProcessInstanceLog) result));
        } else if( result instanceof NodeInstanceLog ) { 
            this.responses.add(new JaxbNodeInstanceLog((NodeInstanceLog) result));
        } else if( result instanceof VariableInstanceLog ) { 
            this.responses.add(new JaxbVariableInstanceLog((VariableInstanceLog) result));
        } else if( result instanceof DefaultFactHandle ) { 
           this.responses.add(new JaxbOtherResponse(result, i, cmd));
        } 
        // Other
        else if( result instanceof JaxbExceptionResponse ) { 
           this.responses.add((JaxbExceptionResponse) result);
        } else {
            unknownResultType = true;
        }
        
        if (unknownResultType) {
            System.out.println( this.getClass().getSimpleName() + ": unknown result type " + result.getClass().getSimpleName() 
                    + " from command " + cmd.getClass().getSimpleName() + " added.");
        }
    }
}
