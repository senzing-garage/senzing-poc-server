ARG BASE_IMAGE=senzing/senzingapi-runtime:3.4.0
ARG BASE_BUILDER_IMAGE=senzing/base-image-debian:1.0.10

# -----------------------------------------------------------------------------
# Stage: builder
# -----------------------------------------------------------------------------

FROM ${BASE_BUILDER_IMAGE} as builder

ENV REFRESHED_AT=2023-01-13

LABEL Name="senzing/senzing-poc-server-builder" \
      Maintainer="support@senzing.com" \
      Version="3.3.7"

# Set environment variables.

ENV SENZING_ROOT=/opt/senzing
ENV SENZING_G2_DIR=${SENZING_ROOT}/g2
ENV PYTHONPATH=${SENZING_ROOT}/g2/sdk/python
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

# Grab a gpg key for our final stage to install the JDK

RUN wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public > /gpg.key

# -----------------------------------------------------------------------------
# Stage: Final
# -----------------------------------------------------------------------------

FROM ${BASE_IMAGE}

ENV REFRESHED_AT=2023-01-13

LABEL Name="senzing/senzing-poc-server" \
      Maintainer="support@senzing.com" \
      Version="3.3.7"

HEALTHCHECK CMD ["/app/healthcheck.sh"]

# Run as "root" for system installation.

USER root

# Install packages via apt.

RUN apt update \
 && apt -y install \
      gnupg2 \
      jq \
      libodbc1 \
      postgresql-client \
      unixodbc \
 && rm -rf /var/lib/apt/lists/*

# Install Java-11.

COPY --from=builder "/gpg.key" "gpg.key"

RUN echo "deb https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/ bullseye main" >> /etc/apt/sources.list \
    echo "# deb-src https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/ bullseye main" >> /etc/apt/sources.list

RUN cat gpg.key | apt-key add - \
 && apt update \
 && apt install -y adoptopenjdk-11-hotspot \
 && rm -rf /var/lib/apt/lists/* \
 && rm -f gpg.key

# Copy files from repository.

COPY ./rootfs /

# Set environment variables for root.

ENV LD_LIBRARY_PATH=/opt/senzing/g2/lib:/opt/senzing/g2/lib/debian:/opt/IBM/db2/clidriver/lib
ENV ODBCSYSINI=/etc/opt/senzing
ENV PATH=${PATH}:/opt/senzing/g2/python:/opt/IBM/db2/clidriver/adm:/opt/IBM/db2/clidriver/bin

# Service exposed on port 8080.

EXPOSE 8080

# Copy files from builder step.

COPY --from=builder "/senzing-poc-server.jar" "/app/senzing-poc-server.jar"

# Copy files from other docker containers.

COPY --from=senzing/senzing-poc-server:1.3.0 "/app/senzing-poc-server.jar" "/appV2/senzing-poc-server.jar"

# Make non-root container.

USER 1001

# Set environment variables for USER 1001.

ENV LD_LIBRARY_PATH=/opt/senzing/g2/lib:/opt/senzing/g2/lib/debian:/opt/IBM/db2/clidriver/lib
ENV ODBCSYSINI=/etc/opt/senzing
ENV PATH=${PATH}:/opt/senzing/g2/python:/opt/IBM/db2/clidriver/adm:/opt/IBM/db2/clidriver/bin

# Runtime execution.

WORKDIR /app

ENTRYPOINT ["/app/docker-entrypoint.sh"]
