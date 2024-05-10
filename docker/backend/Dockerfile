FROM openjdk:17-oracle

WORKDIR /app

COPY ../../target/asset_management-1.0.jar asset-management.jar

EXPOSE 8080

CMD ["java", "-jar", "asset-management.jar"]