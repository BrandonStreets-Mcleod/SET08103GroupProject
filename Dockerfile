FROM openjdk:latest
COPY ./target/GroupProject-0.1.0.2-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "GroupProject-0.1.0.2-jar-with-dependencies.jar"]