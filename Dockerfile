FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar viewed.jar
ENTRYPOINT ["java","-jar","/viewed.jar"]
