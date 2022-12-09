#
# Build stage
#
FROM maven:3.6.0-jdk-13-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-alpine
COPY --from=build /home/app/target/ewill_backend.jar /usr/local/lib/ewill_backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/ewill_backend.jar"]