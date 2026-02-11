# User Service

A Spring Boot RESTful microservice for managing user information with PostgreSQL database and Flyway for database migrations.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Technologies Used](#technologies-used)
- [Building & Running](#building--running)
- [Notes & Improvements](#notes--improvements)

## Overview

This is a microservice that provides a complete CRUD (Create, Read, Update, Delete) API for managing user information. It follows a 3-tier architecture with clear separation of concerns between controllers, services, and data access layers.

**Key Features:**
- RESTful API endpoints for user management
- PostgreSQL database integration
- Automated database migrations using Flyway
- Exception handling and error reporting
- Lombok for reduced boilerplate code
- Named parameter SQL queries for SQL injection prevention

## Architecture

```
┌─────────────────┐
│   Rest Client   │
└────────┬────────┘
         │
         ↓
┌─────────────────────────────────────┐
│   UserController (Web Layer)        │  - REST Endpoints
│   @RestController                   │  - Request/Response Handling
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│   UserService (Business Logic)      │  - Business Rules
│   @Service                          │  - Validation
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│   UserDao (Data Access)             │  - Database Queries
│   @Repository                       │  - SQL Execution
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│   PostgreSQL Database               │  - Data Persistence
│   (ts.user_info table)              │
└─────────────────────────────────────┘
```

## Prerequisites

Before running this project, ensure you have installed:

1. **Java Development Kit (JDK)** - Version 11 or higher
2. **Apache Maven** - Version 3.6.0 or higher
3. **PostgreSQL** - Version 12 or higher
4. **Git** (optional, for version control)

### Verify Installations

```bash
java -version
mvn -version
psql --version
```

## Project Structure

```
user-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/user_service/
│   │   │       ├── UserServiceApplication.java    # Spring Boot entry point
│   │   │       ├── config/
│   │   │       │   └── WebConfig.java             # Database configuration
│   │   │       ├── controller/
│   │   │       │   └── UserController.java        # REST endpoints
│   │   │       ├── service/
│   │   │       │   └── UserService.java           # Business logic
│   │   │       ├── dao/
│   │   │       │   ├── UserDao.java               # Database operations
│   │   │       │   └── mappers/
│   │   │       │       └── UserMapper.java        # ResultSet to Object mapping
│   │   │       └── model/
│   │   │           └── User.java                  # User POJO
│   │   └── resources/
│   │       ├── application.properties             # Application configuration
│   │       └── db/migration/
│   │           ├── V1__create_user_table.sql      # Initial schema creation
│   │           └── V1_1__insert_users.sql         # Sample data insertion
│   └── test/
│       └── java/com/user_service/demo/
│           └── DemoApplicationTests.java          # Unit tests
├── pom.xml                                        # Maven dependencies and build config
├── mvnw                                           # Maven wrapper (Unix/Linux/Mac)
├── mvnw.cmd                                       # Maven wrapper (Windows)
└── README.md                                      # This file
```

## Getting Started

### 1. Clone the Repository

```bash
# Via HTTPS
git clone https://github.com/your-username/user-service.git
cd user-service

# Or download the project directly
```

### 2. Setup PostgreSQL Database

Create a new PostgreSQL database:

```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE user_service_db;

-- Verify creation
\l
```

### 3. Configure Application

Edit `src/main/resources/application.properties` with your PostgreSQL connection details:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/user_service_db?currentSchema=ts
spring.datasource.username=postgres
spring.datasource.password=admin
```

**Note:** Change `admin` to your PostgreSQL password if different.

### 4. Build the Project

```bash
# Using Maven wrapper (Windows)
mvnw.cmd clean install

# Using Maven wrapper (Unix/Linux/Mac)
./mvnw clean install

# Or using installed Maven
mvn clean install
```

### 5. Run the Application

```bash
# Using Maven wrapper (Windows)
mvnw.cmd spring-boot:run

# Using Maven wrapper (Unix/Linux/Mac)
./mvnw spring-boot:run

# Or run the JAR file
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

**Expected Output:**
```
...
Started UserServiceApplication in X.XXX seconds (JVM running for X.XXX)
```

The application will start on `http://localhost:8080` by default.

## Configuration

### Application Properties

| Property | Description | Default Value |
|----------|-------------|----------------|
| `spring.application.name` | Application name | `user-service` |
| `spring.datasource.url` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/user_service_db?currentSchema=ts` |
| `spring.datasource.username` | Database username | `postgres` |
| `spring.datasource.password` | Database password | `admin` |
| `spring.jpa.show-sql` | Show SQL in logs | `true` |
| `spring.flyway.enabled` | Enable database migrations | `true` |
| `spring.flyway.locations` | Migration scripts location | `classpath:db/migration` |

### Custom Configuration Files

- **WebConfig.java**: Defines custom DataSource and JdbcTemplate beans for the user database

## API Endpoints

### Base URL
```
http://localhost:8080
```

### Endpoints

#### 1. Get Greeting
```http
GET /greet
```
**Description:** Returns a greeting message  
**Response:**
```json
"Hello, user Dao!"
```

---

#### 2. Get Name
```http
GET /enter-name
```
**Description:** Returns a hardcoded name  
**Response:**
```json
"Tiara Eshwara"
```

---

#### 3. Get All Users
```http
GET /getusers
```
**Description:** Retrieve all users from the database  
**Response:**
```json
[
  {
    "firstName": "Tiara",
    "lastName": "Eshwara",
    "age": 25,
    "gender": "Female",
    "city": "New York",
    "email": "tiaraeshwara@gmail.com",
    "phoneNumber": "0710780080"
  }
]
```

---

#### 4. Get User by ID
```http
GET /getuserbyid/{id}
```
**Parameters:**
- `id` (path parameter): User ID (Integer)

**Example:**
```http
GET /getuserbyid/1
```

**Response:**
```json
{
  "firstName": "Tiara",
  "lastName": "Eshwara",
  "age": 25,
  "gender": "Female",
  "city": "New York",
  "email": "tiaraeshwara@gmail.com",
  "phoneNumber": "0710780080"
}
```

---

#### 5. Get User by Email
```http
GET /getuserbyemail/{email}
```
**Parameters:**
- `email` (path parameter): User email address (String)

**Example:**
```http
GET /getuserbyemail/tiaraeshwara@gmail.com
```

**Response:**
```json
{
  "firstName": "Tiara",
  "lastName": "Eshwara",
  "age": 25,
  "gender": "Female",
  "city": "New York",
  "email": "tiaraeshwara@gmail.com",
  "phoneNumber": "0710780080"
}
```

---

#### 6. Create User
```http
POST /add-user
Content-Type: application/json
```
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "age": 30,
  "gender": "Male",
  "city": "San Francisco",
  "email": "john.doe@example.com",
  "phoneNumber": "5551234567"
}
```

**Response:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "age": 30,
  "gender": "Male",
  "city": "San Francisco",
  "email": "john.doe@example.com",
  "phoneNumber": "5551234567"
}
```

---

#### 7. Update User
```http
PUT /update-user/{id}
Content-Type: application/json
```
**Parameters:**
- `id` (path parameter): User ID to update (Integer)

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "age": 28,
  "gender": "Female",
  "city": "Los Angeles",
  "email": "jane.smith@example.com",
  "phoneNumber": "5559876543"
}
```

**Response:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "age": 28,
  "gender": "Female",
  "city": "Los Angeles",
  "email": "jane.smith@example.com",
  "phoneNumber": "5559876543"
}
```

---

#### 8. Delete User
```http
DELETE /delete-user/{id}
```
**Parameters:**
- `id` (path parameter): User ID to delete (Integer)

**Example:**
```http
DELETE /delete-user/1
```

**Response (Success):**
```json
"User deleted"
```

**Response (Not Found):**
```json
"User not found"
```

---

### Testing Endpoints with cURL

```bash
# Get all users
curl http://localhost:8080/getusers

# Get user by ID
curl http://localhost:8080/getuserbyid/1

# Get user by email
curl http://localhost:8080/getuserbyemail/tiaraeshwara@gmail.com

# Create user
curl -X POST http://localhost:8080/add-user \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","age":30,"gender":"Male","city":"NYC","email":"john@example.com","phoneNumber":"1234567890"}'

# Update user
curl -X PUT http://localhost:8080/update-user/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","age":28,"gender":"Female","city":"NYC","email":"jane@example.com","phoneNumber":"0987654321"}'

# Delete user
curl -X DELETE http://localhost:8080/delete-user/1
```

## Database Schema

### Schema: `ts`

The database uses a custom schema named `ts` for organization.

### Table: `user_info`

```sql
CREATE TABLE ts.user_info (
    UserID INT NOT NULL PRIMARY KEY,
    LastName VARCHAR(255) NOT NULL,
    FirstName VARCHAR(255),
    Age INT,
    Gender VARCHAR(10),
    City VARCHAR(255),
    PhoneNumber VARCHAR(15),
    Email VARCHAR(255) UNIQUE
);
```

**Column Descriptions:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `UserID` | INT | PRIMARY KEY, NOT NULL | Unique user identifier |
| `FirstName` | VARCHAR(255) | | User's first name |
| `LastName` | VARCHAR(255) | NOT NULL | User's last name (required) |
| `Age` | INT | | User's age |
| `Gender` | VARCHAR(10) | | User's gender |
| `City` | VARCHAR(255) | | User's city |
| `PhoneNumber` | VARCHAR(15) | | User's phone number |
| `Email` | VARCHAR(255) | UNIQUE | User's email (must be unique) |

### Database Migrations (Flyway)

The project uses Flyway for automated database migrations:

- **V1__create_user_table.sql**: Creates the `ts` schema and `user_info` table
- **V1_1__insert_users.sql**: Inserts sample user data

Migrations are automatically executed on application startup.

## Technologies Used

```
Spring Boot           - 2.x (Latest stable)
Spring Framework      - Web, JDBC support
PostgreSQL           - Version 12+
Apache Maven         - Build automation
Lombok               - Boilerplate code reduction
Flyway               - Database version control
JDK                  - Java 11+
```

### Maven Dependencies

Key dependencies in `pom.xml`:
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-jdbc` - JDBC template support
- `postgresql` - PostgreSQL JDBC driver
- `flyway-core` - Database migrations
- `lombok` - Annotation-driven code generation

## Building & Running

### Build Phases

```bash
# Clean previous builds
mvn clean

# Download dependencies and compile
mvn compile

# Run tests
mvn test

# Package to JAR
mvn package

# Install to local Maven repository
mvn install

# All in one command
mvn clean install
```

### Running Modes

**Development (Hot reload with Maven):**
```bash
./mvnw spring-boot:run
```

**Production (JAR file):**
```bash
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

**Custom JVM Parameters:**
```bash
java -Xmx512m -Xms256m -jar target/user-service-0.0.1-SNAPSHOT.jar
```

### IDE Setup

**IntelliJ IDEA:**
1. Open project → File → Open → Select `pom.xml`
2. Import as Maven project
3. Wait for dependency download
4. Run → Edit Configurations → Add Maven
5. Command: `spring-boot:run`

**Eclipse:**
1. File → Import → Existing Maven Projects
2. Select project folder
3. Maven → Update Project
4. Run As → Maven Build → Goal: `spring-boot:run`

**VS Code:**
1. Install "Extension Pack for Java"
2. Open project folder
3. Run and Debug (F5) → Select "Java"

## Notes & Improvements

### Current Implementation Issues

⚠️ **Random UserID Generation**
```java
parameters.addValue("userId", new Random().nextInt(Integer.MAX_VALUE));
```
**Issue:** Can cause ID collisions  
**Solution:** Use auto-increment PRIMARY KEY or UUID

⚠️ **Missing Input Validation**
**Issue:** No validation for user input (null checks, format validation)  
**Solution:** Add `@Valid` annotation and Bean Validation constraints

⚠️ **Error Response Format**
**Issue:** Error handling wraps all exceptions generically  
**Solution:** Implement custom `@ExceptionHandler` with standard error response format

### Recommended Improvements

1. **Use UUID for Primary Key**
   ```java
   import java.util.UUID;
   parameters.addValue("userId", UUID.randomUUID());
   ```

2. **Add Bean Validation**
   ```java
   @Email
   private String email;
   
   @NotBlank
   private String firstName;
   ```

3. **Implement Global Exception Handler**
   ```java
   @RestControllerAdvice
   public class GlobalExceptionHandler {
       @ExceptionHandler(DataAccessException.class)
       public ResponseEntity<?> handleDatabaseError(DataAccessException e) {
           // Return standardized error response
       }
   }
   ```

4. **Add Logging**
   ```java
   import lombok.extern.slf4j.Slf4j;
   
   @Slf4j
   public class UserDao {
       log.info("Fetching user with id: {}", id);
   }
   ```

5. **Add Unit Tests**
   - Test controller endpoints with MockMvc
   - Test service layer with mocked DAO
   - Test database operations with TestContainers

6. **Add API Documentation (Swagger/OpenAPI)**
   ```java
   @GetMapping("/getusers")
   @Operation(summary = "Get all users")
   public List<User> getUsers() { ... }
   ```

## Troubleshooting

### Connection Refused Error
```
Connection refused: localhost:5432
```
**Solution:** Ensure PostgreSQL is running
```bash
# Windows
net start PostgreSQL-14

# Linux/Mac
sudo service postgresql start
```

### Authentication Failed
```
FATAL: password authentication failed for user "postgres"
```
**Solution:** Check credentials in `application.properties`

### Flyway Migration Error
```
Flyway.FlywayException: Unable to resolve location classpath:db/migration
```
**Solution:** Ensure migration files are in `src/main/resources/db/migration` folder

### Port Already in Use
```
Address already in use: bind
```
**Solution:** Change port in `application.properties`
```properties
server.port=8081
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact & Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Contact: your-email@example.com

---

**Last Updated:** February 2026  
**Version:** 1.0.0
