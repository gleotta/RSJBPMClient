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

	private long id;
	
	private int state;
	private String processId;
	private String processName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	@Override
	public String toString() {
		return "POSTCreateInstanceResponse [id=" + id + ", state=" + state
				+ ", "
				+ (processId != null ? "processId=" + processId + ", " : "")
				+ (processName != null ? "processName=" + processName : "")
				+ "]";
	}
	
	
}
