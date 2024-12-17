package com.isep.certification.commons.exceptions.dtos;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.isep.certification.users.models.enums.LoginMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

	int status;
	LoginMode loginMode;
	List<Error> errors;

	public static Response<NoContent> noContent(HttpStatus httpStatus) {
		Response<NoContent> response = new Response<NoContent>();
		NoContent data = new NoContent();
		data.setSuccess(true);
		response.setStatus(httpStatus.value());

		return response;
	}

	public static <T> Response<T> build(HttpStatus httpStatus, T data) {
		Response<T> response = new Response<T>();
		response.setStatus(httpStatus.value());

		return response;
	}

	public static Response build(HttpStatus httpStatus, String errorMessaqe) {
		Response response = new Response();
		response.setStatus(httpStatus.value());
		Error error = new Error(httpStatus.name(), errorMessaqe);
		response.setErrors(Arrays.asList(error));

		return response;
	}

	public static Response build(HttpStatus httpStatus, String errorName, String errorReason) {
		Response response = new Response();
		response.setStatus(httpStatus.value());
		Error error = new Error(errorName, errorReason);
		response.setErrors(Arrays.asList(error));

		return response;
	}

	public static Response build(HttpStatus httpStatus, String errorName, String errorReason, LoginMode loginMode) {
		Response response = new Response();
		response.setStatus(httpStatus.value());
		response.setLoginMode(loginMode);
		Error error = new Error(errorName, errorReason);
		response.setErrors(Arrays.asList(error));

		return response;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Error {
		String type;
		String message;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class NoContent {

		boolean success;
	}
}
