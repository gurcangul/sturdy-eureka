FROM gcr.io/distroless/java17-debian11:nonroot

ARG ARTIFACT_NAME
ARG ARTIFACT_VERSION
ARG ARTIFACT_TYPE=jar

WORKDIR /app

COPY target/${ARTIFACT_NAME}-${ARTIFACT_VERSION}.${ARTIFACT_TYPE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Dspring.profiles.active=prod", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
