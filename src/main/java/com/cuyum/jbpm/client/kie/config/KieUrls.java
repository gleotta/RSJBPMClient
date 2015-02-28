/**
 * 
 */
package com.cuyum.jbpm.client.kie.config;


/**
 * BRMS REST Web Services Central URL Repository
 * @author Jorge Morando
 *
 */
public class KieUrls {
	
	//public static final String LOGIN_WS_URL = "/business-central-server/rs/identity/secure/j_security_check";
	//public static final String LOGOUT_WS_URL = "/business-central-server/rs/identity/sid/invalidate";
	//public static final String ROLE_CHECK_WS_URL = "/business-central-server/rs/identity/user/roles?roleCheck={roleCheck}";
	//public static final String SID_WS_URL = "/business-central-server/rs/identity/secure/sid";
	public static final String SERVER_STATUS_WS_URL = "/deployment/{deploymentId}"; //DONE
	//public static final String PROCESS_DEFINITIONS_WS_URL = "/business-central-server/rs/process/definitions";
	public static final String PROCESS_INSTANCES_WS_URL = "/runtime/{deploymentId}/history/process/{processDefinitionId}"; //done
	public static final String PROCESS_INSTANCE_WS_URL =  "/runtime/{deploymentId}/process/instance/{processInstanceId}";
	public static final String QUERY_TASKS_WS_URL = "/task/query"; //done
	public static final String ASSIGN_TASK_WS_URL = "/task/{taskId}/claim"; //done
	public static final String CREATE_PROCESS_INSTANCE_WS_URL = "/runtime/{deploymentId}/process/{processDefId}/start"; //done
	public static final String UPDATE_TASK_WS_URL = "/task/{taskId}/complete";
	public static final String RELEASE_TASK_WS_URL = "/task/{taskId}/release";
	public static final String DATASET_WS_URL = "/runtime/{deploymentId}/withvars/process/instance/{processInstanceId}"; //DONE
	public static final String TRANSITION_TOKEN_WS_URL = "/runtime/{deploymentId}/process/instance/{processInstanceId}/signal";//DONE
	//public static final String NEW_INSTANCE_WS_URL = "/business-central-server/rs/process/definition/{idProceso}/new_instance";

		
}
