package com.maestrano.net;

import java.util.Map;

import exception.InvalidRequestException;

public class MnoApiAccountResponse<T> {
	private Boolean success;
	private T data;
	private Map<String,Object> errors;
	
	/**
	 * Raise an error if the response is unsuccessful
	 * @throws InvalidRequestException 
	 */
	public void validate() throws InvalidRequestException {
		if (this.success != null && !this.success) {
			throw new InvalidRequestException(this.getErrorsAsString());
		}
	}
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Map<String,Object> getErrors() {
		return errors;
	}
	public void setErrors(Map<String,Object> errors) {
		this.errors = errors;
	}
	
	public String getErrorsAsString() {
		return MnoApiAccountClient.GSON.toJson(this.errors);
	}
}
