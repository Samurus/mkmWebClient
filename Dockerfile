FROM openjdk:11-jre-slim-buster
ARG profile
WORKDIR /app
RUN addgroup --system spring && adduser --ingroup spring --system spring
RUN mkdir logs
RUN chmod a+rwx -R logs
USER spring:spring
COPY /build/libs/*.jar autopricing.jar
ENV spring_profiles_active=${profile}
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=", "autopricing.jar"]