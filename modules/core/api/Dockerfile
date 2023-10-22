FROM node:18-alpine as build-web

WORKDIR /build

# Build web
COPY modules/core/web /build/modules/core/web 
RUN cd /build/modules/core/web && npm install && npm run build

FROM maven:3.6.0-jdk-13-alpine AS build

WORKDIR /build

#Build api with maven
COPY modules/core/api /build/modules/core/api
COPY --from=build-web /build/modules/core/web/dist /build/modules/core/api/src/main/resources/static
RUN mvn -f /build/modules/core/api/pom.xml clean package -DskipTests

#
# Run Spring Boot app as jar
#
FROM openjdk:17-alpine
COPY --from=build /build/modules/core/api/target/api.jar /usr/local/api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/api.jar"]


