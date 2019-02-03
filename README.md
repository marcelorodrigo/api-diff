API Difference
==============

This simple API works on accepting JSON base64 encoded binary data
and providing analysis about the difference between data stored.

## Technologies

- Java 8
- [Spring Boot](http://spring.io/projects/spring-boot)
- [Swagger](https://swagger.io/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [JUnit](https://junit.org/junit4/)
- [Mockito](https://site.mockito.org/)
- [OpenClover](https://openclover.org)

## Prerequisites

- JDK 1.8
- Maven 3.5 (or newer)
- Git
- HTTP 8080 port available (to run API server)

### Compiling and Running

 1. Clone this repository:
 > git clone git@github.com:marcelorodrigo/api-diff.git
 2. Build using Maven
 > mvn clean install
 3. Execute the jar artifact generated
 > java -jar target/api-diff-1.0.0.jar
 4. You can access swagger via URL:
> http://localhost:8080/swagger-ui.html
API Service is up and running and ready to use

## API Usage

### Sending data
The following API endpoints are available to post data:

>**POST**: http://localhost:8080/v1/diff/{ID}/left
>
>**POST**: http://localhost:8080/v1/diff/{ID}/right

* You must provide ID as a pathparam and the
JSON base64 string as payload:
>{"data": "TWFyY2Vsbw=="}

The following HTTP response codes will be returned as result:
>**201 Created**: Ok
>
>**400 Bad Request**: Invalid base64 data provided

These endpoints will validate and store decoded your base64 binary data posted.

### Getting the results
Considering that you posted data for left and right endpoints, you can retrieve the comparing results on the following result endpoint:

>http://localhost:8080/rest/api/v1/diff/{ID}

* You must provide ID as a Path param

#### Results

All results provided will be considered as decoded base64 binary data.

* If LEFT and RIGHT data are equals:
>{
  "resultType": "EQUALS",
  "message": "Values are equal"
}

* If LEFT and RIGHT data differs in length:
>{
  "resultType": "DIFFERENT_LENGTH",
  "message": "Values are of different length"
}

* If LEFT and RIGHT has same size, but they are different in content you will get the differences hints:
>{
  "resultType": "SAME_LENGTH",
  "message": "{offset=1, length=1}, {offset=3, length=1}"
}

#### HTTP Errors
>**400 Bad Request**: Missing data for LEFT or RIGHT side
>
>**404 Not Found**: ID not found



## Tests
This project is covered by unit tests and API integration tests, you can
execute all these tests by running this command on project root:
> mvn test

#### Code Coverage
You can check this project code coverage and other metrics using [Clover](https://openclover.org),
by running this command on project root:
> mvn clean clover:setup test clover:aggregate clover:clover

A report will be stored at `target/site/clover/index.html`
just open that file on your browser.

![image](https://user-images.githubusercontent.com/443962/52166428-3e438980-26f4-11e9-9ae1-e7ef9c45ba2e.png)
