# Product price API

This is a Spring Boot project created for an interview challenge.

It provides an API to retrieve the price of a product valid on a given date, filtered by product ID and brand ID.

The project follows **hexagonal architecture** to ensure separation of concerns and flexibility for future enhancements.
It uses an SQL in-memory database (H2), as this was a requirement of the challenge.

> **ℹ️ IMPORTANT:**  
>  Requirements
> - Java 21
> - Maven (or use the included Maven wrapper)


## ▶️Quick start

To run the project locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/fabriciodigennaro/product-price-service.git
   cd product-service
   ```

2. **Build the project using Maven**:
   ```bash
   ./mvnw clean package
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```  
   The application will start at http://localhost:8080.

## 🐳Running with Docker
### To build and run the application using Docker, follow these steps:

Build the application JAR without running tests:
   ```bash
      ./mvnw clean package -DskipTests
   ```
Build the Docker image:
   ```bash
     docker build --no-cache -t productservice .
   ```

Run the container:
   ```bash
     docker run -p 8080:8080 productservice
   ```

Also, the application will start at http://localhost:8080.

## 📝Swagger API documentation
The project includes Swagger for API documentation and manual testing. After starting the application, you can view the
Swagger UI at:

http://localhost:8080/swagger-ui/index.html

## 🧪Running tests
To execute the tests, run:

```bash
  ./mvnw test
```


## Test types

### Unit tests
These tests focus on verifying the correctness of individual components, primarily testing the use case in this project.
They ensure the core business logic functions as expected, isolated from external dependencies like databases or
external services.

### Integration tests
Tests the interaction with external services, such as the database.

### Contract tests
Verifies that the API complies with the expected contract, ensuring consistent integration with other systems.

### Component tests
Triggers an end-to-end flow to test the product price retrieval functionality.

### ⚠️ *IMPORTANT NOTE*
I have included a parameterized test within the component tests to cover the test cases requested in the challenge
description.
This decision was made because the challenge description implied that these test cases should be included as part of
the component (E2E) tests.

However, these scenarios have also been covered by other types of tests. In a typical production project, I would aim
to avoid having component tests for scenarios that could be covered by more cost-effective tests.

## Design decisions

### Hexagonal architecture
The project is designed following the principles of hexagonal architecture. The domain and application logic are
isolated from infrastructure concerns, making the system flexible for future changes.

### No Spring annotations in domain/application layers
Framework-specific annotations (like Spring’s) are avoided in the domain and application layers, as the framework is
considered part of the infrastructure.
This keeps the business logic decoupled from the framework.

### Controlled bean configuration
Instead of using Spring stereotype annotations (@Component, @Service, etc.), all beans (except controllers) are
configured in the config package.
This gives more control over bean lifecycle and allows for easier changes in the future.

### Value classes for Product ID and Brand ID
The use of value classes (classes that serve the purpose of wrapping values) was applied in the case of product ID and
brand ID, as these have the same type (long) and were used as parameters in the use case.
The value classes ProductId and BrandId were introduced to avoid a bug caused by accidentally switching the parameters.
All other properties were implemented without value classes to avoid adding unnecessary complexity in this challenge.

### Sealed class for use case response
The use case returns a sealed class, providing explicit control over success and failure scenarios.
Currently, only one failure case is handled (product price not found), but this design allows easy expansion for additional
failure scenarios in the future.
This approach avoids using exceptions for expected outcomes and provides more semantic meaning than using Optional or null.

