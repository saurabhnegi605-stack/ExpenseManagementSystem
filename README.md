# Expense Management System

A Java-based console application for managing personal expenses with user authentication, MySQL persistence, validation, reporting, and automated unit testing.

The project follows a layered architecture using the **DAO and Service Layer patterns**, with Maven for dependency management and Git/GitHub for version control.

---

## Features

- User registration and login
- User-specific expense management
- Add, view, update, and delete expenses
- Expense validation
- Monthly and date-range expense reports
- Category-wise expense analysis
- Total expense calculation
- Dashboard summary
- User ownership enforcement
- Exception and input handling
- Unit testing with JUnit 5
- Mock-based testing with Mockito

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java       | Application development |
| Maven      | Build and dependency management |
| JDBC       | Database connectivity |
| MySQL      | Data persistence |
| JUnit 5    | Unit testing |
| Mockito    | Mocking dependencies |
| Git        | Version control |
| GitHub     | Source code hosting |

---

## Architecture

The application follows a layered architecture:

```text
                    Main
                     │
                     ▼
               Service Layer
                     │
                     ▼
                 DAO Layer
                     │
                     ▼
                    JDBC
                     │
                     ▼
                  MySQL
