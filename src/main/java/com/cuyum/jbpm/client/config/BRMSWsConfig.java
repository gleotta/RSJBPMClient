package com.cuyum.jbpm.client.config;

import static com.cuyum.jbpm.client.config.WSUrls.ASSIGNED_TASKS_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.ASSIGN_TASK_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.CREATE_PROCESS_INSTANCE_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.DATASET_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.LOGIN_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.LOGOUT_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.PARTICIPATIONS_TASK_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.PROCESS_DEFINITIONS_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.PROCESS_INSTANCES_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.RELEASE_TASK_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.ROLE_CHECK_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.SERVER_STATUS_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.UPDATE_TASK_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.TRANSITION_TOKEN_WS_URL;
import static com.cuyum.jbpm.client.config.WSUrls.NEW_INSTANCE_WS_URL;
import static com.cuyum.jbpm.client.config.WsConfig.WsMethod.GET;
import static com.cuyum.jbpm.client.config.WsConfig.WsMethod.POST;

import javax.ws.rs.core.MediaType;

import com.cuyum.jbpm.client.artifacts.responses.BRMSClientWSResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETAssignedTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETDatasetInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETParticipationsTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETProcessDefinitionsResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETProcessInstancesResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETRoleCheckResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETServerStatusResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTNewInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTSignalTokenResponse;



/**
 * @author Jorge Morando
 *
 */
@Deprecated
public enum BRMSWsConfig implements WsConfig {


	LOGIN_WS(LOGIN_WS_URL, MediaType.WILDCARD_TYPE, null, POST, false,false,false),
	LOGOUT_WS(LOGOUT_WS_URL, MediaType.WILDCARD_TYPE, null, POST, false,false,false),
	ROLE_CHECK_WS(ROLE_CHECK_WS_URL, MediaType.WILDCARD_TYPE, GETRoleCheckResponse.class, GET, true,true,false),
	SERVER_STATUS_WS(SERVER_STATUS_WS_URL, MediaType.WILDCARD_TYPE, GETServerStatusResponse.class, GET, false,false,false),
	PROCESS_DEFINITIONS_WS(PROCESS_DEFINITIONS_WS_URL, MediaType.WILDCARD_TYPE, GETProcessDefinitionsResponse.class, GET, true,false,false),
	PROCESS_INSTANCES_WS(PROCESS_INSTANCES_WS_URL, MediaType.WILDCARD_TYPE, GETProcessInstancesResponse.class, GET, true,true,false),
	PARTICIPATIONS_TASKS_WS(PARTICIPATIONS_TASK_WS_URL, MediaType.WILDCARD_TYPE, GETParticipationsTasksResponse.class, GET, true,true,false),
	ASSIGNED_TASKS_WS(ASSIGNED_TASKS_WS_URL, MediaType.WILDCARD_TYPE, GETAssignedTasksResponse.class, GET, true,true,false),
	ASSIGN_TASK_WS(ASSIGN_TASK_WS_URL, MediaType.WILDCARD_TYPE, null, POST, true,true,false),
	RELEASE_TASK_WS(RELEASE_TASK_WS_URL, MediaType.WILDCARD_TYPE, null, POST, true,true,false),
	CREATE_PROCESS_INSTANCE_WS(CREATE_PROCESS_INSTANCE_WS_URL, MediaType.MULTIPART_FORM_DATA_TYPE, null, POST, true,true,true),
	UPDATE_TASK_WS(UPDATE_TASK_WS_URL, MediaType.MULTIPART_FORM_DATA_TYPE, null, POST, true,true,true),
	DATASET_WS(DATASET_WS_URL, MediaType.WILDCARD_TYPE, GETDatasetInstanceResponse.class, GET, true,true,false),
	TRANSITION_TOKEN_WS(TRANSITION_TOKEN_WS_URL, MediaType.WILDCARD_TYPE, POSTSignalTokenResponse.class, POST, true,true,false),
	NEW_INSTANCE_WS(NEW_INSTANCE_WS_URL, MediaType.WILDCARD_TYPE, POSTNewInstanceResponse.class, POST, true,true,false);

	
	private boolean requiresAuth;
	private boolean hasPathParams;
	private String wsPath;
	private MediaType mediaType;
	private Class<? extends BRMSClientWSResponse> responseClass;
	private WsMethod method;
	
	private BRMSWsConfig(
			String wsPath, 
			MediaType accepts, 
			Class<? extends BRMSClientWSResponse> clazz,
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
	public MediaType accepts() {
		return mediaType;
	}

	@Override
	public Class<? extends BRMSClientWSResponse> responseClass() {
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
	public String injectPathParams2(String[]... params) {
		
		String key;
		String value;
		String ret = new String(wsPath);
		for (String[] param : params) {
			key = param[0];
			value = param[1];
			ret =  ret.replace("{"+key+"}", value);
		}
		return ret;
	}

}
