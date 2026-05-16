# Environment Configuration Guide

## 🔧 Environment Variables

### For Local Development (docker-compose)

```env
# Database
DB_HOST=postgres
DB_PORT=5432
DB_NAME=devshop_db
DB_USER=devshop_user
DB_PASSWORD=devshop_password

# Service URLs
INVENTORY_SERVICE_URL=http://inventory-service:8081
ORDER_SERVICE_URL=http://order-service:8082

# Spring Profile
SPRING_PROFILE=docker
```

### For Development (application-dev.yml)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devshop_db?sslmode=disable
    username: devshop_user
    password: devshop_password
```

### For Production (Neo Cloud)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://ep-fancy-cake-aoo5cpag.c-2.ap-southeast-1.aws.neon.tech:5432/neondb?sslmode=require
    username: neondb_owner
    password: npg_D6JoRyB0vikc
```

## 🌍 Profile-Specific Configurations

Create these files for environment-specific settings:

### Dev Profile (`application-dev.yml`)
```bash
src/main/resources/application-dev.yml
```

### Test Profile (`application-test.yml`)
```bash
src/main/resources/application-test.yml
```

### Prod Profile (`application-prod.yml`)
```bash
src/main/resources/application-prod.yml
```

## 🚀 Running with Different Profiles

```bash
# Dev (local)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Test
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"

# Prod
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## 📋 TODO: Phase 6 - API Gateway Security

Create `JwtConfig.java` in api-gateway:

```java
package com.devshop.gateway.config;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Bean
    public JwtValidator jwtValidator() {
        return new JwtValidator(secret);
    }
}
```

## 📋 TODO: Phase 7 - Observability

Add Jaeger tracing for distributed tracing:

```yaml
spring:
  cloud:
    sleuth:
      sampler:
        probability: 1.0
  zipkin:
    base-url: http://localhost:9411
```

---

See `README.md` for full documentation.

