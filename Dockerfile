FROM openjdk:11-jre-slim
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/vodacommft-0.0.1-SNAPSHOT.jar /app/vodacommft.jar
RUN chgrp -R 0 /app && chmod -R g+rwX /app
LABEL io.vodacom.mft-service 8080/http
# OpenShift uses root group instead of root user
USER 1001
ENTRYPOINT ["java", "-Xmx1024m", "-Xms1024m", "-jar", "/app/vodacommft.jar"]
