FROM openjdk:11-jdk
ARG JAR_FILE=build/libs/unknowng-server-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} unknowng-server.jar
ENTRYPOINT ["java","-DSpring.profiles.active=prod", "-jar","unknowng-server.jar"]