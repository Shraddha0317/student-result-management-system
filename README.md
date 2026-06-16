# 🎓 Student Result Management System

A production-grade REST API built with Spring Boot for managing students, subjects, marks, and result generation. Designed following enterprise-level architecture patterns including layered architecture, DTO pattern, global exception handling, and comprehensive unit testing.


---

##  Overview

The Student Result Management System is a backend REST API that allows educational institutions to:

- Manage student records and subject catalogs
- Record and update student marks per subject
- Automatically calculate grades based on percentage
- Generate consolidated result reports per student
- Bulk upload students and marks via CSV files

---

##  Features

| Feature                   |  Description                                                   |
|---------------------------|----------------------------------------------------------------|
| Student Management        | Full CRUD with pagination and sorting                          |
| Subject Management        | Full CRUD with unique subject codes                            |
| Marks Management          | Add, update, delete marks with auto grade calculation          |
| Result Generation         | Consolidated result with overall percentage and pass/fail      |
| CSV Bulk Upload           | Upload students and marks via CSV with per-row error reporting |
| Global Exception Handling | Consistent JSON error responses across all endpoints           |
| Bean Validation           | Input validation with detailed field-level error messages      |
| API Documentation         | Interactive Swagger UI at `/swagger-ui/index.html`             |
| Unit Testing              | JUnit 5 + Mockito with 85%+ service layer coverage             |
| Dockerization             | Fully containerized with Docker Compose                        |
------------------------------------------------------------------------------------------------
---

## Tech Stack

| Category               | Technology                                       |
|------------------------|--------------------------------------------------|
| Language               | Java 17                                          |
| Framework              | Spring Boot 3.x                                  |
| Database               | MySQL 8                                          |
| ORM                    | Spring Data JPA + Hibernate                      |
| Validation             | Spring Boot Starter Validation (Bean Validation) |
| Documentation          | SpringDoc OpenAPI 3 (Swagger UI)                 |
| CSV Parsing            | Apache Commons CSV                               |
| Testing                | JUnit 5 + Mockito                                |
| Build Tool             | Maven                                            |
| Containerization       | Docker + Docker Compose                          |
| Boilerplate Reduction  | Lombok                                           |
 ____________________________________________________________________________



## Architecture

This project follows a strict **Layered Architecture** pattern:


Client (Postman / Browser)
        │
        ▼
┌─────────────────────┐
│   Controller Layer  │  ← Handles HTTP, delegates to Service
│   (@RestController) │
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐     ┌──────────────────────┐
│    DTO Layer        │◄────│  Validation Layer     │
│  (Request/Response) │     │  (@Valid + @NotBlank) │
└─────────┬───────────┘     └──────────────────────┘
          │
          ▼
┌─────────────────────┐     ┌──────────────────────┐
│   Service Layer     │────►│  Utility Layer        │
│   (@Service)        │     │  (GradeCalculator)    │
│   Business Logic    │     └──────────────────────┘
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐
│  Repository Layer   │  ← Data access only, no business logic
│  (JpaRepository)    │
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐
│   MySQL Database    │  ← students, subjects, marks tables
└─────────────────────┘

Cross-cutting:
┌──────────────────────────────┐
│  Global Exception Handler    │  ← @ControllerAdvice intercepts all exceptions
│  (@RestControllerAdvice)     │
└──────────────────────────────┘


### Design Decisions

- **Package by Feature** — each domain (student, subject, marks, result) is self-contained
- **DTO Pattern** — entities never exposed directly to API consumers
- **Service Interface + Impl** — enables loose coupling and easy mocking in tests
- **Result as computed DTO** — result is never stored in DB, always calculated at runtime
- **Row-independent CSV processing** — one bad CSV row never affects other rows

---


## 🗄Database Design

### Entity Relationship

students (1) ────────── (N) marks (N) ────────── (1) subjects


### Tables
##Student table

| Column        | Data Type    | Constraints                 |
| ------------- | ------------ | --------------------------- |
| id            | BIGINT       | Primary Key, Auto Increment |
| first_name    | VARCHAR(50)  | NOT NULL                    |
| last_name     | VARCHAR(50)  | NOT NULL                    |
| roll_number   | VARCHAR(20)  | NOT NULL, UNIQUE            |
| email         | VARCHAR(100) | NOT NULL, UNIQUE            |
| date_of_birth | DATE         | Nullable                    |
| created_at    | TIMESTAMP    | NOT NULL, Auto-generated    |
| updated_at    | TIMESTAMP    | NOT NULL, Auto-updated      |

##SubjectTable

| Column       | Data Type    | Constraints                     |
| ------------ | ------------ | ------------------------------- |
| id           | BIGINT       | Primary Key, Auto Increment     |
| subject_code | VARCHAR(20)  | NOT NULL, UNIQUE                |
| subject_name | VARCHAR(100) | NOT NULL                        |
| max_marks    | INT          | NOT NULL, CHECK (max_marks > 0) |
| description  | VARCHAR(255) | Nullable                        |
| created_at   | TIMESTAMP    | NOT NULL                        |
| updated_at   | TIMESTAMP    | NOT NULL                        |

## MarksTable

| Column         | Data Type    | Constraints                           |
| -------------- | ------------ | ------------------------------------- |
| id             | BIGINT       | Primary Key, Auto Increment           |
| student_id     | BIGINT       | Foreign Key → students.id, NOT NULL   |
| subject_id     | BIGINT       | Foreign Key → subjects.id, NOT NULL   |
| marks_obtained | DECIMAL(5,2) | NOT NULL, CHECK (marks_obtained >= 0) |
| grade          | VARCHAR(2)   | Nullable                              |
| exam_date      | DATE         | Nullable                              |
| created_at     | TIMESTAMP    | NOT NULL                              |
| updated_at     | TIMESTAMP    | NOT NULL                              |

##Relationships


| Table            | Relationship                    |
| ---------------- | ------------------------------- |
| students → marks | One Student can have many Marks |
| subjects → marks | One Subject can have many Marks |
| marks → students | Many-to-One                     |
| marks → subjects | Many-to-One                     |




**Composite Unique Constraint on marks:** `UNIQUE(student_id, subject_id)`



##  API Endpoints

### Student APIs — `/api/v1/students`

| Method  | Endpoint                | Description         | Status          |
|---------|-------------------------|---------------------|-----------------|
| POST    | `/api/v1/students`      | Create student      | 201 / 400 / 409 |
| GET     | `/api/v1/students`      | Get all (paginated) | 200             |
| GET     | `/api/v1/students/{id}` | Get by ID           | 200 / 404       |
| PUT     | `/api/v1/students/{id}` | Update student      | 200 / 404 / 409 |
| DELETE  | `/api/v1/students/{id}` | Delete student      | 204 / 404       |

**Pagination Parameters:** `?page=0&size=10&sortBy=lastName&sortDir=asc`

### Subject APIs — `/api/v1/subjects`

| Method  | Endpoint                | Description         | Status          |
|---------|-------------------------|---------------------|-----------------|
| POST    | `/api/v1/subjects`      | Create subject      | 201 / 400 / 409 |
| GET     | `/api/v1/subjects`      | Get all (paginated) | 200             |
| GET     | `/api/v1/subjects/{id}` | Get by ID           | 200 / 404       |
| PUT     | `/api/v1/subjects/{id}` | Update subject      | 200 / 404       |
| DELETE  | `/api/v1/subjects/{id}` | Delete subject      | 204 / 404       |

### Marks APIs — `/api/v1/marks`

| Method   | Endpoint                            | Description           | Status                |
|----------|-------------------------------------|-----------------------|-----------------------|
| POST     | `/api/v1/marks`                     | Add marks             | 201 / 400 / 404 / 409 |
| GET      | `/api/v1/marks/{id}`                | Get by ID             | 200 / 404             |
| GET      | `/api/v1/marks/student/{studentId}` | All marks for student | 200 / 404             |
| GET      | `/api/v1/marks/subject/{subjectId}` | All marks for subject | 200 / 404             |
| PUT      | `/api/v1/marks/{id}`                | Update marks          | 200 / 400 / 404       |
| DELETE   | `/api/v1/marks/{id}`                | Delete marks          | 204 / 404             |

### Result APIs — `/api/v1/results`

| Method  | Endpoint                              | Description                 | Status    |
|---------|---------------------------------------|-----------------------------|-----------|
| GET     | `/api/v1/results/student/{studentId}` | Full result for one student | 200 / 404 |
| GET     | `/api/v1/results`                     | Results for all students    | 200       |

### CSV Upload APIs — `/api/v1/csv`

| Method  | Endpoint               | Description          | Status    |
|---------|------------------------|----------------------|-----------|
| POST    | `/api/v1/csv/students` | Bulk upload students | 200 / 400 |
| POST    | `/api/v1/csv/marks`    | Bulk upload marks    | 200 / 400 |

---

##  Grade Calculation Logic

| Percentage   | Grade  |
|--------------|--------|
| >= 90%       | A+     |
| >= 80%       | A      |
| >= 70%       | B      |
| >= 60%       | C      |
| >= 50%       | D      |
| < 50%        | F      |

**Pass/Fail Rule:**
- Overall percentage must be >= 40%
- AND every individual subject must be >= 40%
- If either condition fails → **FAIL**

---

##  Getting Started

### Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven 3.8+
- MySQL 8+
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/student-result-management-system.git
cd student-result-management-system
```

### 2. Create the Database

```sql
CREATE DATABASE student_result_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Application Properties

Edit `src/main/resources/application-dev.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_result_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### 4. Run the Application

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The application starts at: `http://localhost:8080`

### 5. Verify

Open in browser:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 🐳 Running with Docker

### Prerequisites

- Docker Desktop installed and running

### 1. Build the JAR

```bash
mvn clean package -DskipTests
```

### 2. Start with Docker Compose

```bash
docker-compose up --build
```

This starts:
- **MySQL 8** container on port `3306`
- **Spring Boot app** container on port `8080`

### 3. Stop

```bash
docker-compose down
```

To remove volumes (wipe DB data):
```bash
docker-compose down -v
```

---

## 🧪 Running Tests

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=StudentServiceTest
mvn test -Dtest=GradeCalculatorTest
```



Open coverage report:
```
target/site/jacoco/index.html
```

### Test Summary

| Test Class            | Tests   | Covers                                      |
|-----------------------|---------|---------------------------------------------|
| GradeCalculatorTest   | 13      | All grade boundaries, edge cases            |
| StudentServiceTest    | 10      | CRUD, duplicate checks, 404 handling        |
| MarksServiceTest      | 8       | FK validation, marks exceed max, duplicates |
| StudentControllerTest | 7       | HTTP status codes, JSON responses           |
| **Total**             | **38**  | **85%+ service layer coverage**             |

---

##  API Documentation

After starting the application, access:

| URL                                           | Description            |
|-----------------------------------------------|------------------------|
| `http://localhost:8080/swagger-ui/index.html` | Interactive Swagger UI |
| `http://localhost:8080/api-docs`              | Raw OpenAPI JSON spec  |

The Swagger UI allows you to:
- Browse all 17 endpoints organized by domain
- View request/response schemas
- Execute API calls directly from the browser
- See all possible response codes per endpoint

---

##  CSV Upload Format

### students.csv

csv
firstName,lastName,email,rollNumber,dateOfBirth
Raj,Kumar,raj@gmail.com,CS001,2000-05-15
Priya,Sharma,priya@gmail.com,CS002,2001-03-20


**Rules:**
- Header row required with exact column names
- `dateOfBirth` format: `yyyy-MM-dd` (optional column)
- Duplicate emails or roll numbers are reported as errors, not failures

### marks.csv


rollNumber,subjectCode,marksObtained,examDate
CS001,SPRING101,85.0,2026-04-10
CS001,JAVA101,90.0,2026-04-10


**Rules:**
- Uses `rollNumber` and `subjectCode` — not database IDs
- `examDate` format: `yyyy-MM-dd` (optional)
- `marksObtained` must not exceed subject's `maxMarks`

### Upload Response

```json
{
  "totalRows": 5,
  "successCount": 3,
  "failureCount": 2,
  "errors": [
    {
      "rowNumber": 4,
      "data": "...",
      "reason": "Email already exists: raj@gmail.com"
    }
  ]
}
```

---

## ⚠️ Error Handling

All errors return a consistent JSON structure:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id: 99",
  "path": "/api/v1/students/99",
  "timestamp": "2026-06-16T10:30:00",
  "errors": null
}
```

For validation errors, `errors` contains a field-level map:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/students",
  "timestamp": "2026-06-16T10:30:00",
  "errors": {
    "firstName": "First name is required",
    "email": "Email must be a valid email address"
  }
}
```

| HTTP Status               | When                                         |
|---------------------------|----------------------------------------------|
| 200 OK                    | Successful GET or PUT                        |
| 201 Created               | Successful POST                              |
| 204 No Content            | Successful DELETE                            |
| 400 Bad Request           | Validation failed or invalid input           |
| 404 Not Found             | Resource does not exist                      |
| 409 Conflict              | Duplicate email, roll number, or marks entry |
| 500 Internal Server Error | Unexpected server error                      |

---

## 👩‍💻 Author

**Shradha**
- Built as a learning project 
- Designed with production-grade patterns: layered architecture, DTO pattern,
  global exception handling, unit testing, Docker

---

## 📜 License

This project is licensed under the MIT License.