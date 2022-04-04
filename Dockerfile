ARG BASE_IMAGE=senzing/senzing-base:1.6.6
ARG BASE_BUILDER_IMAGE=senzing/base-image-debian:1.0.7

# -----------------------------------------------------------------------------
# Stage: builder
# -----------------------------------------------------------------------------

FROM ${BASE_BUILDER_IMAGE} as builder

ENV REFRESHED_AT=2022-04-01

LABEL Name="senzing/senzing-poc-server-builder" \
      Maintainer="support@senzing.com" \
      Version="1.0.0"

# Set environment variables.

ENV SENZING_ROOT=/opt/senzing
ENV SENZING_G2_DIR=${SENZING_ROOT}/g2
ENV PYTHONPATH=${SENZING_ROOT}/g2/python
ENV LD_LIBRARY_PATH=${SENZING_ROOT}/g2/lib:${SENZING_ROOT}/g2/lib/debian

# Build "senzing-api-server.jar".

COPY senzing-api-server /senzing-api-server
WORKDIR /senzing-api-server
RUN make install

# Build "senzing-poc-server.jar".

COPY . /poc-api-server
WORKDIR /poc-api-server

RUN export POC_API_SERVER_VERSION=$(mvn "help:evaluate" -Dexpression=project.version -q -DforceStdout) \
 && make package \
 && cp /poc-api-server/target/senzing-poc-server-${POC_API_SERVER_VERSION}.jar "/senzing-poc-server.jar"

# -----------------------------------------------------------------------------
# Stage: Final
# -----------------------------------------------------------------------------

FROM ${BASE_IMAGE}

ENV REFRESHED_AT=2022-04-01

LABEL Name="senzing/senzing-poc-server" \
      Maintainer="support@senzing.com" \
      Version="1.2.0"

HEALTHCHECK CMD ["/app/healthcheck.sh"]

# Run as "root" for system installation.

USER root

# Install packages via apt.

RUN apt update \
 && apt -y install \
      software-properties-common \
 && rm -rf /var/lib/apt/lists/*

# Install Java-11.

RUN wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | apt-key add - \
 && add-apt-repository --yes https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/ \
 && apt update \
 && apt install -y adoptopenjdk-11-hotspot \
 && rm -rf /var/lib/apt/lists/*

# Copy files from repository.

COPY ./rootfs /

# Service exposed on port 8080.

EXPOSE 8080

# Copy files from builder step.

COPY --from=builder "/senzing-poc-server.jar" "/app/senzing-poc-server.jar"

# Make non-root container.

USER 1001

# Runtime execution.

WORKDIR /app
ENTRYPOINT ["java", "-jar", "senzing-poc-server.jar"]
