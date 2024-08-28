# 📚 Library Management System API

The **Library Management System API** is a Spring Boot application that provides a comprehensive backend solution for managing books, patrons, and borrowing records in a library. This project demonstrates a range of capabilities, including RESTful API development, data persistence, and optional features like security and caching.

## 🌟 Features

- **Manage Books**: 
  - Retrieve all books or specific books by ID.
  - Add new books, update existing ones, or remove them from the catalog.
- **Manage Patrons**: 
  - View all library patrons or specific patrons by ID.
  - Add new patrons, update their details, or remove them from the system.
- **Borrowing Records**: 
  - Track which books have been borrowed by which patrons.
  - Record the borrowing and returning of books.

### Enhancements
- **Security**: API endpoints protected with basic authentication.
- **Logging**: Monitor key actions and performance with Aspect-Oriented Programming (AOP).
- **Caching**: Improved performance by caching frequently accessed data.
- **Transaction Management**: Ensuring data integrity during critical operations.

## 🛠️ Getting Started

### Prerequisites
- **Java 22**
- **Maven**
- **MySQL** (or any other relational database)-created with H2

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/ibrahimGabr977/online-library.git
   cd online-library
   ```
2. **Configure the database**:
   - Update `src/main/resources/application.properties` with your database credentials.
   - Example:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/library_db
     spring.datasource.username=root
     spring.datasource.password=yourpassword
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build and Run the Application**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the API**:
   - The API is accessible at `http://localhost:8080/api/`.
   - Use tools like Postman to interact with the API endpoints.

### Example API Calls

- **Retrieve all books**:
  ```
  GET /api/books
  ```
- **Add a new book**:
  ```
  POST /api/books
  ```

## 🧪 Testing

The project includes unit tests to validate the functionality of the API. Run the tests using:
```bash
mvn test
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Ibrahim Gabr**

- GitHub: [@ibrahimGabr977](https://github.com/ibrahimGabr977)
