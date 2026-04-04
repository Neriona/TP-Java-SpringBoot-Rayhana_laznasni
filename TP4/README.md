# TP°4 : Architecture Orientée Services (SOA) - E-Commerce

**Université ENSA Beni Mellal** 

## 📋 Table of Contents / Table des matières

- [English](#english)
- [Français](#français)

---

## English

### 1. Project Overview

This is a **Service-Oriented Architecture (SOA)** implementation of an e-commerce application using Spring Boot microservices. The project demonstrates core SOA principles including service discovery, inter-service communication, and distributed data management.

**Key Features:**
- ✅ Microservices architecture with Spring Boot
- ✅ Service discovery using Eureka
- ✅ Inter-service communication via REST & OpenFeign
- ✅ Database per service pattern (H2 in-memory)
- ✅ API Gateway pattern (exercise)
- ✅ Error handling and resilience

### 2. Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Applications                  │
└────────────────────────┬────────────────────────────────────┘
                         │
        ┌────────────────▼─────────────────┐
        │       API Gateway (8080)         │  ← Exercise 2
        │   (Spring Cloud Gateway)         │
        └────────────────┬────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
   ┌────▼────┐      ┌────▼────┐    ┌─────▼──────┐
   │ Product │      │  Order  │    │   Eureka   │
   │ Service │      │ Service │    │   Server   │
   │ (8081)  │      │ (8082)  │    │   (8761)   │
   │         │      │         │    │            │
   │ H2 DB   │      │ H2 DB   │    │  Registry  │
   └─────────┘      └─────────┘    └─  ─────────┘
```

### 3. Prerequisites

**System Requirements:**
- Java JDK 21 or higher
- Maven 3.6+
- IDE: IntelliJ IDEA, Eclipse, or VS Code
- Git (optional)

**Tools for Testing:**
- Postman or cURL
- Any browser for Eureka dashboard

**Installation Check:**
```bash
java -version
mvn -version
```

### 4. Project Structure

```
tp4-soa-ecommerce/
├── eureka-server/              # Service Discovery
│   ├── src/main/java/
│   │   └── com/ecommerce/eurekaserver/
│   │       └── EurekaServerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── product-service/            # Product Catalog Management
│   ├── src/main/java/
│   │   └── com/ecommerce/productservice/
│   │       ├── model/
│   │       │   └── Product.java
│   │       ├── repository/
│   │       │   └── ProductRepository.java
│   │       ├── service/
│   │       │   └── ProductService.java
│   │       ├── controller/
│   │       │   └── ProductController.java
│   │       └── ProductServiceApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── order-service/              # Order Management
│   ├── src/main/java/
│   │   └── com/ecommerce/orderservice/
│   │       ├── model/
│   │       │   └── Order.java
│   │       ├── dto/
│   │       │   └── ProductDTO.java
│   │       ├── client/
│   │       │   └── ProductClient.java
│   │       ├── repository/
│   │       │   └── OrderRepository.java
│   │       ├── service/
│   │       │   └── OrderService.java
│   │       ├── controller/
│   │       │   └── OrderController.java
│   │       └── OrderServiceApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── api-gateway/                # API Gateway (Exercise 2)
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
└── README.md                   # This file
```

### 5. Services Description

#### 5.1 Eureka Server
**Purpose:** Service discovery and registry for all microservices

**Port:** 8761  
**Dashboard:** http://localhost:8761

**Responsibilities:**
- Register microservices
- Maintain service health checks
- Enable service-to-service discovery

**Key Configuration:**
```properties
server.port=8761
spring.application.name=eureka-server
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

#### 5.2 Product Service
**Purpose:** Manage product catalog

**Port:** 8081  
**Base URL:** http://localhost:8081/api/products

**Main Components:**
- `Product` Entity: id, name, description, price, stock
- `ProductRepository`: CRUD operations
- `ProductService`: Business logic
- `ProductController`: REST endpoints

**Database:** H2 in-memory (productdb)

#### 5.3 Order Service
**Purpose:** Manage customer orders

**Port:** 8082  
**Base URL:** http://localhost:8082/api/orders

**Main Components:**
- `Order` Entity: id, productId, productName, quantity, totalPrice, orderDate, status
- `ProductClient`: Feign client for Product Service communication
- `OrderRepository`: CRUD operations
- `OrderService`: Business logic (calls Product Service)
- `OrderController`: REST endpoints

**Database:** H2 in-memory (orderdb)

**Inter-Service Communication:** Uses OpenFeign to call Product Service

#### 5.4 API Gateway (Exercise)
**Purpose:** Single entry point for all client requests

**Port:** 8080  
**Technology:** Spring Cloud Gateway

**Routes:**
- `/products/**` → Product Service (8081)
- `/orders/**` → Order Service (8082)

### 6. Installation & Setup

#### 6.1 Clone or Download the Project
```bash
git clone <repository-url>
cd tp4-soa-ecommerce
```

#### 6.2 Important: Fix Spring Boot Version Compatibility

**⚠️ Critical Step:** All services must use the same Spring Boot version.

Update all `pom.xml` files to use:
- **Spring Boot:** 4.0.5
- **Spring Cloud:** 2025.1.1

**eureka-server/pom.xml:**
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.5</version>
</parent>

<properties>
    <spring-cloud.version>2025.1.1</spring-cloud.version>
</properties>
```

#### 6.3 Build All Services
```bash
# Eureka Server
cd eureka-server
mvn clean install
cd ..

# Product Service
cd product-service
mvn clean install
cd ..

# Order Service
cd order-service
mvn clean install
cd ..
```

### 7. Running the Services

**⚠️ Important:** Start services in this order:

#### Step 1: Start Eureka Server
```bash
cd eureka-server
./mvnw spring-boot:run
```
Expected output:
```
Started EurekaServerApplication in X seconds
```
✅ Access dashboard: http://localhost:8761

#### Step 2: Start Product Service
```bash
cd product-service
./mvnw spring-boot:run
```
Expected output:
```
Started ProductServiceApplication in X seconds
Registering application PRODUCT-SERVICE with eureka with status UP
```

#### Step 3: Start Order Service
```bash
cd order-service
./mvnw spring-boot:run
```
Expected output:
```
Started OrderServiceApplication in X seconds
Registering application ORDER-SERVICE with eureka with status UP
```

#### Verify Services Registered
Visit: http://localhost:8761

You should see:
- ✅ EUREKA-SERVER
- ✅ PRODUCT-SERVICE
- ✅ ORDER-SERVICE

### 8. Testing the Application

#### 8.1 Create a Product

```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1200.00,
    "stock": 10
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1200.00,
  "stock": 10
}
```

#### 8.2 Get All Products

```bash
curl http://localhost:8081/api/products
```

#### 8.3 Get Product by ID

```bash
curl http://localhost:8081/api/products/1
```

#### 8.4 Create an Order

```bash
curl -X POST "http://localhost:8082/api/orders?productId=1&quantity=2"
```

**Expected Response:**
```json
{
  "id": 1,
  "productId": 1,
  "productName": "Laptop",
  "quantity": 2,
  "totalPrice": 2400.00,
  "orderDate": "2026-04-02T14:50:00",
  "status": "PENDING"
}
```

#### 8.5 Get All Orders

```bash
curl http://localhost:8082/api/orders
```

#### 8.5 Using Postman

Import this collection:

**Product Service Endpoints:**
- `POST /api/products` - Create product
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `DELETE /api/products/{id}` - Delete product

**Order Service Endpoints:**
- `POST /api/orders?productId={id}&quantity={qty}` - Create order
- `GET /api/orders` - List all orders

### 9. Troubleshooting

#### Issue: Eureka Server returns 404 Error

**Problem:** Services can't register with Eureka

**Solution:**
1. Ensure Spring Boot versions match across all services (should be 4.0.5)
2. Verify Eureka Server is running on port 8761
3. Check `eureka.client.service-url.defaultZone` in all services points to `http://localhost:8761/eureka/`
4. Check application.properties has `@EnableEurekaServer` annotation

```bash
# Test Eureka endpoint
curl http://localhost:8761/eureka/apps
```

#### Issue: Order Service can't call Product Service

**Problem:** Feign client throws exception

**Solution:**
1. Ensure Product Service is running and registered with Eureka
2. Verify `@EnableFeignClients` annotation is present in OrderServiceApplication
3. Check ProductClient interface has correct service name: `@FeignClient(name = "product-service")`

#### Issue: "Cannot determine local hostname"

**Warning:** This is non-critical. Services will still register with Eureka.

---

### 10. Exercises

#### Exercise 1: Add Stock Update Functionality
**Objective:** Update product stock after order creation

**Steps:**
1. Create `PUT /api/products/{id}/stock` endpoint in Product Service
2. Modify `OrderService.createOrder()` to call this endpoint
3. Handle case where stock is insufficient (throw exception)

**Implementation:**
```java
// ProductController.java
@PutMapping("/{id}/stock")
public ResponseEntity<Product> updateStock(
    @PathVariable Long id, 
    @RequestParam Integer quantity) {
    // Logic to decrease stock
}

// OrderService.java
public Order createOrder(Long productId, Integer quantity) {
    ProductDTO product = productClient.getProductById(productId);
    // Call updateStock endpoint
    productClient.updateStock(productId, quantity);
    // Create order
}
```

#### Exercise 2: Create API Gateway
**Objective:** Implement single entry point for all services

**Steps:**
1. Create new project with Spring Cloud Gateway dependency
2. Configure routes:
   - `/products/**` → `http://localhost:8081`
   - `/orders/**` → `http://localhost:8082`
3. Run on port 8080
4. Test: `http://localhost:8080/products`

**Configuration (application.yml):**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/products/**
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/orders/**
```

#### Exercise 3: Error Handling & Resilience
**Objective:** Improve error handling

**Requirements:**
1. Handle case where product doesn't exist
2. Handle case where Product Service is unavailable
3. Return appropriate HTTP status codes and error messages
4. Add logging for debugging

**Implementation Tips:**
- Use `@ExceptionHandler` for global exception handling
- Implement circuit breaker with Resilience4j
- Return JSON error responses with proper HTTP codes

---

### 11. Technologies Used

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 4.0.5 |
| **Cloud** | Spring Cloud | 2025.1.1 |
| **Service Discovery** | Netflix Eureka | 2.0.5 |
| **REST Communication** | OpenFeign | 13.6 |
| **Database** | H2 | 2.4.240 |
| **ORM** | Hibernate JPA | 7.2.7 |
| **Java** | JDK | 21 |
| **Build Tool** | Maven | 3.6+ |

---

### 12. Key Concepts

#### Service-Oriented Architecture (SOA)
- **Modularity:** Each service handles a specific business capability
- **Reusability:** Services can be called by multiple clients
- **Scalability:** Services can be scaled independently
- **Maintainability:** Easier to understand and modify individual services

#### Service Discovery
- Eureka maintains a registry of all available services
- Services register themselves on startup
- Service-to-service communication uses service names instead of hard-coded URLs

#### API Communication Patterns
- **Synchronous:** REST calls via OpenFeign (Order → Product)
- **Asynchronous:** (Can be enhanced with messaging)

---

### 13. References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Netflix Eureka](https://spring.io/projects/spring-cloud-netflix)
- [OpenFeign Documentation](https://spring.io/projects/spring-cloud-openfeign)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)

---

### 14. License & Credits

**Author:** Pr. BE ELBAGHAZAOUI  
**Institution:** ENSA Marrakech  
**Course:** TP°4 - Architecture Orientée Services (SOA)  
**Date:** 2026

---

---

## Français

### 1. Vue d'ensemble du projet

Il s'agit d'une implémentation d'**Architecture Orientée Services (SOA)** d'une application e-commerce utilisant les microservices Spring Boot. Le projet démontre les principes clés de SOA, notamment la découverte de services, la communication inter-services et la gestion des données distribuées.

**Caractéristiques principales:**
- ✅ Architecture microservices avec Spring Boot
- ✅ Découverte de services avec Eureka
- ✅ Communication inter-services via REST & OpenFeign
- ✅ Pattern base de données par service (H2 en mémoire)
- ✅ Pattern API Gateway (exercice)
- ✅ Gestion des erreurs et résilience

### 2. Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                  Applications Clients                        │
└────────────────────────┬────────────────────────────────────┘
                         │
        ┌────────────────▼─────────────────┐
        │       API Gateway (8080)         │  ← Exercice 2
        │   (Spring Cloud Gateway)         │
        └────────────────┬────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
   ┌────▼────┐      ┌────▼────┐    ┌─────▼──────┐
   │ Service │      │ Service │    │   Serveur  │
   │ Produit │      │ Commande│    │   Eureka   │
   │ (8081)  │      │ (8082)  │    │   (8761)   │
   │         │      │         │    │            │
   │ H2 DB   │      │ H2 DB   │    │ Registre   │
   └─────────┘      └─────────┘    └────────────┘
```

### 3. Prérequis

**Configuration système:**
- Java JDK 21 ou supérieur
- Maven 3.6+
- IDE: IntelliJ IDEA, Eclipse, ou VS Code
- Git (optionnel)

**Outils de test:**
- Postman ou cURL
- Navigateur pour le tableau de bord Eureka

**Vérification d'installation:**
```bash
java -version
mvn -version
```

### 4. Structure du projet

[Identique à la version anglaise]

### 5. Description des services

#### 5.1 Serveur Eureka
**Objectif:** Découverte de services et registre pour tous les microservices

**Port:** 8761  
**Tableau de bord:** http://localhost:8761

**Responsabilités:**
- Enregistrer les microservices
- Maintenir les vérifications de santé du service
- Permettre la découverte service-à-service

#### 5.2 Service Produit
**Objectif:** Gérer le catalogue de produits

**Port:** 8081  
**URL de base:** http://localhost:8081/api/products

**Composants principaux:**
- Entité `Product`: id, name, description, price, stock
- `ProductRepository`: Opérations CRUD
- `ProductService`: Logique métier
- `ProductController`: Points de terminaison REST

**Base de données:** H2 en mémoire (productdb)

#### 5.3 Service Commande
**Objectif:** Gérer les commandes des clients

**Port:** 8082  
**URL de base:** http://localhost:8082/api/orders

**Composants principaux:**
- Entité `Order`: id, productId, productName, quantity, totalPrice, orderDate, status
- `ProductClient`: Client Feign pour la communication avec le Service Produit
- `OrderRepository`: Opérations CRUD
- `OrderService`: Logique métier (appelle le Service Produit)
- `OrderController`: Points de terminaison REST

**Base de données:** H2 en mémoire (orderdb)

**Communication inter-services:** Utilise OpenFeign pour appeler le Service Produit

### 6. Installation et configuration

#### 6.1 Cloner ou télécharger le projet
```bash
git clone <url-du-référentiel>
cd tp4-soa-ecommerce
```

#### 6.2 Important : Corriger la compatibilité de Spring Boot

**⚠️ Étape critique :** Tous les services doivent utiliser la même version de Spring Boot.

Mettre à jour tous les fichiers `pom.xml` pour utiliser:
- **Spring Boot:** 4.0.5
- **Spring Cloud:** 2025.1.1

[Configuration identique à la version anglaise]

#### 6.3 Construire tous les services
```bash
# Serveur Eureka
cd eureka-server
mvn clean install
cd ..

# Service Produit
cd product-service
mvn clean install
cd ..

# Service Commande
cd order-service
mvn clean install
cd ..
```

### 7. Démarrage des services

**⚠️ Important :** Démarrer les services dans cet ordre:

#### Étape 1 : Démarrer le serveur Eureka
```bash
cd eureka-server
./mvnw spring-boot:run
```

Sortie attendue:
```
Started EurekaServerApplication in X seconds
```
✅ Accédez au tableau de bord: http://localhost:8761

#### Étape 2 : Démarrer le Service Produit
```bash
cd product-service
./mvnw spring-boot:run
```

#### Étape 3 : Démarrer le Service Commande
```bash
cd order-service
./mvnw spring-boot:run
```

#### Vérifier les services enregistrés
Visitez: http://localhost:8761

Vous devriez voir:
- ✅ EUREKA-SERVER
- ✅ PRODUCT-SERVICE
- ✅ ORDER-SERVICE

### 8. Test de l'application

#### 8.1 Créer un produit

```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ordinateur portable",
    "description": "Ordinateur portable haute performance",
    "price": 1200.00,
    "stock": 10
  }'
```

**Réponse attendue:**
```json
{
  "id": 1,
  "name": "Ordinateur portable",
  "description": "Ordinateur portable haute performance",
  "price": 1200.00,
  "stock": 10
}
```

#### 8.2 Obtenir tous les produits

```bash
curl http://localhost:8081/api/products
```

#### 8.3 Obtenir un produit par ID

```bash
curl http://localhost:8081/api/products/1
```

#### 8.4 Créer une commande

```bash
curl -X POST "http://localhost:8082/api/orders?productId=1&quantity=2"
```

**Réponse attendue:**
```json
{
  "id": 1,
  "productId": 1,
  "productName": "Ordinateur portable",
  "quantity": 2,
  "totalPrice": 2400.00,
  "orderDate": "2026-04-02T14:50:00",
  "status": "PENDING"
}
```

#### 8.5 Obtenir toutes les commandes

```bash
curl http://localhost:8082/api/orders
```

### 9. Dépannage

#### Problème: Le serveur Eureka retourne une erreur 404

**Problème:** Les services ne peuvent pas s'enregistrer avec Eureka

**Solution:**
1. Assurez-vous que les versions de Spring Boot correspondent sur tous les services (doivent être 4.0.5)
2. Vérifiez que le serveur Eureka s'exécute sur le port 8761
3. Vérifiez que `eureka.client.service-url.defaultZone` dans tous les services pointe vers `http://localhost:8761/eureka/`
4. Vérifiez que application.properties a l'annotation `@EnableEurekaServer`

```bash
# Tester le point de terminaison Eureka
curl http://localhost:8761/eureka/apps
```

#### Problème: Le Service Commande ne peut pas appeler le Service Produit

**Problème:** Le client Feign lève une exception

**Solution:**
1. Assurez-vous que le Service Produit s'exécute et est enregistré avec Eureka
2. Vérifiez que l'annotation `@EnableFeignClients` est présente dans OrderServiceApplication
3. Vérifiez que l'interface ProductClient a le nom de service correct: `@FeignClient(name = "product-service")`

### 10. Exercices

#### Exercice 1 : Ajouter la fonctionnalité de mise à jour du stock
**Objectif:** Mettre à jour le stock des produits après la création d'une commande

**Étapes:**
1. Créer un point de terminaison `PUT /api/products/{id}/stock` dans le Service Produit
2. Modifier `OrderService.createOrder()` pour appeler ce point de terminaison
3. Gérer le cas où le stock est insuffisant (lever une exception)

#### Exercice 2 : Créer une API Gateway
**Objectif:** Implémenter un point d'entrée unique pour tous les services

**Étapes:**
1. Créer un nouveau projet avec la dépendance Spring Cloud Gateway
2. Configurer les routes:
   - `/products/**` → `http://localhost:8081`
   - `/orders/**` → `http://localhost:8082`
3. S'exécuter sur le port 8080
4. Test: `http://localhost:8080/products`

#### Exercice 3 : Gestion des erreurs et résilience
**Objectif:** Améliorer la gestion des erreurs

**Exigences:**
1. Gérer le cas où le produit n'existe pas
2. Gérer le cas où le Service Produit n'est pas disponible
3. Retourner les codes de statut HTTP appropriés et les messages d'erreur
4. Ajouter la journalisation pour le débogage

### 11. Technologies utilisées

| Composant | Technologie | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 4.0.5 |
| **Cloud** | Spring Cloud | 2025.1.1 |
| **Découverte de services** | Netflix Eureka | 2.0.5 |
| **Communication REST** | OpenFeign | 13.6 |
| **Base de données** | H2 | 2.4.240 |
| **ORM** | Hibernate JPA | 7.2.7 |
| **Java** | JDK | 21 |
| **Outil de construction** | Maven | 3.6+ |

### 12. Licence et crédits

**Auteur:** laznasni rayhana  
**Institution:** ENSA Marrakech  
**Cours:** TP°4 - Architecture Orientée Services (SOA)  
**Date:** 2026

---

**Last Updated:** April 2, 2026
