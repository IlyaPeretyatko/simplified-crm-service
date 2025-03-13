FROM openjdk:21-jdk-slim
COPY ./build/libs/template-0.0.1.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database:5430/crm
ENV POSTGRES_USER=crm
ENV POSTGRES_PASSWORD=crm
EXPOSE 8080
CMD java -jar /opt/service.jar