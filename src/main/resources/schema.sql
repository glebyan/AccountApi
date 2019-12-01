drop table if exists accounts;
drop table if exists transactions;

create table if not exists accounts(
  account_uuid varchar(36),
  total_amount DECIMAL(32,2)
);

create table if not exists transactions (
  account_uuid          varchar(36),
  operation_uuid        varchar(36),
  operation_type        VARCHAR(10),
  operation_result      VARCHAR(10),
  operation_amont       DECIMAL(32, 2),
  second_account_uuid   varchar(36)
);