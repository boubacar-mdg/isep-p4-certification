package com.isep.certification.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.isep.certification.users.models.enums.LoginMode;

public class ApplicationResponseStatusException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;
	String type;
	LoginMode loginMode;

	public ApplicationResponseStatusException(HttpStatus status) {
		super(status);
	}

	public ApplicationResponseStatusException(HttpStatus status, String type, String reason) {
		super(status, reason);
		this.type = type;
	}

	public ApplicationResponseStatusException(HttpStatus status, String type, String reason, LoginMode loginMode) {
		super(status, reason);
		this.type = type;
		this.loginMode = loginMode;
	}

	public ApplicationResponseStatusException(HttpStatus status, String type, String reason, Throwable cause) {
		super(status, reason, cause);
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public LoginMode getLoginMode() {
		return this.loginMode;
	}
	
}
