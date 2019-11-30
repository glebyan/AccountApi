create table accounts(
  account_uuid UUID IDENTITY PRIMARY KEY ,
  total_amount DECIMAL(32,2)
);

  create table transactions (
  account_uuid          UUID,
  operation_uuid        UUID IDENTITY primary key,
  operation_type        VARCHAR(10),
  operation_result      VARCHAR(10),
  operation_amont       DECIMAL(32, 2),
  second_account_uuid   UUID
);