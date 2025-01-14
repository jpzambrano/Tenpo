Compute Service

Overview

The Compute Service is a REST API built with Spring Boot that provides dynamic percentage calculations, caching, rate limiting, and a history of calculations. This API allows users to perform advanced calculations while leveraging caching and rate limiting to optimize performance. The service is fully containerized and can be deployed using Docker for scalability and ease of use.

Features

Dynamic Percentage Calculation:

Accepts two numbers and calculates their sum with an additional percentage obtained from an external service.

Example: If num1=10, num2=20, and the external service returns 10%, the result will be (10+20) + 10% = 33.

Caches the percentage for 30 minutes to reduce external service calls.

Rate Limiting:

Limits requests to 3 per minute per client IP to prevent abuse.

Returns a 429 Too Many Requests error if the limit is exceeded.

Calculation History:

Records all calculation requests and their results in a PostgreSQL database.

Supports paginated history retrieval for efficient data browsing.

Error Handling:

Robust handling of errors, including rate limit violations, external service failures, and invalid inputs.

Prerequisites

Before running the application, ensure you have the following installed:

Docker and Docker Compose: For containerized deployment.

A Docker Hub account: To pull the pre-built image.

Step-by-Step Instructions

1. Pull the Docker Image

Pull the pre-built Docker image from Docker Hub:

docker pull jpzambrano92/compute-service:latest

2. Run the Application

Use the provided docker-compose.yml file to start the application along with PostgreSQL and Redis:

docker-compose up

This starts three containers:

The Compute Service API on port 8080.

A PostgreSQL database on port 5432.

A Redis cache on port 6379.

3. Access the API

Swagger UI: View API documentation at http://localhost:8080/swagger-ui.html.

Base API URL: The API endpoints are accessible under http://localhost:8080/api.

4. Example Usage

Dynamic Percentage Calculation

Endpoint: POST /api/calculate

Purpose: Use this endpoint to calculate the sum of two numbers and apply a dynamic percentage.

Request Body:

{
"num1": 10,
"num2": 20
}

Response:

{
"result": 33
}

Get Calculation History

Endpoint: GET /api/history

Purpose: Retrieve a paginated list of all previous calculations.

Query Parameters:

page: Page number (default: 0).

size: Records per page (default: 10).

Response:

{
"content": [
{
"num1": 10,
"num2": 20,
"result": 33,
"timestamp": "2025-01-14T10:00:00"
}
],
"pageable": {
"pageNumber": 0,
"pageSize": 10
}
}

Environment Variables

The following environment variables are pre-configured in docker-compose.yml and control application behavior:

Variable

Description

Default Value

SPRING_DATASOURCE_URL

Connection URL for the PostgreSQL database.

jdbc:postgresql://database:5432/compute_service

SPRING_DATASOURCE_USERNAME

Database username.

postgres

SPRING_DATASOURCE_PASSWORD

Database password.

postgres

SPRING_REDIS_HOST

Host for the Redis cache.

redis

SPRING_REDIS_PORT

Port for the Redis cache.

6379

Testing

Running Tests

To validate the functionality of the application, run:

./mvnw test

Unit Tests: Ensure each component functions correctly (e.g., caching, calculation logic).

Integration Tests: Verify interactions between components such as controllers and services.

Contributing

Interested in improving this project? Follow these steps:

Fork the repository.

Create a new branch for your changes.

Submit a pull request with a detailed description of the improvements or fixes.
