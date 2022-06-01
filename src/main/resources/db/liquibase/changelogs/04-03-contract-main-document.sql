alter table contract_document
    add if not exists is_ct_document boolean default false;