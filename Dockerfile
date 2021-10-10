FROM openjdk:8-jre-alpine
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/vodacommft-0.0.1-SNAPSHOT.jar /app/vodacommft.jar
ENTRYPOINT ["java", "-Xmx1024m", "-Xms1024m", "-jar", "/app/vodacommft.jar"]



