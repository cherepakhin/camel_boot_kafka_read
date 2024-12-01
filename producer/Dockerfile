FROM openjdk:17
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh" ,"-c", "java -jar /app.jar"]
LABEL maintainer="Cherepakhin Vasili" email="vasi.che@gmail.com"