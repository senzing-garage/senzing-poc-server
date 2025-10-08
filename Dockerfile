ARG BASE_IMAGE=senzing/senzingapi-runtime:3.12.8@sha256:3663a1971e564af4d12ecdb0c90a4f46418b77dc229ec6c9f692efc59d1c67ae
ARG BASE_BUILDER_IMAGE=senzing/base-image-debian:1.0.24@sha256:1e00881b45a78d9d93973ba845cd83d35aeb318273e30dde89060e01a9d0167c

# -----------------------------------------------------------------------------
# Stage: builder
# -----------------------------------------------------------------------------

FROM ${BASE_BUILDER_IMAGE} as builder

ENV REFRESHED_AT=2024-06-24

# Run as "root" for system installation.

USER root

# Set environment variables.

ENV SENZING_ROOT=/opt/senzing
ENV SENZING_G2_DIR=${SENZING_ROOT}/g2
ENV PYTHONPATH=${SENZING_ROOT}/g2/sdk/python
ENV LD_LIBRARY_PATH=${SENZING_ROOT}/g2/lib:${SENZING_ROOT}/g2/lib/debian

# Build "senzing-api-server.jar".

COPY senzing-api-server /senzing-api-server
WORKDIR /senzing-api-server
RUN make install

# Build "data-mart-replicator.jar".

COPY data-mart-replicator /data-mart-replicator
WORKDIR /data-mart-replicator
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

ENV REFRESHED_AT=2024-06-24

LABEL Name="senzing/senzing-poc-server" \
  Maintainer="support@senzing.com" \
  Version="3.6.9"

HEALTHCHECK CMD ["/app/healthcheck.sh"]

# Run as "root" for system installation.

USER root

# Install packages via apt-get.

RUN apt-get update \
  && apt-get -y --no-install-recommends install \
  gnupg2 \
  jq \
  libodbc1 \
  postgresql-client \
  unixodbc \
  && rm -rf /var/lib/apt/lists/*

# Install Java-11.

RUN mkdir -p /etc/apt/keyrings \
  && wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public > /etc/apt/keyrings/adoptium.asc

RUN echo "deb [signed-by=/etc/apt/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" >> /etc/apt/sources.list

RUN apt-get update \
  && apt-get install -y --no-install-recommends temurin-11-jdk \
  && rm -rf /var/lib/apt/lists/*

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

COPY --from=senzing/senzing-poc-server:1.3.0@sha256:477c44675aa2178fdb4b0789451ba40906f088b5cdffe8c5a4e5a59de61b6b59 "/app/senzing-poc-server.jar" "/appV2/senzing-poc-server.jar"

# Make non-root container.

USER 1001

# Set environment variables for USER 1001.

ENV LD_LIBRARY_PATH=/opt/senzing/g2/lib:/opt/senzing/g2/lib/debian:/opt/IBM/db2/clidriver/lib
ENV ODBCSYSINI=/etc/opt/senzing
ENV PATH=${PATH}:/opt/senzing/g2/python:/opt/IBM/db2/clidriver/adm:/opt/IBM/db2/clidriver/bin

# Runtime execution.

WORKDIR /app

ENTRYPOINT ["/app/docker-entrypoint.sh"]
