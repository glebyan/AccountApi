## AccountApi

The simple service that provide REST API fo basic operation with bank accounts. 

#### Feature list

* Parallel request processing 
* Tread-safe
* 

### Potential areas for improvement

To be the pretty good small project this one need to:

* Handling invalid requests (check input request, throw errors and return error code to client)
* Add connection pool
* Using dependency injection framework (google guice for example)  
* Using lombok
* Use more suitable datatype for money handling (joda money for example)
* Move SQL injection to special DTO class
* Rid off boilerplate code in services for make code more readable
* Implement additional methods to interface (withdraw operation for ATM service and so on)

