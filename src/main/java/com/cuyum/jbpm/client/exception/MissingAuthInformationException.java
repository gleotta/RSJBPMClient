/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Exception Thrown when a component of the auth object is missing (User/Password)
 * @author Jorge Morando
 *
 */
public class MissingAuthInformationException extends BRMSClientException {

	private static final long serialVersionUID = 5607361167646147195L;

	/**
	 * 
	 */
	public MissingAuthInformationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MissingAuthInformationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MissingAuthInformationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MissingAuthInformationException(Throwable cause) {
		super(cause);
	}
	
}
