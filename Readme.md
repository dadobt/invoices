### The project is build with
-   Spring Boot
-   JWT tokens for authentication and authorization
-   In-memory H2 database


## Requirements
- Java 11 ( build and tested with OpenJDK 11 )
- Maven ( build and tested with 3.5.4 )

##Build the application and run the application

Navigate to the root of the application (where `pom.xml` is) and execute


```shell script
 mvn clean install
``` 
This will all execute all unit and integration tests
or if you navigate again to the root of the project you can start the app with

```shell script
mvn spring-boot:run
```

### h2 console
H2 console available at `/h2-console`
Database available at `jdbc:h2:mem:this-will-be-generated-at-runtime`
- user:`sa`
- pass:` ` (is empty) 

### hard coded users
there is 1 hardcoded users
- username = "user";
- password = "password";


### What the application supports
For the user
- creating new invoices, which includes a scanned image of the physical invoice
- adding new suppliers  
- adding contracts for given supplier.
- Getting invoices due within X days
- Getting all contracts for a given supplier
- Getting all invoices from a specific supplier
- Getting a total sum form all invoices for a specific supplier 
- Counting the number of invoices from a specific supplier


## Usage with cURL

In order to authenticate with valid user and pass POST on /authenticate
example:

```shell script
curl --location '<localhost>:8080/authenticate' \
--header 'Content-Type: application/json' \
--data '{
  "username": "user",
  "password": "password"
}'

```

you will get back jwt token
```shell script
{
"jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzE0MzQ4MjMxLCJpYXQiOjE3MTQzNDQ2MzF9.BZcV1p0vwJBTBQZ7ln47u0mwAaUw4ftrfiXcRR3DmjM"
}
```
that you will need for the endpoints for the user actions as a Bearer token

### As a logged in user you can create a supplier 
```shell 
curl --location '<localhost>:8080/api/supplier/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <TOKEN>' \
--data-raw '{
"name":"test",
"address":"test",
"phone":"1234",
"email":"test@gmail.com"
}'
```

### As a logged in user you can create contract for a given supplier 
````shell
curl --location '<localhost>:8080/api/supplier/1/create/contract' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Token>' \
--data '  { 
    "name":"tes2222t",
     "description":"test description",
     "signed":"2024-04-24T14:28:00.131Z",
     "expires":"2024-04-28T14:28:00.131Z",
     "supplierID":"1"
  }'
````

### As a logged in user you can create a invoice including a uploading document for a given supplier
````shell
curl --location '<localhost>:8080/api/invoice/create' \
--header 'Content-Type: multipart/form-data' \
--header 'Accept: */*' \
--header 'Authorization: Bearer <Token>' \
--form 'file=@"/C:/somefaktura.jpg"' \
--form 'invoice="{
	\"billingDate\": \"2024-04-24T14:28:00.131Z\",
	\"customerAddress\": \"string\",
	\"deliveryDate\": \"2024-04-24T14:28:00.131Z\",
	\"dueDate\": \"2024-04-30T14:28:00.131Z\",
	\"specification\": \"string\",
	\"price\": 450,
	\"vatRate\": 12,
	\"contactDetails\": \"string\",
	\"vatnumber\": \"string\",
	\"bankgiroNumber\": \"12345678\",
	\"supplierId\": \"1\"
}";type=application/json
'
````


### As a logged in user you can get all contracts  for a given supplier
````shell
curl --location '<localhost>:8080/api/supplier/1/get/contract' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Token>'
````
### As a logged in user you can get all invoices  for a given supplier
````shell
curl --location '<localhost>:8080/api/invoice/supplier/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Token>'
````

### As a logged in user you can Count all invoices  for a given supplier
````shell
curl --location '<localhost>:8080/api/supplier/1/get/invoicesNumber' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Token>'
````
### As a logged in user you can get all invoices Due in X days
example with 2 days
````shell
curl --location '<localhost>:8080/api/invoice/duedays/2' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Token>'
````

### As a logged in user you can get the total Amount per supplier  
example with 2 days
````shell
curl --location '<localhost>:8080/api/invoice/supplier/1/total' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Token>'
````

Or you can import the postman collection and use the GUI :) 

### Application metrics
Application metrics are exposed on and should be scraped by OpenMetrics time series db and scraper. Example Prometheus

```shell
http://localhost:8080/actuator/metrics
```

## Known Issues

- jwt secret is hardcoded into the code
- logging should be better , monitoring should be added
- more unit and integration test should be added

 ![byebye](https://media.giphy.com/media/vFKqnCdLPNOKc/giphy.gif)

