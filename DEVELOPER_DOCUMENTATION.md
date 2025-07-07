# Merchant Onboarding API - Developer Documentation

## 1. OVERVIEW

### Purpose and Primary Functionality

The Merchant Onboarding API is a Spring Boot application designed to manage the merchant registration and onboarding process. It provides a RESTful API for creating, retrieving, and managing merchant records in a PostgreSQL database while integrating with external shelf services for merchant registration workflows.

**Core Features:**
- **Merchant Registration**: Create new merchants with comprehensive business information
- **Merchant Retrieval**: Query individual merchants by ID or retrieve all merchants
- **External Integration**: Automatic registration with shelf services upon merchant creation
- **Data Validation**: Comprehensive input validation and error handling
- **Audit Trail**: Automatic timestamp tracking for registration dates

### When to Use This Component vs. Alternatives

**Use the Merchant Onboarding API when:**
- You need to onboard new merchants into your platform
- You require structured merchant data storage with validation
- You need integration with external shelf/inventory services
- You want a RESTful API with OpenAPI documentation
- You need asynchronous processing capabilities for merchant queries

**Consider alternatives when:**
- You only need simple user registration without business context
- You don't require external service integration
- You need real-time streaming of merchant data
- You need complex workflow management beyond basic CRUD operations

### Architectural Context

The Merchant Onboarding API fits within a microservices architecture as a domain service responsible for merchant lifecycle management. It serves as:

- **Upstream Service**: Provides merchant data to other services via REST API
- **Downstream Consumer**: Integrates with shelf services for merchant registration
- **Data Owner**: Maintains authoritative merchant records in PostgreSQL
- **API Gateway Compatible**: Designed to work behind API gateways and load balancers

## 2. TECHNICAL SPECIFICATION

### Architecture Overview

The application follows **Clean Architecture** principles with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │  REST Controllers│  │      DTOs       │                   │
│  └─────────────────┘  └─────────────────┘                   │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer                         │
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │    Services     │  │   Interfaces    │                   │
│  └─────────────────┘  └─────────────────┘                   │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                           │
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │    Entities     │  │  Repositories   │                   │
│  └─────────────────┘  └─────────────────┘                   │
├─────────────────────────────────────────────────────────────┤
│                   Infrastructure Layer                       │
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │  JPA Repositories│  │  External APIs  │                   │
│  └─────────────────┘  └─────────────────┘                   │
└─────────────────────────────────────────────────────────────┘
```

### API Reference

#### Core Endpoints

**Base URL:** `http://localhost:8080/api`

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/merchants` | Create new merchant | `NewMerchantRequest` | 201 Created |
| GET | `/merchants` | Get all merchants | None | `List<Merchant>` |
| GET | `/merchants/{id}` | Get merchant by ID | None | `Merchant` |

#### Request/Response Models

**NewMerchantRequest**
```java
{
  "name": "string",           // Required: Merchant name
  "email": "string",          // Optional: Email address  
  "validUntil": "datetime",   // Required: Validity end date
  "startRelationship": "datetime", // Required: Relationship start date
  "document": {               // Required: Business document
    "type": "string",         // Document type (e.g., "CPF", "CNPJ")
    "value": "string"         // Document number
  },
  "address": {                // Required: Business address
    "street": "string",       // Street name
    "number": "string",       // Street number
    "complement": "string",   // Optional: Address complement
    "neighborhood": "string", // Neighborhood
    "city": "string"         // City
  }
}
```

**Merchant Response**
```java
{
  "id": "uuid",               // Auto-generated merchant ID
  "name": "string",           // Merchant name
  "email": "string",          // Email address
  "validUntil": "datetime",   // Validity end date
  "startRelationship": "datetime", // Relationship start date
  "document": {               // Business document
    "type": "string",
    "value": "string"
  },
  "registrationDate": "datetime", // Auto-generated registration timestamp
  "address": {                // Business address
    "street": "string",
    "number": "string", 
    "complement": "string",
    "neighborhood": "string",
    "city": "string"
  }
}
```

### Service Layer API

#### MerchantService Interface
```java
public interface MerchantService {
    void save(Merchant merchant);
    CompletableFuture<List<Merchant>> findAllMerchants();
    CompletableFuture<Merchant> findMerchantById(UUID id);
}
```

### State Management

**Entity Lifecycle:**
1. **Creation**: Merchant created with auto-generated UUID and registration timestamp
2. **Persistence**: Saved to PostgreSQL database via JPA
3. **External Registration**: Automatically registered with shelf service
4. **Retrieval**: Available via REST API queries

**Transaction Management:**
- Database operations wrapped in Spring transactions
- External service calls handled separately to avoid distributed transaction complexity
- Rollback strategies implemented for critical failures

### Events and Integration

**External Service Events:**
- **Merchant Registration**: Triggered on merchant creation
- **Shelf Service Integration**: HTTP POST to configured shelf service endpoint
- **Audit Events**: Logged for all operations

**Error Handling:**
- Validation errors return HTTP 400 with details
- Not found errors return HTTP 404
- System errors return HTTP 500 with generic message
- Detailed error logging for debugging

## 3. IMPLEMENTATION EXAMPLES

### Basic Usage Example

#### Creating a New Merchant
```bash
curl -X POST http://localhost:8080/api/merchants \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tech Solutions Inc",
    "email": "contact@techsolutions.com",
    "validUntil": "2024-12-31T23:59:59",
    "startRelationship": "2024-01-15T09:00:00",
    "document": {
      "type": "CNPJ",
      "value": "12345678000195"
    },
    "address": {
      "street": "Innovation Street",
      "number": "123",
      "complement": "Suite 456",
      "neighborhood": "Tech District",
      "city": "San Francisco"
    }
  }'
```

#### Retrieving All Merchants
```bash
curl -X GET http://localhost:8080/api/merchants \
  -H "Accept: application/json"
```

#### Retrieving Specific Merchant
```bash
curl -X GET http://localhost:8080/api/merchants/550e8400-e29b-41d4-a716-446655440000 \
  -H "Accept: application/json"
```

### Advanced Configuration Example

#### Custom Application Configuration
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/onboarding
    username: ${DB_USERNAME:onboarding}
    password: ${DB_PASSWORD:onboarding}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: ${DDL_AUTO:update}
    show-sql: ${SHOW_SQL:true}
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

app:
  shelf-service:
    url: ${SHELF_SERVICE_URL:http://localhost:8081/api/shelf}
    timeout: ${SHELF_SERVICE_TIMEOUT:30s}
    retry-attempts: ${SHELF_SERVICE_RETRY:3}

logging:
  level:
    com.example.merchantonboarding: ${LOG_LEVEL:DEBUG}
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

### Customization Scenarios

#### Custom Validation Rules
```java
@Component
public class MerchantValidator {
    
    public void validateBusinessRules(Merchant merchant) {
        // Custom validation logic
        if (merchant.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Valid until date must be in the future");
        }
        
        if (merchant.getStartRelationship().isAfter(merchant.getValidUntil())) {
            throw new IllegalArgumentException("Start relationship cannot be after valid until date");
        }
    }
}
```

#### Custom Error Handling
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
```

### Common Patterns and Best Practices

#### Asynchronous Processing Pattern
```java
@Service
public class MerchantServiceImpl implements MerchantService {
    
    @Async
    @Override
    public CompletableFuture<List<Merchant>> findAllMerchants() {
        List<Merchant> merchants = merchantRepository.findAll();
        return CompletableFuture.completedFuture(merchants);
    }
}
```

#### Repository Pattern with Interface Segregation
```java
// Domain layer - business interface
public interface MerchantRepository {
    void save(Merchant merchant);
    Merchant findById(UUID id);
    List<Merchant> findAll();
}

// Infrastructure layer - implementation
@Repository
public class PostgresMerchantRepository implements MerchantRepository {
    // JPA implementation
}
```

## 4. TROUBLESHOOTING

### Common Errors and Solutions

#### Error: "Connection refused to PostgreSQL"
**Cause:** Database not running or incorrect connection parameters
**Solution:**
```bash
# Check database status
sudo systemctl status postgresql

# Verify connection parameters
psql -h localhost -p 5433 -U onboarding -d onboarding

# Update application.yml with correct values
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/onboarding
    username: your_username
    password: your_password
```

#### Error: "Shelf service unavailable"
**Cause:** External shelf service not accessible
**Solution:**
```bash
# Check service availability
curl -I http://localhost:8081/api/shelf

# Configure timeout and retry settings
app:
  shelf-service:
    url: http://localhost:8081/api/shelf
    timeout: 30s
    retry-attempts: 3
```

#### Error: "Validation failed for merchant data"
**Cause:** Required fields missing or invalid data format
**Solution:**
```java
// Ensure all required fields are provided
{
  "name": "Required - cannot be null or empty",
  "validUntil": "Required - must be valid ISO 8601 datetime",
  "startRelationship": "Required - must be valid ISO 8601 datetime",
  "document": {
    "type": "Required - document type",
    "value": "Required - document number"
  },
  "address": {
    "street": "Required",
    "number": "Required", 
    "neighborhood": "Required",
    "city": "Required"
  }
}
```

### Debugging Strategies

#### Enable Debug Logging
```yaml
logging:
  level:
    com.example.merchantonboarding: DEBUG
    org.springframework.web: DEBUG
    org.hibernate: DEBUG
```

#### Database Query Analysis
```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
```

#### HTTP Request/Response Tracing
```java
@RestController
public class MerchantController {
    
    @PostMapping
    public ResponseEntity<Void> createMerchant(@Valid @RequestBody NewMerchantRequest request) {
        logger.info("Incoming request: {}", request);
        // Process request
        logger.info("Request processed successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
```

### Performance Considerations

#### Database Optimization
- **Indexing**: Ensure proper indexes on frequently queried columns
- **Connection Pooling**: Configure HikariCP for optimal connection management
- **Query Optimization**: Use JPA query hints for complex queries

#### External Service Optimization
- **Timeout Configuration**: Set appropriate timeouts for external calls
- **Circuit Breaker**: Implement circuit breaker pattern for resilience
- **Caching**: Cache external service responses where appropriate

#### Memory Management
- **JVM Tuning**: Configure heap size based on expected load
- **Connection Limits**: Set appropriate database connection limits
- **Resource Cleanup**: Ensure proper resource cleanup in exception scenarios

## 5. RELATED COMPONENTS

### Dependencies

#### Core Dependencies
- **Spring Boot 3.4.4**: Framework foundation
- **Spring Data JPA**: Database abstraction layer
- **PostgreSQL Driver**: Database connectivity
- **Spring WebFlux**: HTTP client for external services
- **Spring Validation**: Input validation framework

#### Development Dependencies
- **SpringDoc OpenAPI**: API documentation generation
- **Spring Boot Actuator**: Health checks and metrics
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework for tests
- **Testcontainers**: Integration testing with real databases

### Components Commonly Used Alongside

#### API Gateway
```yaml
# Example Kong or Spring Cloud Gateway configuration
routes:
  - id: merchant-onboarding
    uri: http://merchant-onboarding:8080
    predicates:
      - Path=/api/merchants/**
    filters:
      - name: RateLimiter
        args:
          rate-limit: 100
          time-window: 60s
```

#### Monitoring and Observability
```yaml
# Prometheus monitoring
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

#### Load Balancer Configuration
```nginx
# Nginx load balancer
upstream merchant-onboarding {
    server merchant-onboarding-1:8080;
    server merchant-onboarding-2:8080;
    server merchant-onboarding-3:8080;
}

server {
    location /api/merchants {
        proxy_pass http://merchant-onboarding;
    }
}
```

### Alternative Approaches

#### Event-Driven Architecture
For high-volume scenarios, consider:
- **Apache Kafka**: For event streaming
- **RabbitMQ**: For message queuing
- **Event Sourcing**: For audit trail requirements

#### Microservices Decomposition
For larger systems, consider splitting into:
- **Merchant Service**: Core merchant CRUD operations
- **Onboarding Service**: Workflow management
- **Notification Service**: Communication handling
- **Audit Service**: Tracking and compliance

#### Database Alternatives
- **MongoDB**: For document-based storage
- **Redis**: For caching and session management
- **Elasticsearch**: For advanced search capabilities

### Integration Patterns

#### API Composition
```java
@Service
public class MerchantAggregationService {
    
    public CompletableFuture<EnrichedMerchant> getEnrichedMerchant(UUID id) {
        CompletableFuture<Merchant> merchant = merchantService.findById(id);
        CompletableFuture<ShelfInfo> shelfInfo = shelfService.getShelfInfo(id);
        
        return merchant.thenCombine(shelfInfo, EnrichedMerchant::new);
    }
}
```

#### Event Publishing
```java
@EventListener
public class MerchantEventHandler {
    
    @Async
    public void handleMerchantCreated(MerchantCreatedEvent event) {
        // Publish to external event bus
        eventPublisher.publish(event);
    }
}
```

---

## Getting Started

1. **Prerequisites**: Java 17, PostgreSQL, Maven 3.6+
2. **Setup Database**: Create `onboarding` database
3. **Configure Application**: Update `application.yml`
4. **Run Application**: `mvn spring-boot:run`
5. **Access API**: `http://localhost:8080/swagger-ui/index.html`

## Additional Resources

- **OpenAPI Documentation**: `/swagger-ui/index.html`
- **Health Check**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Source Code**: Clean Architecture with comprehensive test coverage
- **Docker Support**: Dockerfile included for containerization