FROM openjdk:8-jdk-alpine
MAINTAINER kevin.zellweger@bluewin.ch
VOLUME /tmp
EXPOSE 8080
ADD build/libs/autopricing-0.0.1-SNAPSHOT.jar-0.0.1-SNAPSHOT.jar autopricing.jar
ENTRYPOINT ["java","-jar","/autopricing.jar"]