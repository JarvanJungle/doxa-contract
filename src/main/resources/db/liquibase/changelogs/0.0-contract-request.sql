CREATE SEQUENCE IF NOT EXISTS cr_global
    MINVALUE 1
    MAXVALUE 99999999
    CYCLE;

CREATE TABLE IF NOT EXISTS "contract_request"
(
    "id"                            BIGSERIAL PRIMARY KEY,
    -- only genearate the number during submit, not generated for save draft
    "contract_request_number"       varchar(50),
    "global_cr_number"              varchar(50),
    "contract_title"                varchar(255),
    "contracting_entity"            varchar(50),
    "contracting_owner"             varchar(50),
    "contract_type"                 varchar(255),
    "is_outsourcing_contract"       boolean,
    "currency_code"                 varchar(100),
    "contract_value"                double precision,
    contract_start_date             TIMESTAMPTZ,
    contract_end_date               TIMESTAMPTZ,
    "payment_term_name"             varchar(100),
    "payment_term_uuid"             varchar(100),
    -- renewal option (Yes, No)
    "renewal_option"                varchar(50),
    created_date                    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    submitted_date                  TIMESTAMPTZ,
    "created_by_name"               varchar(255) NOT NULL,
    "created_by_uuid"               varchar(255) NOT NULL,
    "is_project"                    boolean,
    "project_name"                  VARCHAR(50),
    "project_uuid"                  VARCHAR(255),
    "project_rfq_no"                VARCHAR(255),
    project_delivery_date           TIMESTAMPTZ,
    "project_delivery_address"      varchar(500),
    "total_used_currency_code"      varchar(100),
    "total_used"                    double precision,
    "product_service_description"   VARCHAR(500),
    "approval_matrix_uuid"          varchar(255),
    "approval_matrix_name"          varchar(255),
    "approval_sequence"             VARCHAR(255),
    "approval_sequence_groups_uuid" VARCHAR(500),
    "next_approver_group"           varchar(100),
    "next_approver_group_uuid"      varchar(100),
    "status"                        varchar(255) NOT NULL,
    "total_amount"                  double precision,
    updated_date                    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    approved_date                   TIMESTAMPTZ,
    "supplier_name"                 varchar(100),
    "supplier_uuid"                 varchar(100),
    "is_connected"                  boolean,
    "is_converted"                  boolean,
    "uuid"                          varchar(255) NOT NULL,
    "company_uuid"                  varchar(50)  NOT NULL
    );


CREATE TABLE IF NOT EXISTS "contract_request_item"
(
    "id"                              BIGSERIAL PRIMARY KEY,
    "item_code"                       varchar(50),
    "item_name"                       varchar(255),
    "item_description"                varchar(255),
    "item_model"                      varchar(255),
    "item_size"                       varchar(50),
    "item_brand"                      varchar(255),
    "trade"                           varchar(255),
    "uom_code"                        varchar(50),
    "item_quantity"                   double precision,
    "currency_code"                   varchar(50),
    "item_unit_price"                 double precision,
    "tax_code"                        varchar(50),
    "tax_percentage"                  double precision,
    "in_source_currency_before_tax"   double precision,
    "exchange_rate"                   double precision,
    "in_document_currency_before_tax" double precision,
    "in_document_currency_tax_amount" double precision,
    "in_document_currency_after_tax"  double precision,
    "delivery_address"                varchar(255),
    requested_delivery_date           TIMESTAMPTZ,
    "gl_account_number"               varchar(255),
    "gl_account_uuid"                 varchar(255),
    "note"                            varchar(500),
    "manual_item"                     boolean,
    "contract_request_id"             bigint
    );
ALTER TABLE "contract_request_item" DROP CONSTRAINT IF EXISTS "fk_item_contract_request_id";

ALTER TABLE "contract_request_item"
    ADD CONSTRAINT "fk_item_contract_request_id" FOREIGN KEY ("contract_request_id") REFERENCES "contract_request" ("id");


CREATE TABLE IF NOT EXISTS "contract_request_document_metadata"
(
    "id"                  BIGSERIAL PRIMARY KEY,
    "guid"                varchar(50)  NOT NULL,
    "file_label"          varchar(50)  NOT NULL,
    "file_description"    varchar(255) NOT NULL,
    uploaded_on           TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    "uploaded_by_name"    varchar(100) NOT NULL,
    "uploaded_by_uuid"    varchar(50)  NOT NULL,
    "contract_request_id" bigint
    );

ALTER TABLE "contract_request_document_metadata" DROP CONSTRAINT IF EXISTS "fk_document_contract_request_id";
ALTER TABLE "contract_request_document_metadata"
    ADD CONSTRAINT "fk_document_contract_request_id" FOREIGN KEY ("contract_request_id") REFERENCES "contract_request" ("id");


CREATE TABLE IF NOT EXISTS "contract_request_audit_trail"
(
    "id"                  BIGSERIAL PRIMARY KEY,
    "user_uuid"           varchar(50)  NOT NULL,
    "user_name"           varchar(100) NOT NULL,
    "role"                varchar(50),
    "action"              varchar(255) NOT NULL,
    created_date          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    "approval_group_uuid" varchar(255),
    "contract_request_id" bigint
    );
ALTER TABLE "contract_request_audit_trail" DROP CONSTRAINT IF EXISTS "fk_audit_contract_request_id";

ALTER TABLE "contract_request_audit_trail"
    ADD CONSTRAINT "fk_audit_contract_request_id" FOREIGN KEY ("contract_request_id") REFERENCES "contract_request" ("id");
