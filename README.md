# FeedFlow

FeedFlow is a platform designed for managing posts and file storage. It provides user authentication using OAuth2 and JWT, allowing users to securely interact with their content.

## Project Structure

This project follows a hexagonal (ports and adapters) architecture, promoting a separation of concerns and making the application more maintainable and testable. The project is divided into the following modules:

- **`domain`**: Contains the core business logic, entities, and interfaces (ports). This module is independent of any specific technology or framework.
- **`application`**: Orchestrates the use cases of the application. It uses the domain models and ports, and defines its own ports for inbound requests (e.g., services called by controllers) and outbound operations (e.g., repository interfaces implemented by the infrastructure layer).
- **`api`**: Provides the entry points to the application, typically RESTful APIs. It handles incoming HTTP requests, authentication, and delegates actions to the application services.
- **`infrastructure`**: Implements the interfaces (adapters) defined in the domain and application layers. This includes database repositories, integrations with external services like Minio for file storage, and other technology-specific concerns.

## Building and Running the Project

This is a Gradle-based project.

### Prerequisites

- Java Development Kit (JDK) - version 17 or higher
- Docker (for running dependencies like Minio)

### Build

To build the project, run the following command from the root directory:

```bash
./gradlew build
```

### Running the Application

You can run the Spring Boot application using:

```bash
./gradlew bootRun
```

Alternatively, you can run the application and its dependencies (like Minio) using Docker Compose:

```bash
docker-compose up -d
```
This will start the FeedFlow application and a Minio instance. The application will be accessible at `http://localhost:8080` by default (this may vary based on your configuration).

## Main Features

- **User Authentication**: Secure user authentication using OAuth2 (Google) and JWT.
- **Post Management**: Users can create, view, update, and delete posts.
- **File Storage**: Integration with Minio for efficient and scalable file storage associated with posts or user profiles.

## API Endpoints

The application exposes RESTful APIs for various functionalities. Key controllers include:

- **`AuthController`**: Handles user authentication and token management.
- **`PostController`**: Manages operations related to posts (create, read, update, delete).
- **`StorageController`**: Manages file uploads and access, generating presigned URLs for Minio.
- **`MemberController`**: Handles user-specific information and actions.

For detailed API documentation, refer to the OpenAPI/Swagger documentation if available, or explore the controller classes within the `api` module.

## Technologies Used

- **Programming Language**: Kotlin
- **Framework**: Spring Boot
- **Build Tool**: Gradle
- **Containerization**: Docker
- **File Storage**: Minio
- **Authentication**: JWT, OAuth2 (Google)
- **Database**: PostgreSQL