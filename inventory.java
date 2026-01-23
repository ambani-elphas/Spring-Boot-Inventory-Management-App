// Inventory Management System - Spring Boot (MVP)
// Package structure and key files included in one document for clarity.

// =============================
// pom.xml
// =============================
/*
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>inventory</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>inventory</name>
    <description>Inventory Management App</description>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
    </parent>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
*/

// =============================
// InventoryApplication.java
// =============================
package com.example.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}

// =============================
// entity/Product.java
// =============================
package com.example.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String category;
    private int quantity;
    private double price;
}

// =============================
// repository/ProductRepository.java
// =============================
package com.example.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.inventory.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

// =============================
// service/ProductService.java
// =============================
package com.example.inventory.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.entity.Product;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product save(Product product) {
        return repository.save(product);
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

// =============================
// controller/ProductController.java
// =============================
package com.example.inventory.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.example.inventory.service.ProductService;
import com.example.inventory.entity.Product;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public List<Product> getAll() {
        return service.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.save(product);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return service.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

// =============================
// application.properties
// =============================
/*
spring.datasource.url=jdbc:h2:mem:inventorydb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
*/

// =============================
// Run:
// mvn spring-boot:run
// Access API: http://localhost:8080/api/products
// H2 Console: http://localhost:8080/h2-console
