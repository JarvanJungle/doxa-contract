alter table contract_request_item alter column item_description type varchar(500) using item_description::varchar(500);

alter table contract_item alter column item_description type varchar(500) using item_description::varchar(500);