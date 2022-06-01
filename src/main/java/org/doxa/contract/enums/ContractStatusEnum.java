package org.doxa.contract.enums;

public enum ContractStatusEnum {
    PENDING_ISSUE("Pending issue"),
    PENDING_ACKNOWLEDGEMENT("Pending acknowledgment"),
    COMPLETE("Complete"),
    PENDING_ESIGN("Pending e-sign"),
    TERMINATED("Terminated"),
    REJECTED("Rejected"),
    DRAFT_CONTRACT("Draft contract"),
    PENDING_APPROVAL("Pending approval"),
    PENDING_SUBMISSION("Pending Submission"),
    RECALLED("Recalled"),
    SENT_BACK("Sent back");

    private final String config;

    ContractStatusEnum(String config) {
        this.config=config;
    }

    public String getValue() {
        return this.config;
    }
}
