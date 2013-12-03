/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Exception Thrown when a ws fails (HTTP STATUS CODE RANGES 400/500)
 * @author Jorge Morando
 *
 */
public class WSException extends BRMSClientException {

	private static final long serialVersionUID = 5607361167646147195L;

	public WSException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WSException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public WSException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WSException(Throwable cause) {
		super(cause);
	}
	
}
