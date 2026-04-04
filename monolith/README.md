# TP1 Product Module

A Java Spring Boot application for managing products and categories, built as part of my coursework.

---

## 🚀 Features

- Manage **Products**: add, list, update, and delete products.
- Manage **Categories**: add, list, update, and delete categories.
- Products are linked to categories (many-to-one relation).
- RESTful API using Spring Boot.

---

## 🗄️ Technologies Used

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 Database *(in-memory, for development)*
- Maven

---

## 📦 Project Structure

```
monolith/
  └─ src/
      ├─ main/
      │   └─ java/
      │       └─ com.ecommerce.monolith/
      │            ├─ product/
      │            └─ category/
      └─ resources/
          └─ application.properties
```

---

## 💻 How to Run Locally

1. **Clone the repo**
    ```bash
    git clone https://github.com/Neriona/ecommerce-monolithTPSPRINGBOOT.git
    cd ecommerce-monolithTPSPRINGBOOT/monolith
    ```
2. **Build and run (with Maven)**
    ```bash
    mvn spring-boot:run
    ```

3. **Access API**
    - Visit [http://localhost:8080](http://localhost:8080)
    - Test endpoints in Postman (see examples below).

---

## 🔗 Example API Usage

### Create a Category
```http
POST /api/categories
Content-Type: application/json

{
  "name": "Périphériques"
}
```

### Create a Product
```http
POST /api/products
Content-Type: application/json

{
  "name": "Souris Gamer",
  "description": "Souris optique RGB ultra-précise",
  "price": 29.90,
  "stock": 45,
  "category": { "id": 1 }
}
```

---

## 📝 Author

- **Neriona** — [GitHub](https://github.com/Neriona)

---

## ☑️ TODO / Notes

- Add unit tests
- Improve error handling
- Add Swagger/OpenAPI documentation (optional)
