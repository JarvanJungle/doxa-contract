alter table contract
    add if not exists note varchar(3000);

alter table contract_request
    add if not exists note varchar(3000);