alter table contract_request_document_metadata
    add if not exists external_document boolean;

alter table contract_document
    add if not exists external_document boolean;