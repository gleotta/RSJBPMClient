/**
 * Artifact package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.artifacts;

import java.util.Date;

/**
 * @author Jorge Morando
 * 
 */
public class ProcessInstance {

	protected Long id;

	protected String definitionId;

	protected String startDate;
	
	protected int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	protected boolean suspended;

	protected ProcessInstanceToken rootToken;

	protected String key;
	
	protected String endDate;
	
	protected String endResult;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the definitionId
	 */
	public String getDefinitionId() {
		return definitionId;
	}

	/**
	 * @param definitionId
	 *            the definitionId to set
	 */
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}


	/**
	 * @return the suspended
	 */
	public boolean isSuspended() {
		return suspended;
	}

	/**
	 * @param suspended
	 *            the suspended to set
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	/**
	 * @return the rootToken
	 */
	public ProcessInstanceToken getRootToken() {
		return rootToken;
	}

	/**
	 * @param rootToken
	 *            the rootToken to set
	 */
	public void setRootToken(ProcessInstanceToken rootToken) {
		this.rootToken = rootToken;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the endResult
	 */
	public String getEndResult() {
		return endResult;
	}

	/**
	 * @param endResult the endResult to set
	 */
	public void setEndResult(String endResult) {
		this.endResult = endResult;
	}

	@Override
	public String toString() {
		return "ProcessInstance ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (definitionId != null ? "definitionId=" + definitionId + ", "
						: "")
				+ (startDate != null ? "startDate=" + startDate + ", " : "")
				+ "status=" + status + ", suspended=" + suspended + ", "
				+ (rootToken != null ? "rootToken=" + rootToken + ", " : "")
				+ (key != null ? "key=" + key + ", " : "")
				+ (endDate != null ? "endDate=" + endDate + ", " : "")
				+ (endResult != null ? "endResult=" + endResult : "") + "]";
	}


}
