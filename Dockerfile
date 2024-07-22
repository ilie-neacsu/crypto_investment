FROM openjdk:21-jdk
WORKDIR /app
COPY target/crypto_investment-0.0.1-SNAPSHOT.jar /app/crypto_investment.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/crypto_investment.jar"]
