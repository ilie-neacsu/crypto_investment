# Crypto Investment Service (Interview Project)

This is the implementation of a homework project that was part of the interviewing process with [MX](https://www.mx.com/).<br />
The specs for the project are available [here](https://github.com/ilie-neacsu/crypto_investment/blob/main/docs/crypto_recommendations_service_specs.pdf).<br />
The [review](https://github.com/ilie-neacsu/crypto_investment/blob/main/docs/mm_task_review.pdf) from MX was unfortunately 
negative due to the lack of unit/integration tests.<br /> 
A crucial takeaway for the future <span style="color: red; font-weight: bold;"> NEVER SKIP AUTOMATED TESTING </span>, 
regardless of the circumstances and regardless of project (homework or real world project).<br />
<br />

---

This provides a service for ingesting cryptocurrency data and generating investment recommendations based on various statistics.

## Prerequisites

- Docker
- Docker Compose

## Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/ilie-neacsu/crypto_investment.git
    cd crypto_investment
    ```

2. Ensure you have Docker and Docker Compose installed on your machine. If not, you can install Docker from [here](https://docs.docker.com/get-docker/) and Docker Compose from [here](https://docs.docker.com/compose/install/).

## Running the Service

1. Build and start the services using Docker Compose:

    ```sh
    docker-compose up
    ```

   This command will:
    - Pull the Docker images for the application and PostgreSQL database.
    - Start the application on port `8080`.
    - Start the PostgreSQL database on port `5432`.

2. Verify that the services are running:

   You can check the status of the services using:

    ```sh
    docker-compose ps
    ```

## Accessing the OpenAPI Specification

After starting the containers, you can access the OpenAPI specification (Swagger UI) for the Crypto Investment Service at:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


This interface allows you to interact with the API endpoints and view the API documentation.

## Using the API

Here are some example `curl` commands to interact with the API endpoints:

1. **Ingest a CSV file of crypto prices**:

    ```sh
    curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/BTC_values.csv"
    ```

2. **Get yearly statistics for a specific crypto (e.g., BTC)**:

    ```sh
    curl http://localhost:8080/api/BTC/stats/yearly
    ```

3. **Get six-month statistics for a specific crypto (e.g., DOGE)**:

    ```sh
    curl http://localhost:8080/api/DOGE/stats/six-months
    ```

4. **Get cryptos sorted by normalized range**:

    ```sh
    curl http://localhost:8080/api/normalized-range
    ```

5. **Get statistics for a specific crypto (e.g., DOGE)**:

    ```sh
    curl http://localhost:8080/api/DOGE/stats
    ```

6. **Get the crypto with the highest normalized range on a specific date**:

    ```sh
    curl http://localhost:8080/api/highest-normalized-range/2022-01-21
    ```

## Environment Variables

The `docker-compose.yaml` file is configured with the following environment variables for the PostgreSQL database and the Spring Boot application:

- `SPRING_DATASOURCE_URL`: JDBC URL for the PostgreSQL database
- `SPRING_DATASOURCE_USERNAME`: Username for the PostgreSQL database
- `SPRING_DATASOURCE_PASSWORD`: Password for the PostgreSQL database

These environment variables are set in the `docker-compose.yaml` file:

```yaml
services:
   app:
      image: ineacsu/crypto_investment:latest
      ports:
         - "8080:8080"
      environment:
         SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/crypto_investment
         SPRING_DATASOURCE_USERNAME: postgres
         SPRING_DATASOURCE_PASSWORD: postgres
      depends_on:
         - postgres

   postgres:
      image: postgres:latest
      container_name: postgres-db
      environment:
         POSTGRES_USER: postgres
         POSTGRES_PASSWORD: postgres
         POSTGRES_DB: crypto_investment
      ports:
         - "5432:5432"
      volumes:
         - postgres-data:/var/lib/postgresql/data

volumes:
   postgres-data:
      driver: local
```

## Stopping the Service

To stop the services, use the following command:

```sh
docker-compose down
```
