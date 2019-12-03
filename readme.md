## AccountApi

The simple service that provides a REST API for basic operation with a bank account. It does not use Spring, so the development was a little bit challengeable.

```Programmer in a suit of Spring framework. Take that off, what are you?``` 

### Endpoints



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

At first, it create 100 accounts in DB and check the quantity of created accounts.

Then it fill this 100 accounts by default amount value (10_000). 

After that it move 1000 using 10 threads from first account to second and further to last account.
As result it must be zero amount in first account, 1_010_000 in last account and 10_000 in all other accounts.
This test display the behavior of the system in concurrency mode. 

All test depends each other couse it affected DB. In my humble opinion its acceptable for integration tests.  

I don't implement negative test for endpoints because of lack of time. But negative test is implemented in unit tests for services, so I believe it works fine ) 
 




