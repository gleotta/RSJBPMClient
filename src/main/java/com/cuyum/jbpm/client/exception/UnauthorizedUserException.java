/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Exception Thrown when a user is not authorized in BRMS plataform
 * @author Jorge Morando
 *
 */
public class UnauthorizedUserException extends BRMSClientException {

	private static final long serialVersionUID = 5607361167646147195L;

	/**
	 * 
	 */
	public UnauthorizedUserException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnauthorizedUserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UnauthorizedUserException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnauthorizedUserException(Throwable cause) {
		super(cause);
	}
	
}
