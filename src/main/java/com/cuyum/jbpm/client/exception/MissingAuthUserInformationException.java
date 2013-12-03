/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Exception Thrown when the auth user is missing
 * @author Jorge Morando
 *
 */
public class MissingAuthUserInformationException extends MissingAuthInformationException {

	private static final long serialVersionUID = 4124563544294661942L;

	public MissingAuthUserInformationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MissingAuthUserInformationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MissingAuthUserInformationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MissingAuthUserInformationException(Throwable cause) {
		super(cause);
	}
	
}
