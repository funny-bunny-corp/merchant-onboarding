# Merchant Onboarding API

This is a Java Spring Boot application for managing merchant onboarding processes, converted from the original C# ASP.NET Core application while maintaining the same architecture and API contracts.

## Architecture

The application follows Clean Architecture principles with the following layers:

- **Domain Layer**: Core business logic and entities
- **Application Layer**: Service interfaces and implementations
- **Infrastructure Layer**: Database repositories and external service adapters
- **Presentation Layer**: REST controllers and DTOs

## Technology Stack

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring WebFlux** (for HTTP client)
- **SpringDoc OpenAPI 3** (for API documentation)
- **Maven** (build tool)
- **JUnit 5** (testing framework)
- **Mockito** (mocking framework)
- **Testcontainers** (integration testing)

## Prerequisites

- Java 17 or later
- PostgreSQL database
- Maven 3.6 or later

## Getting Started

### Database Setup

1. Install PostgreSQL
2. Create a database named `onboarding`
3. Create a user with credentials:
   - Username: `onboarding`
   - Password: `onboarding`
   - Host: `localhost`
   - Port: `5433`

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Install dependencies:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Running with Docker

1. Build the Docker image:
   ```bash
   docker build -t merchant-onboarding .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 merchant-onboarding
   ```

## API Endpoints

### Create Merchant
- **POST** `/api/merchants`
- Creates a new merchant with the provided information
- **Request Body**:
  ```json
  {
    "name": "Merchant Name",
    "email": "merchant@example.com",
    "validUntil": "2024-12-31T23:59:59",
    "startRelationship": "2024-02-01T00:00:00",
    "document": {
      "type": "CPF",
      "value": "12345678901"
    },
    "address": {
      "street": "Main Street",
      "number": "123",
      "complement": "Apt 1",
      "neighborhood": "Downtown",
      "city": "New York"
    }
  }
  ```

### Get All Merchants
- **GET** `/api/merchants`
- Retrieves a list of all merchants

### Get Merchant by ID
- **GET** `/api/merchants/{id}`
- Retrieves a specific merchant by their ID

## API Documentation

The application includes comprehensive OpenAPI documentation available at:
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: Available in the project root as `openapi.yaml`

## Configuration

### Application Properties

The application can be configured using the following properties:

```yaml
# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/onboarding
    username: onboarding
    password: onboarding
    driver-class-name: org.postgresql.Driver

# External Service Configuration
app:
  shelf-service:
    url: http://localhost:8081/api/shelf
```

### Environment Variables

- `SHELF_SERVICE_URL`: URL for the external shelf service (default: `http://localhost:8081/api/shelf`)

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report
```

### Test Coverage

The project includes comprehensive test coverage:
- **Unit Tests**: Domain entities, services, repositories, and controllers
- **Integration Tests**: Full application workflow testing
- **Test Coverage**: Available in `target/site/jacoco/index.html` after running tests

### Test Profiles

- `test`: Uses H2 in-memory database for testing
- `dev`: Development profile with debug logging

## Health Checks

The application includes Spring Boot Actuator endpoints:
- **Health**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`
- **Metrics**: `http://localhost:8080/actuator/metrics`

## External Dependencies

The application integrates with an external shelf service for merchant registration. Configure the service URL using the `app.shelf-service.url` property.

## Development

### Code Style

The project follows Java coding conventions and Spring Boot best practices:
- Clean Architecture principles
- Dependency injection
- Proper error handling
- Comprehensive logging
- Input validation

### Building

```bash
# Compile the application
mvn compile

# Package the application
mvn package

# Clean and rebuild
mvn clean install
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Migration from C# to Java

This application was converted from a C# ASP.NET Core application to Java Spring Boot while maintaining:
- Same API contracts and endpoints
- Same architecture and design patterns
- Same business logic and domain models
- Same external service integrations
- Enhanced with comprehensive testing and documentation

The conversion includes modern Java/Spring Boot practices and maintains compatibility with the original API specification.