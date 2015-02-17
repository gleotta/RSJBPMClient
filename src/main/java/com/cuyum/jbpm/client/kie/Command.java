package com.cuyum.jbpm.client.kie;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.kie.services.client.serialization.jaxb.impl.JaxbCommandResponse;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbHistoryLogList;
import org.kie.services.client.serialization.jaxb.impl.deploy.JaxbDeploymentUnit;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceWithVariablesResponse;
import org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskSummaryListResponse;
import org.kie.services.client.serialization.jaxb.rest.JaxbGenericResponse;


public enum Command {
	//http://162.243.12.101:8080/business-central/rest/runtime/exportacionTerrestre:exportacion-terrestre:1.0/withvars/process/instance/24/signal?signal=boo
	PROCESS_INSTANCE_WITH_VARIABLES("/runtime/{deploymentId}/withvars/process/instance/{processInstanceId}",JaxbProcessInstanceWithVariablesResponse.class,MediaType.APPLICATION_XML),
	PROCESS_INSTANCE_SIGNAL("/runtime/{deploymentId}/process/instance/{processInstanceId}/signal",JaxbGenericResponse.class,MediaType.APPLICATION_XML),
	TASK("/task/query", JaxbTaskSummaryListResponse.class,  MediaType.APPLICATION_XML),
	//PROCESS_INSTANCE_HISTORY("/runtime/{deploymentId}/history/instance/{processInstanceId}/variable",ProcessCompletedResponse.class,MediaType.APPLICATION_JSON),
	//TASK_VARIABLES("/task/{taskId}/content",TaskVarResponse.class,MediaType.APPLICATION_JSON),
	TASK_CLAIM("/task/{taskId}/claim", JaxbGenericResponse.class, MediaType.APPLICATION_XML),
	//PROCESS_INSTANCE_HISTORY_VAR_VALUE("/history/variable/{var}/value/{value}/instances",ProcessInstanceLogList.class,MediaType.APPLICATION_XML),
	DEPLOYMENT_ID("/deployment/{deploymentId}",JaxbDeploymentUnit.class,MediaType.APPLICATION_XML),
	RUNTIME_DEPLOYMENTID_EXECUTE("/runtime/{deploymentId}/execute",JaxbCommandResponse.class,MediaType.APPLICATION_XML),
	VARIABLE_INSTANCE_HISTORY("/history/instance/{procInstId}/variable/{varId}",JaxbHistoryLogList.class ,MediaType.APPLICATION_XML);
	 
	
	private String wsUrl;
	private  Class<?> clazz;
	private String content;
	
	
	private Command(String wsUrl, Class<?> clazz, String content){
		this.wsUrl=wsUrl;
		this.clazz = clazz;
		this.content=content;
	}
	
	
	/**
	 * @return the clazz
	 */
	public Class<?> getResponseClass() {
		return clazz;
	}

	public String getUrl(){
		return wsUrl;
	}
	
	public String getUrl(Map<String,String> params){
		if(params!=null)
			return withPathParams(params);
		return getUrl();
	}
	
	private String withPathParams(Map<String,String> params){
		String url = new String(wsUrl);
		for (String key : params.keySet()) {
			String value = params.get(key);
			url = url.replaceAll("\\{"+key+"\\}", value);
		}
		return url;
	}
	
	public String getQuery(Map<String, List<String>> parameters) {
		String url = "?";
		String param;
		for (String key : parameters.keySet()) {
			if(parameters.get(key)!=null){
				for (String value : parameters.get(key)) {
					if(value!=null){
						param = key + "=" + value + "&";
						url = url + param;
					}
				}
			}
		}
		url= url.substring(0, url.length()-1);
		return url;
	}

	public String getContentType() {
		return content;
	}
}