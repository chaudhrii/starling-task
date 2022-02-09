package com.chaudhrii.sterlingtechtask.core.exception;

public class StarlingException extends RuntimeException {
	public StarlingException(final String message) {
		super(message);
	}

	public StarlingException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
