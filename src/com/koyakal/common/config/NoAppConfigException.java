package com.koyakal.common.config;

import java.util.UUID;

import com.koyakal.common.security.ArmourErrorCode;


@SuppressWarnings("serial")
public class NoAppConfigException extends Exception {

	private String referenceId;
	
	public NoAppConfigException(){
		super();
		setReferenceId();
	}
	
	public NoAppConfigException(String msg){
		super(msg);
		setReferenceId();
	}
	
	public NoAppConfigException(String msg, Throwable cause){
		super(msg,cause);
		setReferenceId();
	}
	
	public String getReferenceId(){
		return referenceId;
	}
	
	@Override
	public String getMessage(){
		return getReferenceId() + super.getMessage();
	}
	
	private void setReferenceId(){
		this.referenceId = ArmourErrorCode.NO_APP_CONFIG_ERR + UUID.randomUUID();
	}
	
}

