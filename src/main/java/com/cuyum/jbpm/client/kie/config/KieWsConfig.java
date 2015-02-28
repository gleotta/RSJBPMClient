package com.cuyum.jbpm.client.kie.config;

import static com.cuyum.jbpm.client.config.WsConfig.WsMethod.GET;
import static com.cuyum.jbpm.client.config.WsConfig.WsMethod.POST;
import static com.cuyum.jbpm.client.kie.config.KieUrls.ASSIGN_TASK_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.CREATE_PROCESS_INSTANCE_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.DATASET_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.PROCESS_INSTANCES_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.PROCESS_INSTANCE_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.RELEASE_TASK_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.SERVER_STATUS_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.TRANSITION_TOKEN_WS_URL;
import static com.cuyum.jbpm.client.kie.config.KieUrls.UPDATE_TASK_WS_URL;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.kie.services.client.serialization.jaxb.impl.audit.JaxbHistoryLogList;
import org.kie.services.client.serialization.jaxb.impl.deploy.JaxbDeploymentUnit;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceResponse;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceWithVariablesResponse;
import org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskSummaryListResponse;
import org.kie.services.client.serialization.jaxb.rest.JaxbGenericResponse;

import com.cuyum.jbpm.client.config.WsConfig;




/**
 * @author Jorge Morando
 *
 */
public enum KieWsConfig implements WsConfig {


	//LOGIN_WS(LOGIN_WS_URL, MediaType.WILDCARD_TYPE, null, POST, false,false,false),
	//LOGOUT_WS(LOGOUT_WS_URL, MediaType.WILDCARD_TYPE, null, POST, false,false,false),
	//ROLE_CHECK_WS(ROLE_CHECK_WS_URL, MediaType.WILDCARD_TYPE, GETRoleCheckResponse.class, GET, true,true,false),
	SERVER_STATUS_WS(SERVER_STATUS_WS_URL, JaxbDeploymentUnit.class, GET),
	CREATE_PROCESS_INSTANCE_WS(CREATE_PROCESS_INSTANCE_WS_URL, JaxbProcessInstanceResponse.class, POST),
	//PROCESS_DEFINITIONS_WS(PROCESS_DEFINITIONS_WS_URL,GETProcessDefinitionsResponse.class, GET),
	PROCESS_INSTANCES_WS(PROCESS_INSTANCES_WS_URL, JaxbHistoryLogList.class, GET),
	PROCESS_INSTANCE_WS(PROCESS_INSTANCE_WS_URL, JaxbHistoryLogList.class, GET),
	QUERY_TASKS_WS(KieUrls.QUERY_TASKS_WS_URL, JaxbTaskSummaryListResponse.class, GET),
	ASSIGN_TASK_WS(ASSIGN_TASK_WS_URL, JaxbGenericResponse.class, POST),
	RELEASE_TASK_WS(RELEASE_TASK_WS_URL, JaxbGenericResponse.class, POST),
	UPDATE_TASK_WS(UPDATE_TASK_WS_URL,JaxbGenericResponse.class, POST),
	DATASET_WS(DATASET_WS_URL, JaxbProcessInstanceWithVariablesResponse.class, GET),
	TRANSITION_TOKEN_WS(TRANSITION_TOKEN_WS_URL,JaxbGenericResponse.class, POST);
	//NEW_INSTANCE_WS(NEW_INSTANCE_WS_URL, POSTNewInstanceResponse.class, POST);

	
	private boolean requiresAuth;
	private boolean hasPathParams;
	private String wsPath;
	private String mediaType;
	private Class<?> responseClass;
	private WsMethod method;
	
	private KieWsConfig(
			String wsPath, 
			Class<?> clazz,
			WsMethod method){
		this( wsPath, MediaType.APPLICATION_XML, clazz,method, true, true, false);
	}
	
	private KieWsConfig(
			String wsPath,
			String accepts,
			Class<?> clazz,
			WsMethod method,
			boolean requiresAuth,
			boolean hasPathParams,
			boolean hasFormBody){
		this.wsPath = wsPath;
		this.requiresAuth = requiresAuth;
		this.mediaType = accepts;
		this.responseClass = clazz;
		this.method = method;
		this.hasPathParams = hasPathParams;
	}
	
	@Override
	public String path() {
		return wsPath;
	}

	@Override
	public boolean requiresAuth() {
		return requiresAuth;
	}

	@Override
	public String accepts() {
		return mediaType;
	}

	@Override
	public Class<?> responseClass() {
		return responseClass;
	}
	
	@Override
	public WsMethod getMethod() {
		return method;
	}
	
	@Override
	public boolean hasPathParams() {
		return hasPathParams;
	}

	@Override
	public String injectPathParams2(Map<String, String> pathParams) {
		String ret = new String(wsPath);
		Set<String> keys = pathParams.keySet();
		for (String key : keys) {
			String value = pathParams.get(key);
			ret =  ret.replace("{"+key+"}", value);
		}
		
		return ret;
	}

//	@Override
//	public String injectPathParams2(String[]... params) {
//		
//		String key;
//		String value;
//		String ret = new String(wsPath);
//		for (String[] param : params) {
//			key = param[0];
//			value = param[1];
//			ret =  ret.replace("{"+key+"}", value);
//		}
//		return ret;
//	}

}
