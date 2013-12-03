/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Exception Thrown when the auth password is missing
 * @author Jorge Morando
 *
 */
public class MissingAuthPasswordInformationException extends MissingAuthInformationException {

	private static final long serialVersionUID = -1981116868716786349L;

	public MissingAuthPasswordInformationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MissingAuthPasswordInformationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MissingAuthPasswordInformationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MissingAuthPasswordInformationException(Throwable cause) {
		super(cause);
	}
	
}
