alter table contract_request_item alter column note type varchar(3000) using note::varchar(3000);
alter table contract_request alter column product_service_description type varchar(3000) using product_service_description::varchar(3000);


alter table contract_item alter column note type varchar(3000) using note::varchar(3000);
alter table contract alter column product_service_description type varchar(3000) using product_service_description::varchar(3000);


