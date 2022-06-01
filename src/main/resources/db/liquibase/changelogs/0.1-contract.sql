CREATE SEQUENCE IF NOT EXISTS contract_global
    MINVALUE 1
    MAXVALUE 99999999
    CYCLE;

CREATE TABLE IF NOT EXISTS "contact_information"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "contact_name"   varchar(255),
    "contact_email"  varchar(255),
    "contact_number" varchar(255)
    );

CREATE TABLE IF NOT EXISTS "address"
(
    "id"                  BIGSERIAL PRIMARY KEY,
    "address_label"       varchar(20)  NOT NULL,
    "address_first_line"  varchar(50)  NOT NULL,
    "address_second_line" varchar(200),
    "city"                varchar(100),
    "state"               varchar(100) NOT NULL,
    "country"             varchar(100) NOT NULL,
    "postal_code"         varchar(20)  NOT NULL
    );

CREATE TABLE IF NOT EXISTS "buyer_information"
(
    "id"                           BIGSERIAL PRIMARY KEY,
    "buyer_code"                   varchar(255),
    "buyer_vendor_connection_uuid" varchar(255),
    "buyer_company_uuid"           varchar(100),
    "company_name"                 varchar(255),
    "tax_reg_no"                   varchar(255),
    "company_country"              varchar(255),
    "address_id"                   bigint,
    "contact_id"                   bigint,
    CONSTRAINT fk_contact_id
    FOREIGN KEY (contact_id)
    REFERENCES contact_information (id),
    CONSTRAINT fk_address_id
    FOREIGN KEY (address_id)
    REFERENCES address (id)
    );

CREATE TABLE IF NOT EXISTS "supplier_information"
(
    "id"                              BIGSERIAL PRIMARY KEY,
    "supplier_code"                   varchar(255),
    "supplier_vendor_connection_uuid" varchar(255),
    "supplier_company_uuid"           varchar(100),
    "company_name"                    varchar(255),
    "tax_reg_no"                      varchar(255),
    "company_country"                 varchar(255),
    "contact_id"                      bigint,
    CONSTRAINT fk_contact_id
    FOREIGN KEY (contact_id)
    REFERENCES contact_information (id),
    "address_id"                      bigint,
    CONSTRAINT fk_address_id
    FOREIGN KEY (address_id)
    REFERENCES address (id)
    );

CREATE TABLE IF NOT EXISTS "contract"
(
    "id"                          BIGSERIAL PRIMARY KEY,
    "contract_number"             varchar(50),
    "contract_request_number"     varchar(50),
    "contract_request_uuid"       varchar(50),
    "global_contract_number"      varchar(50),
    "contract_title"              varchar(255) NOT NULL,
    "contracting_entity"          varchar(255) NOT NULL,
    "contracting_owner"           varchar(255) NOT NULL,
    "contract_type"               varchar(255) NOT NULL,
    "is_outsourcing_contract"     boolean,
    "currency_code"               varchar(100) NOT NULL,
    "contract_value"              double precision,
    contract_start_date           TIMESTAMPTZ  NOT NULL,
    contract_end_date             TIMESTAMPTZ  NOT NULL,
    "payment_term_name"           varchar(100),
    "payment_term_uuid"           varchar(100),
    "renewal_option"              varchar(255),
    created_date                  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    "created_by_name"             varchar(255) NOT NULL,
    "created_by_uuid"             varchar(255) NOT NULL,
    "nature_of_contract"          varchar(255) NOT NULL,
    "project_name"                VARCHAR(50),
    "project_uuid"                VARCHAR(255),
    "project_rfq_no"              VARCHAR(255),
    delivery_date                 TIMESTAMPTZ,
    "delivery_address_id"         bigint,
    "total_used"                  double precision,
    "product_service_description" VARCHAR(500),
    "approval_matrix_uuid"        varchar(255),
    "approval_matrix_name"        varchar(255),
    "approval_sequence"           VARCHAR(255),
    "next_approver_group"         varchar(100),
    "next_approver_group_uuid"    VARCHAR(255),
    "contract_status"             varchar(255) NOT NULL,
    "e_sign_routing"              boolean,
    "contract_uuid"               varchar(255) NOT NULL,
    "approved_date"               TIMESTAMPTZ,
    "sub_total"                   double precision,
    "tax_total"                   double precision,
    "total_amount"                double precision,
    "issued_by"                   varchar(255),
    "issued_date"                 TIMESTAMPTZ,
    "updated_date"                TIMESTAMPTZ,
    "acknowledge_date"            TIMESTAMPTZ,
    "connected_vendor"            boolean,
    "acknowledge_offline"         boolean,
    "buyer_id"                    bigint,
    "supplier_id"                 bigint,
    CONSTRAINT fk_buyer_id
    FOREIGN KEY (buyer_id)
    REFERENCES buyer_information (id),
    CONSTRAINT fk_supplier_id
    FOREIGN KEY (supplier_id)
    REFERENCES supplier_information (id),
    CONSTRAINT fk_delivery_address_id
    FOREIGN KEY (delivery_address_id)
    REFERENCES address (id)
    );


CREATE TABLE IF NOT EXISTS "contract_item"
(
    "id"                         BIGSERIAL PRIMARY KEY,
    "item_code"                  varchar(255) NOT NULL,
    "item_name"                  varchar(255),
    "item_description"           varchar(255),
    "model"                      varchar(255),
    "size"                       varchar(255),
    "brand"                      varchar(255),
    "trade"                      varchar(255),
    "uom"                        varchar(255),
    "currency"                   varchar(255),
    "qty"                        double precision,
    "unit_price"                 double precision,
    "tax_code"                   varchar(100),
    "tax_code_uuid"              varchar(255),
    "tax_code_value"             double precision,
    "tax_amount"                 double precision,
    "in_source_currency"         double precision,
    "exchange_rate"              double precision,
    "in_document_currency"       double precision,
    "in_document_currency_after" double precision,
    "delivery_address_id"        bigint,
    "delivery_date"              TIMESTAMPTZ,
    "gl_account"                 varchar(255),
    "note"                       varchar(255),
    "manual_item"                boolean,
    "contract_id"                bigint,
    CONSTRAINT fk_contract_id
    FOREIGN KEY (contract_id)
    REFERENCES contract (id),
    CONSTRAINT fk_delivery_address_id
    FOREIGN KEY (delivery_address_id)
    REFERENCES address (id)
    );

CREATE TABLE IF NOT EXISTS "contract_audit_trail"
(
    "id"            BIGSERIAL PRIMARY KEY,
    "user_name"     varchar(255),
    "user_uuid"     varchar(255),
    "role"          varchar(255),
    "current_group" varchar(255),
    "action"        varchar(255),
    "status"        varchar(255),
    "date"          TIMESTAMPTZ DEFAULT NOW(),
    "contract_id"   bigint,
    CONSTRAINT fk_contract_id
    FOREIGN KEY (contract_id)
    REFERENCES contract (id)
    );

CREATE TABLE IF NOT EXISTS "contract_document"
(
    "id"            BIGSERIAL PRIMARY KEY,
    "guid"          VARCHAR(255),
    "title"         VARCHAR(255),
    "file_name"     VARCHAR(255),
    "description"   VARCHAR(255),
    "upload_by"     VARCHAR(255),
    "uploader_uuid" VARCHAR(255),
    "upload_on"     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    "updated_on"    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    "contract_id"   bigint,
    CONSTRAINT fk_contract_id
    FOREIGN KEY (contract_id)
    REFERENCES contract (id)

    );


