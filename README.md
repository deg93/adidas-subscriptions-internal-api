# Adidas Tech Challenge / Internal API

This repository contains the microservice whose objective is to manage newsletters subscriptions (subscriptions persistence and confirmation emails).

## API services

This microservice exposes 4 endpoints:

- `GET /api/subscriptions`: if any parameter is specified to the service, it returns all the active subscriptions (without parameters, this service is only executable by users with ADMIN role). If parameter `userId` is specified, the service will return the specified user active subscriptions.
- `POST /api/subscriptions`: creates a new subscription (and sends the confirmation email).
- `GET /api/subscriptions/{id}`: returns the specified subscription detail.
- `DELETE /api/subscriptions/{id}`: cancels the specified subscription.

This services are secured by JWT.

API documentation (Swagger UI) can be found [here](https://internal-api.adidas-tech-challenge.davidenjuan.es/swagger-ui/) for AWS environment (in a real environment, this API should not be exposed to Internet). In local environment, API docs are served in [http://localhost:8081/swagger-ui/](http://localhost:8081/swagger-ui/).

## Deployment

For deploying this microservice to AWS environment, is enough with pushing changes to main branch. This is done by [deploy-to-aws.yml](.github/workflows/deploy-to-aws.yml) GitHub workflow (pipeline).

For deploying locally, run the following command in the project root directory:
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.data.mongodb.uri=mongodb://<your-mongodb>"
```
Please, replace `<your-mongodb>` with a MongoDB host and port (for example: `mongodb.example.org:27017`).