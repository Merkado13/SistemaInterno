FROM openjdk:8-jre
RUN mkdir /usr/app
COPY target/*.jar /usr/app
WORKDIR /usr/app
CMD ["java","-jar","servicio_interno.jar"]
