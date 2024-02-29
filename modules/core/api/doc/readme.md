# How to use the eWiLL backend

## Tools & Installation

- Settings I used for IntelliJ IDEA: [settings.zip](settings.zip) (Not necessary to import, just for reference)
- Docker Desktop
- Spring Boot
- Springdoc OpenAPI
- Git for Version Control
- Postgres for Database
- Maven for Dependency Management (included in IntelliJ) and will be changed to gradle later on to merge into the "Feedbacksystem"
- If Springboot does not automatically insert the default data into the database import [this](/src/main/resources/data.sql)

## How to run the backend

To run the Backend just execute the docker compose file in the root directory of the project. This will start the backend and the database. The backend will be available at http://localhost:8083  
For not having to start the docker container at every execution, just terminate the api dokcer container and start the Spring run Config in IntelliJ. Keep the database docker container running.

## Documentation

[Swagger](http://localhost:8083/swagger-ui/index.html)
