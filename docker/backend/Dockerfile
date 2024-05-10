# uses java 17 runtime
FROM openjdk:17-oracle

# creates workdir /app
WORKDIR /app

# copies the application to the container
COPY ../../target/asset_management-1.0.jar asset-management.jar

# exposes the application to port 8080
EXPOSE 8080

# runs the application
CMD ["java", "-jar", "asset-management.jar"]