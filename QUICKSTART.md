# 🚀 Quick Start Guide

## 1️⃣ Local Development Setup

### Option A: Docker Compose (Easiest - Recommended)

```bash
# Clone the repository
git clone https://github.com/Franco122698/dev-shop.git
cd dev-shop

# Spin up all services with a single command
docker-compose up

# Check if services are running
curl http://localhost:8080/actuator/health
```

**What starts:**
- ✅ PostgreSQL Database (Port 5432)
- ✅ Inventory Service (Port 8081)
- ✅ Order Service (Port 8082)
- ✅ API Gateway (Port 8080)

### Option B: Maven CLI (For Development)

```bash
# Terminal 1: Inventory Service
cd inventory-service
mvn spring-boot:run

# Terminal 2: Order Service (new terminal)
cd order-service
mvn spring-boot:run

# Terminal 3: API Gateway (new terminal)
cd api-gateway
mvn spring-boot:run
```

## 2️⃣ Create Your First Product

```bash
curl -X POST http://localhost:8080/api/v1/inventory/products \
  -H "X-Store-Id: 1" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "FLASH-TSHIRT-001",
    "name": "Flash Sale T-Shirt",
    "description": "Premium cotton, limited stock",
    "price": 19.99,
    "stockCount": 50
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Product created",
  "data": {
    "id": 1,
    "storeId": 1,
    "sku": "FLASH-TSHIRT-001",
    "name": "Flash Sale T-Shirt",
    "price": 19.99,
    "stockCount": 50,
    "version": 0
  }
}
```

## 3️⃣ Create an Order (Test Inventory Deduction)

```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "X-Store-Id: 1" \
  -H "Content-Type: application/json" \
  -d '{
    "buyerId": 123,
    "productId": 1,
    "quantity": 2,
    "totalPrice": 39.98
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "storeId": 1,
    "buyerId": 123,
    "productId": 1,
    "quantity": 2,
    "totalPrice": 39.98,
    "status": "PAID"
  }
}
```

## 4️⃣ Verify Stock Was Deducted

```bash
curl -X GET http://localhost:8080/api/v1/inventory/products/1 \
  -H "X-Store-Id: 1"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "stockCount": 48,  // Was 50, reduced by 2
    "version": 1        // Version incremented (optimistic locking)
  }
}
```

## 5️⃣ Multi-Tenancy Test (Store 2)

```bash
# Create product for Store 2
curl -X POST http://localhost:8080/api/v1/inventory/products \
  -H "X-Store-Id: 2" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "BOUTIQUE-B-HOODIE",
    "name": "Premium Boutique Hoodie",
    "price": 49.99,
    "stockCount": 30
  }'

# Store 1 products won't be visible to Store 2
curl -X GET http://localhost:8080/api/v1/inventory/products \
  -H "X-Store-Id: 2"
  # Only shows BOUTIQUE-B-HOODIE, not FLASH-TSHIRT-001
```

## 6️⃣ Test Circuit Breaker (Resilience)

### Stop Inventory Service
```bash
# If using Docker Compose
docker-compose stop inventory-service

# Try to create an order
curl -X POST http://localhost:8080/api/v1/orders \
  -H "X-Store-Id: 1" \
  -H "Content-Type: application/json" \
  -d '{
    "buyerId": 124,
    "productId": 1,
    "quantity": 1,
    "totalPrice": 19.99
  }'
```

**Response (Circuit Breaker fallback):**
```json
{
  "success": false,
  "data": {
    "status": "FAILED",
    "failureReason": "Inventory service temporarily unavailable"
  }
}
```

Order is still created but marked as FAILED - preventing data loss!

### Restart Inventory Service
```bash
docker-compose start inventory-service

# Try again - should succeed after service recovers
```

## 🧪 Run Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# Specific service tests
mvn test -pl inventory-service
```

## 📊 Monitor Services

### Health Endpoints
```bash
# Inventory Service
curl http://localhost:8081/actuator/health

# Order Service
curl http://localhost:8082/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health
```

### Metrics
```bash
# Request metrics
curl http://localhost:8081/actuator/metrics/http.server.requests

# Circuit breaker status
curl http://localhost:8082/actuator/health/circuitbreakers
```

## 🔌 Debugging

### View Logs
```bash
# Docker Compose logs
docker-compose logs -f inventory-service
docker-compose logs -f order-service

# Search for trace IDs
docker-compose logs | grep "trace-id-12345"
```

### Connect to PostgreSQL

```bash
# Using psql
psql postgresql://devshop_user:devshop_password@localhost:5432/devshop_db

# List tables
\dt

# Query products
SELECT id, sku, name, stock_count, version FROM products WHERE store_id = 1;

# Query orders
SELECT id, buyer_id, status FROM orders WHERE store_id = 1;
```

## 📝 Git Workflow Example

```bash
# Create a feature branch from dev
git checkout dev
git checkout -b feature/add-product-filters

# Make changes
# ...

# Commit and push
git add .
git commit -m "feat: add product filtering by category"
git push -u origin feature/add-product-filters

# Create PR on GitHub from feature → dev
# After approval, merge to dev

# Later, merge dev → test for QA testing
# Finally, merge test → main for production
```

## 🐳 Docker Compose Common Commands

```bash
# Start all services
docker-compose up

# Start in background
docker-compose up -d

# Stop services
docker-compose stop

# Stop and remove containers
docker-compose down

# View logs
docker-compose logs -f

# Restart a specific service
docker-compose restart inventory-service

# Rebuild images
docker-compose build

# Execute command in container
docker-compose exec order-service bash
```

## 🆘 Troubleshooting

### "Connection refused" on localhost:8080
```bash
# Check if services are running
docker-compose ps

# View logs for errors
docker-compose logs api-gateway

# Ensure ports are not in use
# Linux/Mac: lsof -i :8080
# Windows: netstat -ano | findstr :8080
```

### "Optimistic locking failure" error
This happens when two requests try to update the same product simultaneously. It's expected in flash sale scenarios. The client should implement retry logic.

### Database connection issues
```bash
# Test Neo Cloud connection
psql postgresql://neondb_owner:npg_D6JoRyB0vikc@ep-fancy-cake-aoo5cpag.c-2.ap-southeast-1.aws.neon.tech/neondb?sslmode=require

# Ensure SSL is enabled for Neo Cloud
# Check application.yml has ?sslmode=require in connection URL
```

### Circuit Breaker stuck in OPEN state
```bash
# Check circuit breaker health
curl http://localhost:8082/actuator/health/circuitbreakers

# Wait for waitDurationInOpenState (default: 10 seconds)
# Or restart the service
docker-compose restart order-service
```

## 📚 Next Steps

1. **Phase 6 - API Security:** Add JWT token validation
2. **Phase 7 - Observability:** Integrate Jaeger tracing
3. **Phase 8 - Cloud:** Deploy to Azure AKS
4. **Phase 9 - CI/CD:** Set up automated deployments
5. **Phase 10 - Launch:** Go live with your first white-label customer

---

See `README.md` for full documentation and `ENV.md` for environment configuration.

