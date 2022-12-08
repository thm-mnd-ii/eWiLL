# How to use the eWiLL backend

## Tools & Installation

- Settings i used for IntelliJ IDEA: [settings.zip](settings.zip) (Not necessary to import, just for reference)    
- Docker Desktop
- Git for Version Control
- Postgres for Database
- If Springboot does no automatically insert the default data into the database import [this](/src/main/resources/import.sql)

## How to run the backend

To run the Backend just execute the docker compose file in the root directory of the project. This will start the backend and the database. The backend will be available at http://localhost:8080    
For not having to start the docker container at every execution, just terminate the api dokcer container and start the Spring run Config in IntelliJ. Keep the database docker container running.


