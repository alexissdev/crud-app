# 🔐 Spring Boot JWT Authentication API

Production-ready REST API built with **Spring Boot** implementing a complete **JWT-based authentication and authorization system**, including:

* ✅ JWT Login
* ✅ Access Token & Refresh Token
* ✅ Role-based Authorization
* ✅ Docker + MySQL
* ✅ Global Exception Handling (`@ControllerAdvice`)
* ✅ `SecurityFilterChain` Configuration
* ✅ Token Refresh Flow
* ✅ Clean `User` → `UserDetails` Mapping

---

## 🚀 Tech Stack

* Java 17+
* Spring Boot 3+
* Spring Security
* JWT (JJWT)
* MySQL
* Docker & Docker Compose
* Lombok
* gradle

---

## 🔑 JWT Authentication

### 📌 Token Types

| Token         | Lifetime      | Purpose                    |
| ------------- | ------------- | -------------------------- |
| Access Token  | 10–15 minutes | Access protected endpoints |
| Refresh Token | 7–30 days     | Generate new access tokens |

---

## 🔓 Authentication Endpoints

### ✅ Login

```
POST /api/auth/login
```

**Response:**

```json
{
  "accessToken": "...",
  "refreshToken": "..."
}
```

---

### ✅ Refresh Token

```
POST /api/auth/refresh
```

**Request Body:**

```json
{
  "refreshToken": "..."
}
```

**Response:**

```json
{
  "accessToken": "new-access-token"
}
```

---

## 🧠 Security Architecture

### 🔐 JwtService

Service responsible for:

* Generating Access Tokens
* Generating Refresh Tokens
* Extracting username from token
* Validating token signature and expiration

---

### 🧩 UserDetailsFactory

Utility class that converts the `User` entity into a `UserDetails` instance:

```java
public class UserDetailsFactory {

    public static UserDetails create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
```

---

## 🔁 Refresh Token Flow

1. User logs in
2. Receives Access Token + Refresh Token
3. Access Token expires
4. Frontend sends Refresh Token
5. Backend validates Refresh Token
6. New Access Token is generated ✅

---

## 🧱 Role-Based Authorization

* Roles are mapped to `GrantedAuthority`
* Roles are included as `claims` inside JWT
* Access rules defined using:

```java
.hasAuthority("ADMIN")
```

---

## 🐳 Docker

The project uses:

* MySQL container
* Backend connected via `application.properties`

---

## ⚠️ Global Exception Handling

Implemented using `@ControllerAdvice` for:

* Validation errors
* Authentication errors
* Resource not found
* Custom business exceptions

---

## ✅ Best Practices Applied

* Layered architecture
* DTOs for requests and responses
* Stateless security
* Decoupled role management
* Token expiration control
* Entity to `UserDetails` mapping

---


## ✅ Project Status

✔️ Production-ready
✔️ Complete security layer
✔️ Clean architecture
✔️ Ready for deployment

---

## 👨‍💻 Author

Developed as part of an advanced backend learning process using Spring Boot.

---

🔥 This project follows real-world backend security standards and practices.
