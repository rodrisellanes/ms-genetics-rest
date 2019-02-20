# Basic docker image

FROM openjdk:8-jdk-alpine
ADD build/libs/adn-1.0.0-SNAPSHOT.jar adn-1.0.0-SNAPSHOT.jar
EXPOSE 8080
VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} ./build/libs/
ENTRYPOINT ["java","-jar","/adn-1.0.0-SNAPSHOT.jar"]
MAINTAINER Rodrigo Sellanes "rdsellanes@gmail.com"
CMD ["Docker image generated -> ms-genetics-rest"]