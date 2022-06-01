alter table contract_request drop column project_delivery_address;
alter table contract_request drop column supplier_name;
alter table contract_request drop column supplier_uuid;

alter table contract_request
    add if not exists delivery_address_id bigint;

alter table contract_request
    add if not exists supplier_id bigint;

ALTER TABLE contract_request
    ADD CONSTRAINT fk_cr_delivery_address_id FOREIGN KEY (delivery_address_id) REFERENCES address (id);

ALTER TABLE contract_request
    ADD CONSTRAINT fk_cr_supplier_id FOREIGN KEY (supplier_id) REFERENCES supplier_information (id);
