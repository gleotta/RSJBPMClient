/**
 * Response package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.artifacts.responses;

import java.util.List;

import com.cuyum.jbpm.client.artifacts.ProcessDefinition;


/**
 * GET Process Definitions Response
 * @author Jorge Morando
 *
 */
public class GETProcessDefinitionsResponse extends BRMSClientWSResponse {

	List<ProcessDefinition> definitions;
	
	/**
	 * @return the definitions
	 */
	public List<ProcessDefinition> getDefinitions() {
		return definitions;
	}

	/**
	 * @param definitions the definitions to set
	 */
	public void setDefinitions(List<ProcessDefinition> definitions) {
		this.definitions = definitions;
	}

	@Override
	public String toString(){
		return definitions.toString();
	}
	
}
