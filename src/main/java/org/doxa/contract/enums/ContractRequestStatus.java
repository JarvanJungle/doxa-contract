package org.doxa.contract.enums;

import org.doxa.contract.config.DoxaServiceNameConsts;

public enum ContractRequestStatus {
	REJECTED ("Rejected"),
	DRAFT_CONTRACT_REQUEST ("Draft Contract Request"),
	RECALLED ("Recalled"),
	SENT_BACK ("Sent Back"),
	APPROVE ("Approve"),
	REJECT ("Reject"),
	CANCELLED("Cancelled"),
	APPROVED ("Approved"),
	CONTRACT_PENDING_CONVERSION ("Contract Pending Conversion"),
	REQUEST_PENDING_APPROVAL ("Request Pending Approval"),
	CONVERTED("Converted to Contract");

    private final String config;

    ContractRequestStatus(String config) {
        this.config=config;
    }

    public String getValue() {
        return this.config;
    }
}
