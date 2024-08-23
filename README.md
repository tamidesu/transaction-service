# Transaction Service

This is a Java-based microservice for managing financial transactions, integrating with external APIs for currency exchange rates, and managing spending limits. The service is built using Spring Boot, JPA, PostgreSQL, and Flyway for database migrations.



## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Database Migration](#database-migration)
- [Running the Service](#running-the-service)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Docker Support](#docker-support)
- [Contributing](#contributing)



## Features
- Record transactions with various currencies.
- Automatically fetch and store exchange rates from an external API.
- Enforce spending limits and flag transactions that exceed them.
- Query transactions that exceed spending limits.
- Set new monthly spending limits.
- Use Flyway for database migrations.



## Prerequisites

Before you begin, ensure you have the following installed:

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Gradle](https://gradle.org/)
- [PostgreSQL](https://www.postgresql.org/) (version 13 or above)
- [Docker](https://www.docker.com/) (optional, for containerized deployment)



## Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/transaction-service.git
   cd transaction-service

2. **Build the project:**
   If you are using Gradle:
   ```bash
   ./gradlew build

   
## Configuration
1. **Database Configuration:**
   The service uses PostgreSQL as its database. You can configure the database connection in the src/main/resources/application.properties file:


    ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/transaction_service_db
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password

Replace your_db_username and your_db_password with your actual PostgreSQL credentials.      


2. **Flyway Configuration:**
   Flyway is used for managing database migrations. Migration scripts are located in the src/main/resources/db/migration folder. The Flyway configuration is already set in the application.properties file:
   ```bash
    spring.flyway.enabled=true
    spring.flyway.locations=classpath:db/migration
    spring.flyway.baseline-on-migrate=true
   ```

   
3. **External API Configuration:**
   Configure the external API for currency exchange rates in the CurrencyExchangeService class. The API URL is set in the service:
    ```bash
    private static final String API_URL = "http://api.exchangeratesapi.io/v1/latest?access_key=your_access_key";
    ```
Replace **'your_access_key'** with your actual API access key.



## Database Migration

Flyway will automatically apply the database migration scripts on application startup. Make sure your PostgreSQL database is running and the connection details are correctly set in the **'application.properties'** file.



## Running the Service with Gradle

```bash
./gradlew bootRun
```



## API Endpoints
* **Create Transaction:**  
`'POST /api/transactions/create'`  
Example Request Body:   
```json
{
    "accountFrom": 12345,
    "accountTo": 67890,
    "currencyShortname": "USD",
    "sum": 500.00,
    "expenseCategory": "groceries"
}
```
* **Get All Transactions:**  
`'GET /api/transactions/all'`
* **Get Transaction by ID:**  
`'GET /api/transactions/{id}'`
* **Update Transaction:**  
`'PUT /api/transactions/update/{id}'`
* **Get Exchange Rate for Currency:**  
`'GET /api/transactions/exchange-rate/{currency}'`
* **Set New Monthly Limit:**  
`'POST /api/transactions/set-monthly-limit'`
* **Get Transactions Exceeding Limit:**  
`'GET /api/transactions/exceeding-limit'`



## Testing

Unit tests are provided using JUnit 5, Mockito. You can run tests with the following command:

```bash
./gradlew test
```


## Docker Support

You can containerize the application using Docker and Docker Compose. This makes it easy to deploy the service along with its dependencies.  
### Building and Running with Docker
1. **Ensure Docker and Docker Compose** are installed on your system.
2. **Build the Docker image** using the Dockerfile provided in the project:
   ```bash
   docker build -t transaction-service .
   ```
3. **Run the Docker container** using Docker Compose:


   Navigate to the directory containing the **'docker-compose.yml'** file and run:
   ```bash
   docker-compose up
   ```
This will start both the PostgreSQL database container and the transaction service container, setting up the necessary network and dependencies.

4. **Accessing the Service:**  Once the containers are up, the transaction service will be available at **'http://localhost:8080'**.
5. **Stopping the Service:**  To stop the running containers, use:
   ```bash
   docker-compose down
   ```




### Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

### Summary
- This `README.md` file provides a comprehensive guide on how to set up, configure, and run your Transaction Service.
- It includes instructions for building the project, configuring the database and external API, running the application, and using Docker.
- Ensure to adjust paths, names, and configurations according to your actual setup and project needs.
