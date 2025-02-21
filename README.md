# Spring Boot Imgur Integration Application

This is a Spring Boot REST application that demonstrates user registration, JWT-based authentication, and integration with the Imgur API for image upload, retrieval, and deletion. The application uses an H2 in-memory database with JPA for data persistence and follows a clean, three-layer architecture (Controller, Service, Repository).

## Features

- **User Registration & Login:**\
  Users can register and log in. Passwords are encoded using BCrypt.

- **JWT Authentication:**\
  The application uses JSON Web Tokens (JWT) to secure endpoints. Only authenticated users can access protected resources.

- **Image Upload, Retrieval, and Deletion:**

  - **Upload:** Send a Base64-encoded image to be uploaded to Imgur. The response from Imgur (including the image URL, ID, and delete hash) is stored in the H2 database and associated with the user.
  - **Retrieval:** Retrieve all images associated with a user.
  - **Deletion:** Delete an image both from Imgur and from the local database using the stored delete hash.

- **Externalized Secrets:**\
  Sensitive configuration (e.g., Imgur client ID/secret, JWT secret) is externalized via an external properties file (`application-secrets.properties`), which is ignored in Git.

## Prerequisites

- **Java 21** (or later)
- **Maven** for building the project
- **Git** for version control
- An **Imgur account** and registered Imgur application to obtain your client ID and secret

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/rpeloso21/API-Engineer-Challenge-2.git
cd API Engineer Challenge 2
```

### 2. Configure Secrets

Create a file named `application-secrets.properties` in the project root (this file is excluded from Git):

```properties
# Fill in your credentials.
imgur.client.id=YOUR_IMGUR_CLIENT_ID
imgur.client.secret=YOUR_IMGUR_CLIENT_SECRET
```

### 3. Build the Project

Run the following command to build the project:

```bash
mvn clean install
```

### 4. Run the Application

Start the application using the Maven wrapper:

```bash
./mvnw spring-boot:run
```

The application will start on port **8080**. The H2 console is available at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:testdb`).

## API Endpoints

### User Endpoints

- **Register User**\
  `POST /api/users/register`\
  **Body (JSON):**

  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```

  Registers a new user with an encoded password.

- **Login**\
  `POST /api/users/login`\
  **Body (JSON):**

  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```

  Authenticates the user and returns a JWT token.

- **Get User Details**\
  `GET /api/users/{userId}`\
  Retrieves user details (including associated images).\
  **Authorization:**\
  Include header `Authorization: Bearer <JWT_TOKEN>`

### Image Endpoints

- **Upload Image**\
  `POST /api/images/upload/{userId}`\
  **Body (JSON):**

  ```json
  {
    "base64Image": "BASE64_ENCODED_IMAGE_STRING"
  }
  ```

  Uploads the image to Imgur, stores the returned image info in H2, and associates it with the user.\
  **Authorization:**\
  Include header `Authorization: Bearer <JWT_TOKEN>`

- **Get User Images**\
  `GET /api/images/user/{userId}`\
  Retrieves all images associated with the specified user.\
  **Authorization:**\
  Include header `Authorization: Bearer <JWT_TOKEN>`

- **Delete Image**\
  `DELETE /api/images/{imageId}`\
  Deletes the image from Imgur (using the stored delete hash) and removes it from H2.\
  **Authorization:**\
  Include header `Authorization: Bearer <JWT_TOKEN>`

## Testing with Postman

1. **Register a User:**

   - Method: `POST`
   - URL: `http://localhost:8080/api/users/register`
   - Body (raw JSON):
     ```json
     {
       "username": "testuser",
       "password": "password123"
     }
     ```

2. **Login:**

   - Method: `POST`
   - URL: `http://localhost:8080/api/users/login`
   - Body (raw JSON):
     ```json
     {
       "username": "testuser",
       "password": "password123"
     }
     ```
   - Copy the returned JWT token.

3. **Access a Protected Endpoint:**

   - For GET/DELETE endpoints, add the header:\
     `Authorization: Bearer <your_jwt_token>`

## Security Configuration

- The application uses Spring Security with JWT. All endpoints except `/api/users/register` and `/api/users/login` require authentication.
- The JWT filter intercepts requests and validates the token.
- For more details, see the `SecurityConfig.java` and `JwtAuthenticationFilter.java` files.




