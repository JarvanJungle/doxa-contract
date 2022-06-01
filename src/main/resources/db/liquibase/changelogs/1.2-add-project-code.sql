alter table contract
    add if not exists project_code varchar(255);

alter table contract_request
    add if not exists project_code varchar(255);