package com.koyakal.common.security;

public class ArmourErrorCode {

	public static final String AMR_ERR_001 = "AMR_ERR_001:AppName is expected in the request - AppName is null or empty";
	public static final String AMR_ERR_002 = "AMR_ERR_002:AppId not available or configured";
	public static final String AMR_ERR_003 = "AMR_ERR_003:App Key not available or configured";
	public static final String AMR_ERR_004 = "AMR_ERR_004:Input AppId and Key combination does not match";
	public static final String AMR_ERR_005 = "AMR_ERR_005:AppId is expected in the request - AppId is null or empty";
	public static final String AMR_ERR_006 = "AMR_ERR_006:App Key should contain 16 Characters. Neither more nor less.";
	
	//Exception ReferenceNumber prefix
	public static final String NO_APP_CONFIG_ERR = "NO-CNF-";
	public static final String AMR_EXCEPTION = "ARMOR-";
}