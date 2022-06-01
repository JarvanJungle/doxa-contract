alter table contract
    add if not exists rfq_uuid varchar(255);
alter table contract
    add if not exists rfq_number varchar(255);

alter table contract
    add if not exists procurement_type varchar(255);

alter table contract_request
    add if not exists procurement_type varchar(255);