/**
 * Response package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.artifacts.responses;


/**
 * POST Creates a process instance
 * @author Jorge Morando
 *
 */
public class POSTCreateInstanceResponse extends BRMSClientWSResponse {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "POSTCreateInstanceResponse [status=" + status + ", message="
				+ message + "]";
	}

	
}
