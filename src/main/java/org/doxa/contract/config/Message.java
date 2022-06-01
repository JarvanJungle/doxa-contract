package org.doxa.contract.config;

public enum Message {
	SAMPLE_MESSAGE ("This is the sample message"),
	ACCESS_DENIED("Access Denied, user don't have permission to access/modify resource: "),
	
	INVALID_APPROVALMATRIX ("Invalid approval matrix"),
	CONTRACT_REQUEST ("Contract Request"),
	INVALID_ACTION ("Action is invalid for this record with the current status"),
	CONTRACT_REQUEST_NOT_FOUND ("Contract request not found"),
	CONTRACT_REQUEST_RECALL_ACCESS_DENIED ("Recall for contract request denied as not creator"),
	CONTRACT_REQUEST_CANCEL_ACCESS_DENIED ("Cancel for contract request denied as not creator"),
	RECALL_INVALID ("Recall action is invalid at current stage"),
	CANCEL_INVALID ("Cancel action is invalid at current stage"),
	USER_HAS_APPROVED ("Invalid as user has already done his approval"),
	NO_PERMISSION ("User do not has the permission to approve, reject or send back"),
	HAS_BEEN_REJECTED ("Record has been rejected. Hence no approval can be made"),
	
	//General
	NOT_CONNECTED("Not connected between buyer and supplier"),
	MISSING_NECESSARY_FIELDS ("Necessary fields are missing"),
	CANNOT_APPROVE_REJECT_SENDBACK_APPROVED("User cannot approve, reject or sendback already approved purchase requisition"),
	SUBMIT ("Submit"),
	RECALL ("Recall"),
	CANCEL ("Cancel"),
	DRAFT_SAVED ("Draft Saved"),
	CREATED_SUCCESSFULLY("Submit is successful"),
	RECALL_SUCCESSFULLY ("Recall is successful"),
	CANCEL_SUCCESSFULLY ("Cancel is successful"),
	APPROVE_SUCCESSFULLY ("Approve is successful"),
	SENTBACK_SUCCESSFULLY ("Send back is successful"),
	REJECTED_SUCCESSFULLY ("Reject is successful"),
	DRAFT_SAVED_SUCCESSFULLY("Draft save is successful"),
	PENDING_CONTRACT_CREATED_SUCCESSFULLY("Pending contract for submission created successfully"),
	CONTRACT_DOES_NOT_EXIST("The contract does not exist"),
	CONTRACT_SUBMITTED_SUCCESSFULLY("The contract is submitted successfully"),
	CONTRACT_SAVED_SUCCESSFULLY("The contract has been saved successfully"),
	CONTRACT_SUBMISSION_DENIED("The current contract status is not allowed to be submitted"),
	CONTRACT_STATUS_CHANGE_DENIED("The current contract status is not allowed to change"),
	CONTRACT_REJECTED("The contract has been rejected"),
	CONTRACT_SENDBACK("The contract has been sendback"),
	CONTRACT_APPROVED("The contract has been approved"),
	CONTRACT_RECALLED("The contract has been recalled"),
	CONTRACT_ISSUED("The contract has been issued"),
	CONTRACT_TERMINATED("The contract has been terminated"),
	CONTRACT_ACKNOWLEDGE("The supplier has acknowledge the contract"),
	CREATOR_CANNOT_APPROVE("The creator of the contract cannot approve"),
	APPROVER_CANNOT_APPROVE_AGAIN("The approver can not approve again"),
	CONTRACT_CANNOT_BE_RECALLED("The contract cannot be recalled"),
	CONTRACT_MISSING_PROJECT_DETAILS("The contract is a project and is missing details"),
	RETRIEVE_SUPPLIERS_FAILED ("Retrieval of supplier list from entities service failed"),
	CONTRACT_EXIST_WITH_THIS_CR("Cannot create a new contract with this contract request number"),
	MANUAL_CT_NUMBER("Contract number needs to be manually fill in"),
	NON_UNIQUE_CT_NUMBER("The Contract Number is not unique"),

	OBJECT_DOESNOT_EXIST("Obejct doesn't exist"),

	SUPPLIER_NOT_FOUND("Supplier information not found"),
	INVALID_PROJECT("Project doesn't exist or has been closed"),
	USER_NOT_IN_PROJECT("Permission denied, user does not belong to this project");

	private final String constMessages;
	
	Message(String constMessages) {
		this.constMessages=constMessages;
	}
	
	public String getValue() {
		return this.constMessages;
	}

}
