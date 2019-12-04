## AccountApi

The simple service that provides a REST API for basic operation with a bank account. It does not use Spring, so the development was a little bit challengeable.

```Programmer in a suit of Spring framework. Take that off, what are you?``` 

### Endpoints

`/create` POST (no param), return UUID, 201 Created
Create new bank account and return the UUID.

`/deposit` POST json (account uuid, value for deposit), return 202 Accepted, or 404 Not found
Deposit the bank account specified by UUID with given value. If operation successful return 202. If account not found return 404. 

`/total` POST json (UUID), return BigDecimal, 200 OK, or 404 Not found
Return the total amount of the bank account specified by UUID. Return 200 and amount in application/json body of response. Return 404 if account not exists.

`/transfer` POST json (UUID from, UUID to, amount to transfer), return  202 Accepted, or 404 Nof found, 403 FORBIDDEN
The main endpoint. Receive two account ID and amount of money for transfer. If one of account is not exist returns 404. 
If operation success returns 202. If in account "from" the total amount of money lest then amount of transaction returns 403.

### Potential areas for improvement

To be the pretty good small project this one needs to:

* Handling invalid requests (check input request, throw errors and return error code to client)
* Add connection pool
* Using dependency injection framework to bundle controllers and services (google guice for example)  
* Use more suitable datatype for money handling (joda money for example or hand-made wrapper to BigDecimal)
* Rid off boilerplate code in services for make code more readable (move SQL injection to special DTO class)
* Use some library for concurrency testing instead of manual starting threads  

But I have no more time to improve it right now. 

### Backlog (additional features to implement in future)

* History service of account operation 
* Revert service for particular operation using HistoryId
* Withdraw service (for ATM)

### Testing 

The project include unit-tests for services (servicesTest) and also the integration tests (AppTest). The integration test 
described in details in further part of this file.

This also include soap-ui project if you wish (AccountApi-soapui-project.xml).

#### Integratin tests

The integration test invoke endpoint to perform operation and check the changes in DB. It may be time-consumed depending of 
speed or your PC so you may adjust the number off accounts that uses for test by changing the `numberOfAccounts` field.

At first, it create 10 accounts in DB and check the quantity of created accounts.

Then it fill this 10 accounts by default amount value (10_000). 

After that it move 1000 using 10 threads from first account to second and further to last account.
As result it must be zero amount in first account, 20_00 in last account and 10_000 in all other accounts.
This test display the behavior of the system in concurrency mode. 

All test depends each other because it affected DB. In my humble opinion its acceptable for integration tests.  

I don't implement negative test for endpoints because of lack of time. But negative test is implemented in unit tests for services, so I believe it works fine ) 
 




