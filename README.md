Access Governance Manager

📌 Project Overview

Access Governance Manager is a Spring Boot backend application used to manage users, roles, tasks, reminders, and access control within an organization.

The project includes:

- User Management
- Task Management
- Email Notifications
- Redis Caching
- Docker Integration
- MySQL Database
- JWT Authentication

---

🏗️ Architecture Diagram

Client
   ↓
Spring Boot Backend
   ↓
MySQL Database
   ↓
Redis Cache
   ↓
Docker Containers

---

⚙️ Technologies Used

- Java 17
- Spring Boot
- Spring Security
- MySQL
- Redis
- Docker
- Maven

---

✅ Prerequisites

Before running the project, install:

- Java 17
- Maven
- Docker Desktop
- MySQL Workbench
- Git

---

🚀 Setup Steps

1 Clone Repository

git clone <repository-url>

2 Navigate to Project

cd access-governance-manager

3 Build Project

mvn clean package

4 Run Docker Containers

docker compose up --build

5 Access Application

Backend:

http://localhost:8080

phpMyAdmin:

http://localhost:8081

---

🔐 Environment Reference

Variable| Description
DB_URL| MySQL database URL
DB_USERNAME| Database username
DB_PASSWORD| Database password
JWT_SECRET| JWT secret key
REDIS_HOST| Redis hostname
REDIS_PORT| Redis port

---

📬 Features

- User Authentication
- Role Based Access
- Task Assignment
- Scheduled Email Reminders
- Deadline Alerts
- Overdue Notifications
- Dockerized Deployment

---

👨‍💻 Author

Prethi S K