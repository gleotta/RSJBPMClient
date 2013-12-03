/**
 * 
 */
package com.cuyum.jbpm.client.artifacts;

import java.util.List;

/**
 * @author Jorge Morando
 *
 */
public class ProcessInstanceToken {

	private Long id;
	
	private String name;
	
	private String currentNodeName;
	
	private List<ProcessInstanceToken> children;
	
	private List<String> availableSignals;
	
	private boolean canBeSignaled; 
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the currentNodeName
	 */
	public String getCurrentNodeName() {
		return currentNodeName;
	}

	/**
	 * @param currentNodeName the currentNodeName to set
	 */
	public void setCurrentNodeName(String currentNodeName) {
		this.currentNodeName = currentNodeName;
	}

	/**
	 * @return the children
	 */
	public List<ProcessInstanceToken> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<ProcessInstanceToken> children) {
		this.children = children;
	}

	/**
	 * @return the availableSignals
	 */
	public List<String> getAvailableSignals() {
		return availableSignals;
	}

	/**
	 * @param availableSignals the availableSignals to set
	 */
	public void setAvailableSignals(List<String> availableSignals) {
		this.availableSignals = availableSignals;
	}

	/**
	 * @return the canBeSignaled
	 */
	public boolean isCanBeSignaled() {
		return canBeSignaled;
	}

	/**
	 * @param canBeSignaled the canBeSignaled to set
	 */
	public void setCanBeSignaled(boolean canBeSignaled) {
		this.canBeSignaled = canBeSignaled;
	}

	@Override
	public String toString() {
		return "ProcessInstanceToken [id=" + id + ", name=" + name
				+ ", currentNodeName=" + currentNodeName + ", children="
				+ children + ", availableSignals=" + availableSignals
				+ ", canBeSignaled=" + canBeSignaled + "]";
	}
	
	
	
		
}
