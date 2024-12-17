package com.isep.certification.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.isep.certification.commons.exceptions.dtos.Response;
import com.isep.certification.users.models.enums.LoginMode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApplicationResponseStatusExceptionResolver extends ResponseEntityExceptionHandler {

	@ResponseBody
	@ExceptionHandler(ApplicationResponseStatusException.class)
	public ResponseEntity<Response<?>> handleGlobalException(ApplicationResponseStatusException e) {

		String reason = e.getReason();
		LoginMode loginMode = e.getLoginMode();

		return loginMode == null
				? new ResponseEntity<Response<?>>(Response.build((HttpStatus) e.getStatusCode(), e.getType(), reason),
						(HttpStatus) e.getStatusCode())
				: new ResponseEntity<Response<?>>(
						Response.build((HttpStatus) e.getStatusCode(), e.getType(), reason, loginMode),
						(HttpStatus) e.getStatusCode());

	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<?>> handleGlobalException(Exception e) {
		return new ResponseEntity<Response<?>>(
				Response.build(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue"),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseBody
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<Response<?>> handleGlobalException(MalformedJwtException e) {
		return new ResponseEntity<Response<?>>(
				Response.build(HttpStatus.FORBIDDEN, "Accès non autorisé"),
				HttpStatus.FORBIDDEN);
	}

	@ResponseBody
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Response<?>> handleGlobalException(ExpiredJwtException e) {
		return new ResponseEntity<Response<?>>(
				Response.build(HttpStatus.UNAUTHORIZED, "Session expirée"),
				HttpStatus.UNAUTHORIZED);
	}

	@ResponseBody
	@ExceptionHandler(UnsupportedJwtException.class)
	public ResponseEntity<Response<?>> handleGlobalException(UnsupportedJwtException e) {
		return new ResponseEntity<Response<?>>(
				Response.build(HttpStatus.UNAUTHORIZED, "Session expirée"),
				HttpStatus.UNAUTHORIZED);
	}

	@ResponseBody
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<Response<?>> handleGlobalException(SignatureException e) {
		return new ResponseEntity<Response<?>>(
				Response.build(HttpStatus.UNAUTHORIZED, "Signature de session invalide"),
				HttpStatus.UNAUTHORIZED);
	}

}
