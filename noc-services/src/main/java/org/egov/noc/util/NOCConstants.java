package org.egov.noc.util;

import org.springframework.stereotype.Component;

@Component
public class NOCConstants {

	public static final String SEARCH_MODULE = "rainmaker-nocsrv";
	
	public static final String NOC_MODULE = "NOC";
	
	public static final String NOC_TYPE = "NocType";
	
	// mdms path codes

    public static final String NOC_JSONPATH_CODE = "$.MdmsRes.NOC";

    // error constants

	public static final String INVALID_TENANT_ID_MDMS_KEY = "INVALID TENANTID";

	public static final String INVALID_TENANT_ID_MDMS_MSG = "No data found for this tenentID";

	public static final String APPROVED_STATE = "APPROVED";	
	
	public static final String AUTOAPPROVED_STATE = "AUTO_APPROVED";	
	
	public static final String ACTION_APPROVE = "APPROVE";	
	
	public static final String MODE = "mode";	
	
	public static final String ONLINE_MODE = "online";	
	
	public static final String OFFLINE_MODE = "offline";	
	
	public static final String ONLINE_WF = "onlineWF";	

	public static final String OFFLINE_WF = "offlineWF";
	
	public static final String ACTION_REJECT = "REJECT";	
	
	public static final String WORKFLOWCODE = "workflowCode";	
	
    public static final String NOCTYPE_JSONPATH_CODE = "$.MdmsRes.NOC.NocType";
    
    public static final String NOC_DOC_TYPE_MAPPING = "DocumentTypeMapping";
    
	public static final String DOCUMENT_TYPE = "DocumentType";
	
	public static final String COMMON_MASTERS_MODULE = "common-masters";
	    
	public static final String COMMON_MASTER_JSONPATH_CODE = "$.MdmsRes.common-masters";
	
    public static final String CREATED_STATUS = "CREATED";	
    
	public static final String ACTION_VOID = "VOID";	
	
	public static final String VOIDED_STATUS = "VOIDED";	
	
}
