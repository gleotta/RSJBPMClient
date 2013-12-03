/**
 * 
 */
package com.cuyum.jbpm.client.config;

/**
 * BRMS REST Web Services Central URL Repository
 * @author Jorge Morando
 *
 */
public class WSUrls {
	
	public static final String LOGIN_WS_URL = "/business-central-server/rs/identity/secure/j_security_check";
	public static final String LOGOUT_WS_URL = "/business-central-server/rs/identity/sid/invalidate";
	public static final String ROLE_CHECK_WS_URL = "/business-central-server/rs/identity/user/roles?roleCheck={roleCheck}";
	public static final String SID_WS_URL = "/business-central-server/rs/identity/secure/sid";
	public static final String SERVER_STATUS_WS_URL = "/business-central-server/rs/server/status";
	public static final String PROCESS_DEFINITIONS_WS_URL = "/business-central-server/rs/process/definitions";
	public static final String PROCESS_INSTANCES_WS_URL = "/business-central-server/rs/process/definition/{processDefinitionId}/instances";
	public static final String ASSIGNED_TASKS_WS_URL = "/business-central-server/rs/tasks/{actorId}";
	public static final String PARTICIPATIONS_TASK_WS_URL = "/business-central-server/rs/tasks/{actorId}/participation";
	public static final String ASSIGN_TASK_WS_URL = "/business-central-server/rs/task/{taskId}/assign/{actorId}";
	public static final String CREATE_PROCESS_INSTANCE_WS_URL = "/business-central-server/rs/form/process/{processId}/complete";
	public static final String UPDATE_TASK_WS_URL = "/business-central-server/rs/form/task/{taskId}/complete";
	public static final String RELEASE_TASK_WS_URL = "/business-central-server/rs/task/{taskId}/release";
	public static final String DATASET_WS_URL = "/business-central-server/rs/process/instance/{id}/dataset";
	public static final String TRANSITION_TOKEN_WS_URL = "/business-central-server/rs/process/tokens/{tokenId}/transition?signal={signalValue}";
	public static final String NEW_INSTANCE_WS_URL = "/business-central-server/rs/process/definition/{idProceso}/new_instance";

	
}
