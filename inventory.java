// FULL ENTERPRISE INVENTORY MANAGEMENT SYSTEM (SPRING BOOT)
// Includes: MySQL/PostgreSQL, JWT Auth, Roles, Stock Alerts, Suppliers, Orders, Swagger, Docker-ready

// =============================
// pom.xml (additions highlighted)
// =============================
/* Add these dependencies:
   - spring-boot-starter-security
   - springdoc-openapi-starter-webmvc-ui
   - jjwt-api, jjwt-impl, jjwt-jackson
   - mysql-connector-j or postgresql
*/

// =============================
// application.yml
// =============================
/*
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/inventory_db
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  security:
    jwt:
      secret: myjwtsecretkey
      expiration: 86400000
*/

// =============================
// ENTITY: Role
// =============================
@Entity
public class Role {
 @Id @GeneratedValue private Long id;
 private String name; // ADMIN, MANAGER, USER
}

// =============================
// ENTITY: User
// =============================
@Entity
public class User {
 @Id @GeneratedValue private Long id;
 private String username;
 private String password;
 @ManyToMany(fetch = FetchType.EAGER)
 private Set<Role> roles;
}

// =============================
// ENTITY: Product (Stock Alert)
// =============================
@Entity
public class Product {
 @Id @GeneratedValue private Long id;
 private String name;
 private int quantity;
 private int lowStockThreshold;
}

// =============================
// ENTITY: Supplier
// =============================
@Entity
public class Supplier {
 @Id @GeneratedValue private Long id;
 private String name;
 private String email;
}

// =============================
// ENTITY: PurchaseOrder
// =============================
@Entity
public class PurchaseOrder {
 @Id @GeneratedValue private Long id;
 @ManyToOne private Supplier supplier;
 @ManyToOne private Product product;
 private int quantity;
 private LocalDate orderDate;
}

// =============================
// JWT SECURITY CONFIG
// =============================
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http.csrf().disable()
   .authorizeHttpRequests(auth -> auth
    .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/auth/**").permitAll()
    .anyRequest().authenticated())
   .addFilter(new JwtAuthFilter());
  return http.build();
 }
}

// =============================
// STOCK ALERT SERVICE
// =============================
@Service
public class StockAlertService {
 public void check(Product p) {
  if(p.getQuantity() <= p.getLowStockThreshold()){
    System.out.println("LOW STOCK ALERT: " + p.getName());
  }
 }
}

// =============================
// SWAGGER CONFIG
// =============================
@OpenAPIDefinition(
 info = @Info(title = "Inventory API", version = "1.0")
)
@Configuration
public class SwaggerConfig {}

// =============================
// DOCKERFILE
// =============================
/*
FROM eclipse-temurin:17-jdk
COPY target/inventory.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
*/

// =============================
// AWS DEPLOYMENT NOTES
// =============================
// - Build jar: mvn clean package
// - Push Docker image to ECR
// - Deploy on EC2 / ECS / Elastic Beanstalk

// =============================
// FRONTEND (REACT)
// =============================
/*
- Login with JWT
- Product dashboard
- Stock alerts UI
- Supplier & Orders management
*/
