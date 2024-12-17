FROM amazoncorretto:17
COPY app.jar app.jar
EXPOSE 8190
ENTRYPOINT ["java","-jar","/app.jar"]