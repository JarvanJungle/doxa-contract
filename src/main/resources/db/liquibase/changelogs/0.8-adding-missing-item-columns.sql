alter table contract_item
    add if not exists gl_account_uuid varchar(255);
alter table contract_request_item
    add if not exists tax_code_uuid varchar(255);