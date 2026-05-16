# DevShop - White-Label Flash Sale Microservices Platform

## 📋 Project Overview

A production-ready multi-phase microservices architecture for a white-label flash sale e-commerce platform.

**Tech Stack:**
- **Framework:** Spring Boot 3.3.0
- **Database:** PostgreSQL (Neo Cloud)
- **Service Communication:** OpenFeign + Resilience4j Circuit Breaker
- **API Gateway:** Spring Cloud Gateway
- **Containerization:** Docker & Docker Compose
- **CI/CD:** GitHub Actions
- **Observability:** Spring Cloud Sleuth + Micrometer
- **Java:** 17 (LTS)

## 🏗️ Architecture Overview

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ▼
┌──────────────────────┐
│   API Gateway       │ (Port 8080)
│   - Routing         │
│   - JWT Auth        │
│   - Rate Limiting   │
└────────┬────────────┘
         │
    ┌────┴──────────────┬──────────────┐
    │                   │              │
    ▼                   ▼              ▼
┌─────────────┐  ┌──────────────┐   ┌─────────────┐
│ Inventory   │  │ Order        │   │ (Future)    │
│ Service     │  │ Service      │   │ Payment Svc │
│ Port: 8081  │  │ Port: 8082   │   │ Port: 8083  │
└──────┬──────┘  └──────┬───────┘   └─────────────┘
       │                │
       └────────┬───────┘
                │
                ▼
         ┌────────────────┐
         │  PostgreSQL    │
         │  Neo Cloud     │
         └────────────────┘
```

## 📁 Project Structure

```
dev-shop/
├── pom.xml                          # Parent Maven POM
├── docker-compose.yml               # Local dev environment
├── .gitignore                       # Git ignore rules
├── .github/workflows/               # CI/CD Pipelines
│   ├── dev.yml                      # Dev branch pipeline
│   └── test.yml                     # Test branch pipeline
│
├── devshop-common/                  # Shared library
│   ├── pom.xml
│   └── src/main/java/com/devshop/common/
│       ├── model/
│       │   └── TenantEntity.java    # Base entity with multi-tenancy
│       ├── dto/
│       │   └── ApiResponse.java     # Standard API response
│       └── util/
│           └── TenantContext.java   # ThreadLocal tenant context
│
├── inventory-service/               # Product Inventory (Phase 1-3)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/devshop/inventory/
│       ├── InventoryServiceApplication.java
│       ├── model/
│       │   └── Product.java         # @Version for optimistic locking
│       ├── repository/
│       │   └── ProductRepository.java
│       ├── service/
│       │   └── ProductService.java  # Stock deduction logic
│       └── controller/
│           └── ProductController.java  # REST endpoints
│
├── order-service/                   # Order Processing (Phase 2-3)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/devshop/order/
│       ├── OrderServiceApplication.java
│       ├── model/
│       │   └── Order.java           # Multi-tenant orders
│       ├── repository/
│       │   └── OrderRepository.java
│       ├── client/
│       │   └── InventoryServiceClient.java  # OpenFeign + Circuit Breaker
│       ├── service/
│       │   └── OrderService.java    # Order creation & fallback
│       └── controller/
│           └── OrderController.java
│
├── api-gateway/                     # API Gateway (Phase 6)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/devshop/gateway/
│       └── ApiGatewayApplication.java
│
└── scripts/
    └── init-db.sql                  # Database initialization
```

## 🔑 Key Features

### Phase 1: Domain & Database Design
- ✅ Separate databases for each service
- ✅ TenantEntity base class for white-labeling (store_id)
- ✅ Version column for optimistic locking

### Phase 2: Core Service Implementation
- ✅ @Version annotation for race condition prevention
- ✅ REST controllers with standard API responses
- ✅ Service-layer business logic

### Phase 3: Inter-Service Communication
- ✅ OpenFeign client for service-to-service calls
- ✅ Resilience4j Circuit Breaker pattern
- ✅ Graceful fallback handling

### Phase 4: White Label Logic
- ✅ X-Store-Id header extraction
- ✅ TenantContext ThreadLocal storage
- ✅ Automatic filtering by store_id

### Phase 5: Containerization
- ✅ Multi-stage Dockerfile for each service
- ✅ Docker Compose for local development
- ✅ PostgreSQL service container

### Phase 6: API Gateway & Security (In Progress)
- 🔄 Spring Cloud Gateway with routing
- 🔄 JWT token validation
- 🔄 Rate limiting

### Phase 7: Observability
- 🔄 Spring Cloud Sleuth for distributed tracing
- 🔄 Micrometer metrics
- 🔄 Actuator health checks

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL (or use Neo Cloud)

### 1. Clone Repository
```bash
git clone <your-repo-url>
cd dev-shop
```

### 2. Database Configuration

#### Option A: Neo Cloud (Recommended for Production)
Your connection string is already configured in `application.yml` files:
```
postgresql://neondb_owner:npg_D6JoRyB0vikc@ep-fancy-cake-aoo5cpag.c-2.ap-southeast-1.aws.neon.tech/neondb?sslmode=require
```

#### Option B: Local PostgreSQL with Docker
```bash
docker-compose up -d postgres
```

### 3. Build the Project
```bash
# Build parent and all modules
mvn clean package -DskipTests

# Build specific service
mvn clean package -pl inventory-service -DskipTests
```

### 4. Run Services Locally

#### Option A: Using Docker Compose (Recommended)
```bash
docker-compose up
```

Endpoints:
- API Gateway: http://localhost:8080
- Inventory Service: http://localhost:8081
- Order Service: http://localhost:8082

#### Option B: Run Services Individually
```bash
# Terminal 1: Inventory Service
cd inventory-service
mvn spring-boot:run

# Terminal 2: Order Service
cd order-service
mvn spring-boot:run

# Terminal 3: API Gateway
cd api-gateway
mvn spring-boot:run
```

## 📡 API Endpoints

### Inventory Service

```bash
# Get all products for a store
curl -X GET http://localhost:8080/api/v1/inventory/products \
  -H "X-Store-Id: 1"

# Create product
curl -X POST http://localhost:8080/api/v1/inventory/products \
  -H "X-Store-Id: 1" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "FLASH-001",
    "name": "Flash Sale Tee",
    "price": 19.99,
    "stockCount": 100
  }'

# Deduct stock (during order processing)
curl -X POST http://localhost:8080/api/v1/inventory/products/1/deduct \
  -H "X-Store-Id: 1" \
  -d "quantity=5"
```

### Order Service

```bash
# Create order
curl -X POST http://localhost:8080/api/v1/orders \
  -H "X-Store-Id: 1" \
  -H "Content-Type: application/json" \
  -d '{
    "buyerId": 123,
    "productId": 1,
    "quantity": 2,
    "totalPrice": 39.98
  }'

# Get buyer's orders
curl -X GET http://localhost:8080/api/v1/orders/buyer/123 \
  -H "X-Store-Id: 1"
```

## 🔀 Git Workflow (dev → test → main)

### Branch Strategy
- **main:** Production code
- **test:** Staging/QA code
- **dev:** Development code

### Creating Dev & Test Branches

```bash
# Create dev branch from main
git checkout -b dev
git push -u origin dev

# Create test branch from main
git checkout main
git checkout -b test
git push -u origin test
```

### Workflow Example

**Development:**
```bash
# Make changes in dev
git checkout dev
# ... make changes ...
git add .
git commit -m "feat: add new feature"
git push

# GitHub Actions runs dev.yml pipeline
```

**Testing:**
```bash
# Create PR from dev to test
# After approval, merge to test
git checkout test
git pull origin dev
git push

# GitHub Actions runs test.yml pipeline
# Docker images are built and tagged with :test
```

**Production:**
```bash
# Create PR from test to main
# After approval, merge to main
git checkout main
git pull origin test
git push

# GitHub Actions runs main.yml pipeline (not yet created)
# Docker images pushed to ACR
# Deploy to Azure AKS
```

## 🐛 Optimistic Locking (Race Condition Prevention)

The `Product` entity uses JPA's `@Version` annotation:

```java
@Version
@Column(nullable = false)
private Integer version;
```

**How it works:**
1. Two customers try to buy the last item simultaneously
2. Product has version = 5
3. Customer A decrements stock, version becomes 6
4. Customer B tries to update but version is now 6
5. Hibernate throws `ObjectOptimisticLockingFailureException`
6. Customer B gets a "Please retry" message

## 🔌 Circuit Breaker Pattern

Order Service uses Resilience4j to handle Inventory Service failures:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      inventoryService:
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10000
```

**States:**
- **CLOSED:** Normal operation (requests pass through)
- **OPEN:** Service failing (requests rejected immediately)
- **HALF_OPEN:** Testing recovery (limited requests allowed)

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Load Testing (TODO)
```bash
# TODO: Add JMeter or Gatling tests
```

## 📊 Observability

### Health Check
```bash
curl http://localhost:8081/actuator/health
```

### Metrics
```bash
curl http://localhost:8081/actuator/metrics
```

### Distributed Tracing
All requests include a `traceId` in logs:
```
2024-05-16 10:30:45 [main] INFO ... - trace-id-12345 - Order created successfully
```

## 🔒 Multi-Tenancy (White-Label)

Every request must include the `X-Store-Id` header:

```bash
curl -X GET http://localhost:8080/api/v1/inventory/products \
  -H "X-Store-Id: 1"  # Boutique A
```

The system automatically filters all queries by store_id, preventing data leakage between tenants.

## 📦 Neo Cloud Database

Your credentials:
- **Host:** ep-fancy-cake-aoo5cpag.c-2.ap-southeast-1.aws.neon.tech
- **Database:** neondb
- **User:** neondb_owner
- **Region:** ap-southeast-1 (Singapore)

**Connection String:**
```
postgresql://neondb_owner:npg_D6JoRyB0vikc@ep-fancy-cake-aoo5cpag.c-2.ap-southeast-1.aws.neon.tech/neondb?sslmode=require
```

## 🚢 Deployment Roadmap

| Phase | Component | Status |
|-------|-----------|--------|
| 1-5 | Core Microservices + Docker | ✅ Done |
| 6 | API Gateway + Security | 🔄 In Progress |
| 7 | Observability | ⏳ TODO |
| 8 | Cloud Infrastructure (AKS) | ⏳ TODO |
| 9 | CI/CD Pipeline | ⏳ TODO |
| 10 | Production Launch | ⏳ TODO |

## 📚 Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Resilience4j Documentation](https://resilience4j.readme.io/)
- [OpenFeign Documentation](https://github.com/OpenFeign/feign)
- [Neo Cloud Documentation](https://neon.tech/docs)

## 🤝 Contributing

1. Create a feature branch from `dev`
2. Make changes and test locally
3. Push to feature branch
4. Create PR to `dev`
5. After approval, PR to `test`
6. After staging tests pass, PR to `main`

## 📝 License

[Add your license]

---

**Last Updated:** May 16, 2026
