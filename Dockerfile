FROM openjdk:18-ea-11-jdk-alpine3.15
COPY target/transaction-authorizer-1.0.0.jar transaction-authorizer-1.0.0.jar
ENTRYPOINT ["java","-jar","/transaction-authorizer-1.0.0.jar"]