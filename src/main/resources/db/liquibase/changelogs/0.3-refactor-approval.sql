-- remove existing fields
alter table contract_request drop column approval_matrix_uuid;

alter table contract_request drop column approval_matrix_name;

alter table contract_request drop column approval_sequence;

alter table contract_request drop column approval_sequence_groups_uuid;

alter table contract_request drop column next_approver_group;

alter table contract_request drop column next_approver_group_uuid;

-- Add new fields
alter table contract_request
    add if not exists approval_route_sequence varchar(255);

alter table contract_request
    add if not exists approval_route_name varchar(100);

alter table contract_request
    add if not exists approval_route_uuid varchar(100);

alter table contract_request
    add if not exists next_approver varchar(255);

alter table contract_request
    add if not exists next_approval_group varchar(255);

alter table contract_request_audit_trail
    add if not exists approval_group varchar(255);


-- remove existing fields
alter table contract drop column approval_matrix_uuid;

alter table contract drop column approval_matrix_name;

alter table contract drop column approval_sequence;

alter table contract drop column next_approver_group;

alter table contract drop column next_approver_group_uuid;

-- Add new fields
alter table contract
    add if not exists approval_route_sequence varchar(255);

alter table contract
    add if not exists approval_route_name varchar(100);

alter table contract
    add if not exists approval_route_uuid varchar(100);

alter table contract
    add if not exists next_approver varchar(255);

alter table contract
    add if not exists next_approval_group varchar(255);

alter table contract_audit_trail
    add if not exists approval_group_uuid varchar(255);