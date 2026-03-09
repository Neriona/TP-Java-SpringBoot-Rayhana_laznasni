 # TP2 : Restructuration en Monolithe Modulaire avec MapStruct

## 📋 Description

Ce TP consiste à transformer l'application monolithique simple du TP1 en un **monolithe modulaire** bien structuré. L'objectif est d'appliquer les bonnes pratiques d'architecture logicielle en introduisant les **DTOs**, **MapStruct**, et une **séparation claire en modules** (Product, Customer, Order).

## 🎯 Objectifs

- Restructurer le code en modules indépendants
- Introduire le pattern **DTO** (Data Transfer Object)
- Utiliser **MapStruct** pour le mapping automatique Entity ↔ DTO
- Appliquer le principe d'**interface/implémentation** pour les services
- Assurer un **couplage faible** entre les modules

## 🏗️ Architecture

```
com.ecommerce.monolith
│
├── product/              ← Module indépendant
│   ├── controller/
│   │   └── ProductController.java
│   ├── dto/
│   │   ├── ProductDTO.java
│   │   └── CreateProductRequest.java
│   ├── mapper/
│   │   └── ProductMapper.java
│   ├── model/
│   │   └── Product.java
│   ├── repository/
│   │   └── ProductRepository.java
│   └── service/
│       ├── ProductService.java (interface)
│       └── ProductServiceImpl.java
│
├── customer/             ← Module indépendant
│   ├── controller/
│   │   └── CustomerController.java
│   ├── dto/
│   │   ├── CustomerDTO.java
│   │   └── CreateCustomerRequest.java
│   ├── mapper/
│   │   └── CustomerMapper.java
│   ├── model/
│   │   └── Customer.java
│   ├── repository/
│   │   └── CustomerRepository.java
│   └── service/
│       ├── CustomerService.java (interface)
│       └── CustomerServiceImpl.java
│
├── order/                ← Dépend de Product & Customer (via interfaces)
│   ├── controller/
│   │   └── OrderController.java
│   ├── dto/
│   │   ├── OrderDTO.java
│   │   ├── OrderItemDTO.java
│   │   ├── CreateOrderRequest.java
│   │   └── CreateOrderItemRequest.java
│   ├── mapper/
│   │   └── OrderMapper.java
│   ├── model/
│   │   ├── Order.java
│   │   └── OrderItem.java
│   ├── repository/
│   │   └── OrderRepository.java
│   └── service/
│       ├── OrderService.java (interface)
│       └── OrderServiceImpl.java
│
└── MonolithApplication.java
```

## 📐 Diagramme des dépendances

```
┌──────────────┐     ┌──────────────┐
│   Product    │     │   Customer   │
│   Module     │     │   Module     │
│              │     │              │
│ (indépendant)│     │ (indépendant)│
└──────┬───────┘     └──────┬───────┘
       │                    │
       │   via interfaces   │
       └────────┐  ┌────────┘
                │  │
         ┌──────▼──▼──────┐
         │     Order      │
         │     Module     │
         │                │
         │ Utilise :      │
         │ - ProductService│
         │ - CustomerService│
         └────────────────┘
```

## 🛠️ Technologies utilisées

| Technologie | Version | Rôle |
|---|---|---|
| Java | 17+ | Langage principal |
| Spring Boot | 3.x | Framework backend |
| Spring Data JPA | - | Accès aux données |
| MapStruct | 1.5.5 | Mapping Entity ↔ DTO |
| Lombok | - | Réduction du boilerplate |
| H2 Database | - | Base de données en mémoire |
| Maven | - | Gestion des dépendances |

## 🚀 Endpoints API

### Products (`/api/products`)
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/products` | Liste tous les produits |
| GET | `/api/products/{id}` | Récupère un produit par ID |
| POST | `/api/products` | Crée un nouveau produit |
| PUT | `/api/products/{id}` | Met à jour un produit |
| DELETE | `/api/products/{id}` | Supprime un produit |

### Customers (`/api/customers`)
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/customers` | Liste tous les clients |
| GET | `/api/customers/{id}` | Récupère un client par ID |
| POST | `/api/customers` | Crée un nouveau client |
| PUT | `/api/customers/{id}` | Met à jour un client |
| DELETE | `/api/customers/{id}` | Supprime un client |

### Orders (`/api/orders`)
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/orders` | Liste toutes les commandes |
| GET | `/api/orders/{id}` | Récupère une commande par ID |
| POST | `/api/orders` | Crée une nouvelle commande |
| DELETE | `/api/orders/{id}` | Supprime une commande |

## ▶️ Lancer le projet

```bash
cd monolith
./mvnw spring-boot:run
```

L'application sera accessible sur : `http://localhost:8080`

## 🧪 Exemples de requêtes

### Créer un produit
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","description":"PC Portable","price":5000,"stock":10}'
```

### Créer un client
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Rayhana","email":"rayhana@ensa.ma","phone":"0600000000","address":"Beni Mellal"}'
```

### Créer une commande
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"items":[{"productId":1,"quantity":2}]}'
```

## 👩‍💻 Auteur

- **Rayhana Laznasni**
- ENSA Beni Mellal
- Module : Architecture des Systèmes Distribués
