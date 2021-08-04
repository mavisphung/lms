package com.lmsapp.project.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseException extends Exception {
	
	private String code;
	private String message;
	
	
	public ResponseException() {
		super();
	}
	public ResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public ResponseException(String message, Throwable cause) {
		super(message, cause);
	}
	public ResponseException(String message) {
		super(message);
	}
	public ResponseException(Throwable cause) {
		super(cause);
	}
	public ResponseException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
