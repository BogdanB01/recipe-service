# Recipe-service

Recipe service is a Spring Boot application which allows users to manage their favourite recipes. It allows adding, updating, removing and fetching recipes. Additionally users are able to filter available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.

Technologies:
1. Java 11
2. Spring Boot
2. H2 in-memory database

How to run the application:
1. mvn spring-boot:run

Swagger UI:
http://localhost:8080/swagger-ui/index.html#/recipe-controller
