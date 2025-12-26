/*package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    private final StockRecordRepository stockRepository;
    private final ConsumptionLogRepository logRepository;
    private final PredictionRuleRepository ruleRepository;

    @Override
    public PredictionRule createRule(PredictionRule rule) {

        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Invalid average window");
        }

        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Invalid daily usage range");
        }

        if (ruleRepository.findByRuleName(rule.getRuleName()).isPresent()) {
            throw new IllegalArgumentException("Rule already exists");
        }

        rule.setCreatedAt(LocalDateTime.now());
        return ruleRepository.save(rule);
    }

    @Override
    public List<PredictionRule> getAllRules() {
        return ruleRepository.findAll();
    }

    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {

        StockRecord record = stockRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));

        List<ConsumptionLog> logs =
                logRepository.findByStockRecordId(stockRecordId);

        if (logs.isEmpty()) {
            return LocalDate.now();
        }

        double avgUsage = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .average()
                .orElse(0);

        if (avgUsage == 0) {
            return LocalDate.now();
        }

        int daysRemaining =
                (int) ((record.getCurrentQuantity() - record.getReorderThreshold()) / avgUsage);

        return LocalDate.now().plusDays(Math.max(daysRemaining, 0));
    }
}
*/
I'll provide a complete implementation of the Smart Inventory Restock Predictor application. This is a comprehensive Spring Boot application with all the required components.

## 1. Project Structure

First, let me show you the complete project structure:

```
demo/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── demo/
        │               ├── config/
        │               │   ├── OpenApiConfig.java
        │               │   ├── SecurityConfig.java
        │               │   └── JwtAuthenticationFilter.java
        │               ├── controller/
        │               │   ├── AuthController.java
        │               │   ├── ConsumptionLogController.java
        │               │   ├── PredictionController.java
        │               │   ├── ProductController.java
        │               │   ├── StockRecordController.java
        │               │   └── WarehouseController.java
        │               ├── dto/
        │               │   ├── AuthRequest.java
        │               │   ├── AuthResponse.java
        │               │   └── UserRegisterDto.java
        │               ├── exception/
        │               │   ├── GlobalExceptionHandler.java
        │               │   └── ResourceNotFoundException.java
        │               ├── model/
        │               │   ├── Product.java
        │               │   ├── Warehouse.java
        │               │   ├── StockRecord.java
        │               │   ├── ConsumptionLog.java
        │               │   ├── PredictionRule.java
        │               │   ├── User.java
        │               │   └── Role.java
        │               ├── repository/
        │               │   ├── ProductRepository.java
        │               │   ├── WarehouseRepository.java
        │               │   ├── StockRecordRepository.java
        │               │   ├── ConsumptionLogRepository.java
        │               │   ├── PredictionRuleRepository.java
        │               │   └── UserRepository.java
        │               ├── security/
        │               │   ├── CustomUserDetailsService.java
        │               │   ├── JwtAuthenticationEntryPoint.java
        │               │   └── JwtProvider.java
        │               ├── service/
        │               │   ├── impl/
        │               │   │   ├── ProductServiceImpl.java
        │               │   │   ├── WarehouseServiceImpl.java
        │               │   │   ├── StockRecordServiceImpl.java
        │               │   │   ├── ConsumptionLogServiceImpl.java
        │               │   │   ├── PredictionServiceImpl.java
        │               │   │   └── UserServiceImpl.java
        │               │   ├── ProductService.java
        │               │   ├── WarehouseService.java
        │               │   ├── StockRecordService.java
        │               │   ├── ConsumptionLogService.java
        │               │   ├── PredictionService.java
        │               │   └── UserService.java
        │               ├── util/
        │               │   └── DateUtil.java
        │               └── DemoApplication.java
        └── resources/
            └── application.properties
```

## 2. Maven Dependencies (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo</name>
    <description>Smart Inventory Restock Predictor</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.5</version>
        </dependency>
        
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- OpenAPI / Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## 3. Application Properties (application.properties)

```properties
# Server Configuration
server.port=8080
spring.application.name=smart-inventory-predictor

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=your-secret-key-which-should-be-very-long-and-secure-in-production
jwt.expiration=86400000 # 24 hours in milliseconds

# OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

## 4. Main Application Class

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

## 5. Models (Entities)

### 5.1 Role.java
```java
package com.example.demo.model;

public enum Role {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_MANAGER
}
```

### 5.2 User.java
```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", 
                     joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

### 5.3 Product.java
```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(unique = true, nullable = false)
    private String sku;
    
    private String category;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
```

### 5.4 Warehouse.java
```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "warehouse_name", unique = true, nullable = false)
    private String warehouseName;
    
    @Column(nullable = false)
    private String location;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
```

### 5.5 StockRecord.java
```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_records",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"product_id", "warehouse_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    
    @Column(name = "current_quantity", nullable = false)
    @Builder.Default
    private Integer currentQuantity = 0;
    
    @Column(name = "reorder_threshold", nullable = false)
    private Integer reorderThreshold;
    
    @Column(name = "last_updated", nullable = false)
    @Builder.Default
    private LocalDateTime lastUpdated = LocalDateTime.now();
}
```

### 5.6 ConsumptionLog.java
```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumption_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumptionLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_record_id", nullable = false)
    private StockRecord stockRecord;
    
    @Column(name = "consumed_quantity", nullable = false)
    private Integer consumedQuantity;
    
    @Column(name = "consumed_date", nullable = false)
    private LocalDate consumedDate;
    
    @Column(name = "logged_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime loggedAt = LocalDateTime.now();
}
```

### 5.7 PredictionRule.java
```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rule_name", unique = true, nullable = false)
    private String ruleName;
    
    @Column(name = "average_days_window", nullable = false)
    private Integer averageDaysWindow;
    
    @Column(name = "min_daily_usage", nullable = false)
    private Integer minDailyUsage;
    
    @Column(name = "max_daily_usage", nullable = false)
    private Integer maxDailyUsage;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
```

## 6. DTOs

### 6.1 AuthRequest.java
```java
package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
}
```

### 6.2 AuthResponse.java
```java
package com.example.demo.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String token;
    private Long userId;
    private String email;
    private Set<String> roles;
}
```

### 6.3 UserRegisterDto.java
```java
package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @Builder.Default
    private Set<String> roles = new HashSet<>();
}
```

## 7. Repositories

### 7.1 UserRepository.java
```java
package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### 7.2 ProductRepository.java
```java
package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
}
```

### 7.3 WarehouseRepository.java
```java
package com.example.demo.repository;

import com.example.demo.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByWarehouseName(String warehouseName);
    boolean existsByWarehouseName(String warehouseName);
}
```

### 7.4 StockRecordRepository.java
```java
package com.example.demo.repository;

import com.example.demo.model.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    List<StockRecord> findByProductId(Long productId);
    List<StockRecord> findByWarehouseId(Long warehouseId);
    Optional<StockRecord> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
```

### 7.5 ConsumptionLogRepository.java
```java
package com.example.demo.repository;

import com.example.demo.model.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionLogRepository extends JpaRepository<ConsumptionLog, Long> {
    List<ConsumptionLog> findByStockRecordId(Long stockRecordId);
    List<ConsumptionLog> findByStockRecordIdAndConsumedDateBetween(
            Long stockRecordId, java.time.LocalDate startDate, java.time.LocalDate endDate);
}
```

### 7.6 PredictionRuleRepository.java
```java
package com.example.demo.repository;

import com.example.demo.model.PredictionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PredictionRuleRepository extends JpaRepository<PredictionRule, Long> {
    Optional<PredictionRule> findByRuleName(String ruleName);
    boolean existsByRuleName(String ruleName);
}
```

## 8. Service Interfaces

### 8.1 UserService.java
```java
package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserRegisterDto;
import com.example.demo.model.User;

public interface UserService {
    AuthResponse login(AuthRequest request);
    User register(UserRegisterDto dto);
    User getByEmail(String email);
    User getById(Long id);
}
```

### 8.2 ProductService.java
```java
package com.example.demo.service;

import com.example.demo.model.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
    void deleteProduct(Long id);
}
```

### 8.3 WarehouseService.java
```java
package com.example.demo.service;

import com.example.demo.model.Warehouse;
import java.util.List;

public interface WarehouseService {
    Warehouse createWarehouse(Warehouse warehouse);
    Warehouse getWarehouse(Long id);
    List<Warehouse> getAllWarehouses();
    void deleteWarehouse(Long id);
}
```

### 8.4 StockRecordService.java
```java
package com.example.demo.service;

import com.example.demo.model.StockRecord;
import java.util.List;

public interface StockRecordService {
    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record);
    StockRecord getStockRecord(Long id);
    List<StockRecord> getRecordsByProduct(Long productId);
    List<StockRecord> getRecordsByWarehouse(Long warehouseId);
    StockRecord updateStockRecord(Long id, StockRecord record);
    void deleteStockRecord(Long id);
}
```

### 8.5 ConsumptionLogService.java
```java
package com.example.demo.service;

import com.example.demo.model.ConsumptionLog;
import java.util.List;

public interface ConsumptionLogService {
    ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log);
    ConsumptionLog getLog(Long id);
    List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId);
    void deleteLog(Long id);
}
```

### 8.6 PredictionService.java
```java
package com.example.demo.service;

import com.example.demo.model.PredictionRule;
import java.time.LocalDate;
import java.util.List;

public interface PredictionService {
    LocalDate predictRestockDate(Long stockRecordId);
    List<PredictionRule> getAllRules();
    PredictionRule createRule(PredictionRule rule);
    PredictionRule getRule(Long id);
    void deleteRule(Long id);
}
```

## 9. Service Implementations

### 9.1 UserServiceImpl.java
```java
package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserRegisterDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    
    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String token = jwtProvider.generateToken(authentication);
        
        Set<String> roles = user.getRoles().stream()
            .map(Role::name)
            .collect(Collectors.toSet());
        
        return AuthResponse.builder()
            .token(token)
            .userId(user.getId())
            .email(user.getEmail())
            .roles(roles)
            .build();
    }
    
    @Override
    @Transactional
    public User register(UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        Set<Role> roles = new HashSet<>();
        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            roles.add(Role.ROLE_USER);
        } else {
            roles = dto.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        }
        
        User user = User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .roles(roles)
            .build();
        
        return userRepository.save(user);
    }
    
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
```

### 9.2 ProductServiceImpl.java
```java
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    @Transactional
    public Product createProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }
        
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }
        
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        
        return productRepository.save(product);
    }
    
    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
```

### 9.3 WarehouseServiceImpl.java
```java
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    
    @Override
    @Transactional
    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse.getWarehouseName() == null || warehouse.getWarehouseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse name cannot be empty");
        }
        
        if (warehouse.getLocation() == null || warehouse.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        
        if (warehouseRepository.existsByWarehouseName(warehouse.getWarehouseName())) {
            throw new IllegalArgumentException("Warehouse name already exists");
        }
        
        if (warehouse.getCreatedAt() == null) {
            warehouse.setCreatedAt(LocalDateTime.now());
        }
        
        return warehouseRepository.save(warehouse);
    }
    
    @Override
    public Warehouse getWarehouse(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
    }
    
    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
    
    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found");
        }
        warehouseRepository.deleteById(id);
    }
}
```

### 9.4 StockRecordServiceImpl.java
```java
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.StockRecord;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockRecordServiceImpl implements StockRecordService {
    
    private final StockRecordRepository stockRecordRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    
    @Override
    @Transactional
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        
        if (stockRecordRepository.existsByProductIdAndWarehouseId(productId, warehouseId)) {
            throw new IllegalArgumentException("StockRecord already exists");
        }
        
        if (record.getCurrentQuantity() < 0) {
            throw new IllegalArgumentException("Current quantity must be greater than or equal to zero");
        }
        
        if (record.getReorderThreshold() <= 0) {
            throw new IllegalArgumentException("Reorder threshold must be greater than zero");
        }
        
        record.setProduct(product);
        record.setWarehouse(warehouse);
        record.setLastUpdated(LocalDateTime.now());
        
        return stockRecordRepository.save(record);
    }
    
    @Override
    public StockRecord getStockRecord(Long id) {
        return stockRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
    }
    
    @Override
    public List<StockRecord> getRecordsByProduct(Long productId) {
        return stockRecordRepository.findByProductId(productId);
    }
    
    @Override
    public List<StockRecord> getRecordsByWarehouse(Long warehouseId) {
        return stockRecordRepository.findByWarehouseId(warehouseId);
    }
    
    @Override
    @Transactional
    public StockRecord updateStockRecord(Long id, StockRecord record) {
        StockRecord existing = getStockRecord(id);
        
        if (record.getCurrentQuantity() != null) {
            if (record.getCurrentQuantity() < 0) {
                throw new IllegalArgumentException("Current quantity must be greater than or equal to zero");
            }
            existing.setCurrentQuantity(record.getCurrentQuantity());
        }
        
        if (record.getReorderThreshold() != null) {
            if (record.getReorderThreshold() <= 0) {
                throw new IllegalArgumentException("Reorder threshold must be greater than zero");
            }
            existing.setReorderThreshold(record.getReorderThreshold());
        }
        
        existing.setLastUpdated(LocalDateTime.now());
        
        return stockRecordRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void deleteStockRecord(Long id) {
        if (!stockRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("StockRecord not found");
        }
        stockRecordRepository.deleteById(id);
    }
}
```

### 9.5 ConsumptionLogServiceImpl.java
```java
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.ConsumptionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumptionLogServiceImpl implements ConsumptionLogService {
    
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordRepository stockRecordRepository;
    
    @Override
    @Transactional
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
        
        if (log.getConsumedQuantity() <= 0) {
            throw new IllegalArgumentException("Consumed quantity must be greater than zero");
        }
        
        if (log.getConsumedDate() == null) {
            log.setConsumedDate(LocalDate.now());
        }
        
        if (log.getConsumedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be future");
        }
        
        // Update stock record quantity
        int newQuantity = stockRecord.getCurrentQuantity() - log.getConsumedQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        
        stockRecord.setCurrentQuantity(newQuantity);
        stockRecord.setLastUpdated(LocalDateTime.now());
        stockRecordRepository.save(stockRecord);
        
        log.setStockRecord(stockRecord);
        log.setLoggedAt(LocalDateTime.now());
        
        return consumptionLogRepository.save(log);
    }
    
    @Override
    public ConsumptionLog getLog(Long id) {
        return consumptionLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ConsumptionLog not found"));
    }
    
    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }
    
    @Override
    @Transactional
    public void deleteLog(Long id) {
        if (!consumptionLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("ConsumptionLog not found");
        }
        consumptionLogRepository.deleteById(id);
    }
}
```

### 9.6 PredictionServiceImpl.java
```java
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {
    
    private final StockRecordRepository stockRecordRepository;
    private final ConsumptionLogRepository consumptionLogRepository;
    private final PredictionRuleRepository predictionRuleRepository;
    
    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
        
        // Get the default prediction rule (first rule or specific rule)
        PredictionRule rule = predictionRuleRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("No prediction rules found"));
        
        // Calculate average daily consumption
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(rule.getAverageDaysWindow());
        
        List<ConsumptionLog> logs = consumptionLogRepository
            .findByStockRecordIdAndConsumedDateBetween(stockRecordId, startDate, endDate);
        
        if (logs.isEmpty()) {
            return endDate.plusDays(30); // Default prediction if no consumption data
        }
        
        // Calculate total consumption and days
        int totalConsumption = logs.stream()
            .mapToInt(ConsumptionLog::getConsumedQuantity)
            .sum();
        
        long daysBetween = ChronoUnit.DAYS.between(
            logs.get(0).getConsumedDate(),
            logs.get(logs.size() - 1).getConsumedDate()
        ) + 1; // +1 to include both start and end days
        
        double averageDailyConsumption = (double) totalConsumption / daysBetween;
        
        // Apply min/max thresholds
        averageDailyConsumption = Math.max(averageDailyConsumption, rule.getMinDailyUsage());
        averageDailyConsumption = Math.min(averageDailyConsumption, rule.getMaxDailyUsage());
        
        if (averageDailyConsumption <= 0) {
            return endDate.plusDays(30); // No consumption, default prediction
        }
        
        // Calculate days until reorder threshold
        int currentQuantity = stockRecord.getCurrentQuantity();
        int reorderThreshold = stockRecord.getReorderThreshold();
        
                if (currentQuantity <= reorderThreshold) {
            return LocalDate.now(); // Already at or below reorder threshold
        }
        
        int quantityUntilReorder = currentQuantity - reorderThreshold;
        long daysUntilReorder = (long) Math.ceil(quantityUntilReorder / averageDailyConsumption);
        
        return LocalDate.now().plusDays(daysUntilReorder);
    }
    
    @Override
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    @Override
    public PredictionRule createRule(PredictionRule rule) {
        if (rule.getRuleName() == null || rule.getRuleName().trim().isEmpty()) {
            throw new IllegalArgumentException("Rule name cannot be empty");
        }
        
        if (predictionRuleRepository.existsByRuleName(rule.getRuleName())) {
            throw new IllegalArgumentException("Rule name already exists");
        }
        
        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Average days window must be greater than zero");
        }
        
        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Min daily usage must be less than or equal to max daily usage");
        }
        
        if (rule.getCreatedAt() == null) {
            rule.setCreatedAt(LocalDateTime.now());
        }
        
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    public PredictionRule getRule(Long id) {
        return predictionRuleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PredictionRule not found"));
    }
    
    @Override
    public void deleteRule(Long id) {
        if (!predictionRuleRepository.existsById(id)) {
            throw new ResourceNotFoundException("PredictionRule not found");
        }
        predictionRuleRepository.deleteById(id);
    }
}