FROM openjdk:11-jre-slim
COPY build/libs/omc-0.0.1-SNAPSHOT.jar omc-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-DSpring.profiles.active=prod","-jar","omc-0.0.1-SNAPSHOT.jar"]