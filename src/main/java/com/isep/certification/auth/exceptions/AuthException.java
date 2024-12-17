package com.isep.certification.auth.exceptions;

import com.isep.certification.commons.exceptions.ApplicationResponseStatusException;

public class AuthException extends ApplicationResponseStatusException {
	private AuthErrors errors;

	public AuthException(AuthErrors errors) {
		super(errors.status, errors.name(), errors.reason, errors.loginMode);
		this.errors = errors;
	}

	public AuthException(AuthErrors errors, Throwable cause) {
		super(errors.status, errors.name(), errors.reason, cause);
		this.errors = errors;
	}

	public AuthErrors getErrors() {
		return errors;
	}
}
