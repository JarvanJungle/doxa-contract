package org.doxa.contract.config;

public class ControllerPath {

    public static final String CONTRACT = "/{companyUuid}/contract";
    public static final String BUYER = "/buyer";
    public static final String SUPPLIER = "/supplier";
    public static final String GET = "/get";
    public static final String LIST = "/list";
    public static final String CONTRACT_UUID = "/contract-uuid/{contractUuid}";
    public static final String CONVERT = "/convert";
    public static final String SUBMIT = "/submit";
    public static final String SAVE_DRAFT = "/save-draft";
    public static final String CREATE = "/create";
    public static final String EDIT = "/edit";
    public static final String ISSUE = "/issue";
    public static final String APPROVE = "/approve";
    public static final String REJECT = "/reject";
    public static final String SEND_BACK = "/send-back";
    public static final String RECALL = "/recall";
    public static final String CANCEL = "/cancel";
    public static final String ACKNOWLEDGE = "/acknowledge";
    public static final String TERMINATE = "/terminate";
    public static final String CONTRACT_REQUEST = "/{companyUuid}/contract-request";
    public static final String CR_SUBMIT = "/submit";
    public static final String CR_SAVE_DRAFT = "/save-draft";
    public static final String CR_LIST = "/list";
    public static final String CR_DETAILS = "/details/{contractRequestUuid}";
    public static final String CR_RECALL = "/recall/{contractRequestUuid}";
    public static final String CR_CANCEL = "/cancel/{contractRequestUuid}";
    public static final String CR_APPROVE = "/approve/{contractRequestUuid}";
    public static final String CR_SENTBACK = "/sentback/{contractRequestUuid}";
    public static final String CR_REJECT = "/reject/{contractRequestUuid}";
    public static final String CR_EDIT_SUBMIT = "/edit-submit";
    public static final String CONTRACT_REQUEST_UUID = "/{contractRequestUuid}";

    //private controller path
    public static final String PRIVATE = "/private";
    public static final String PRIVATE_CONVERT_RFQ_TO_CONTRACT = "/convert-rfq-to-contract";

}
