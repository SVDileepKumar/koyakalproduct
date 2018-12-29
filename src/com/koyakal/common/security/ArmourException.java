package com.koyakal.common.security;

import java.util.UUID;


@SuppressWarnings("serial")
public class ArmourException extends Exception {
	
	private String referenceId;
	
	public ArmourException(){
		super();
		setReferenceId();
	}
	
	public ArmourException(String msg){
		super(msg);
		setReferenceId();
	}
	
	public ArmourException(String msg, Throwable cause){
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
		this.referenceId = ArmourErrorCode.AMR_EXCEPTION + UUID.randomUUID();
	}
	
}

