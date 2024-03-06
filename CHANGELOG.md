# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
[markdownlint](https://dlaa.me/markdownlint/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.5.2] - 2024-03-06

### Changed in 3.5.2

- Added Data Mart Statistics service endpoints:
  - `GET /statistics/summary`
  - `GET /statistics/summary/data-sources/{dataSourceCode}`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/matches`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/ambiguous-matches`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possible-matches`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possible-relations`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/disclosed-relations`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/matches/entities`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/ambiguous-matches/entities`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/ambiguous-matches/relations`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possible-matches/entities`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possible-matches/relations`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possible-relations/entities`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possible-relations/relations`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/disclosed-relations/entities`
  - `GET /statistics/summary/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/disclosed-relations/relations`
  - `GET /statistics/loaded/data-sources/{dataSourceCode}/entities`
- Added supporting classes and interfaces to `com.senzing.poc.model` package to support new endpoints
- Added `SummaryStatsServices.java` to support new endpoints
- Updated POC REST API Specification version to 3.5.2
- Updated `BuildInfo.java` to reflect new specification version
- Renamed `SzMatchType` to `SzRelationType` since it only dealt with types of relationships
- Updated dependencies:
  - Updated `senzing-commons` to version `3.2.0`
  - Updated Jetty dependencies to version `9.4.54.v20240208`
  - Updated Jackson dependencies to version `2.16.1`
  - Updated `junit-jupiter` to version `5.10.2`
  - Updated `icu4j` to version `74.2`
  - Updated `sqs` to version `2.24.12`
  - Updated `kafka-clients` to version `3.7.0`
  - Updated SLF4J dependencies to version `2.0.12`
  - Updated `org.glassfish/javax.annotation` to `jakarta.annotation-api` version `2.1.1`
  - Updated Spring dependencies to version `5.3.32`
  - Updated `maven-surefire-plugin` to version `3.2.5`
  - Updated `maven-compiler-plugin` to version `3.12.1`
  - Updated `maven-shade-plugin` to version `3.5.2`

## [3.5.1] - 2023-12-15

### Changed in 3.5.1

- Added Entity Relation Breakdown service endpoints
  - `GET /statistics/relations`
  - `GET /statistics/relations/{relationsCount}`
  - `GET /statistics/relations/{relationsCount}/entities`
- Updated POC REST API Specification version to 3.5.1
- Updated `BuildInfo.java` to reflect new specification version
- Fixed various JavaDoc errors caused by copy/paste
- Renamed model and service classes to match endpoint names

## [3.5.0] - 2023-12-12

### Changed in 3.5.0

- Added integration with embedded Data Mart Replicator
  - Added `data-mart-replicator` submodule
  - Added options to configure data mart database connection 
  - Added initial REST operations for data mart statistics
- Deprecated stream loader integration
  - Marked all command-line options deprecated
  - Stream loader integration to be removed in version 4.0
  - Removed startup requirement for load queue configuration

## [3.4.8] - 2023-11-14

### Changed in 3.4.8

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.8.0
- In `pom.xml`, updated:
  - sqs 2.20.162
  - kafka-clients 3.6.0
- Updated `senzing-api-server` dependency to version `3.5.8`

## [3.4.7] - 2023-09-29

### Changed in 3.4.7

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.7.1`
- In `Dockerfile`, updated to Java 11 JDK
- Updated `senzing-api-server` dependency to version `3.5.7`

## [3.4.6] - 2023-09-06

### Changed in 3.4.6

- Updated `senzing-api-server` dependency to version `3.5.6`
- Updated remaining `2.39` Jersey dependencies to version `2.40`
- Updated pom.xml dependencies for Jetty, Swagger, and Amazon SQS

## [3.4.5] - 2023-06-30

### Changed in 3.4.5

- Updated `pom.xml` to POC Server version `3.4.5`
- Updated `Dockerfile` to version `3.4.5`
- Updated `sqlite-jdbc` to version `3.42.0.0`
- Updated `senzing-api-server` to version `3.5.5`
- Updated Jersey dependencies to version `2.40`
- Updated `icu4j` to version `73.2`
- Updated `sqs` to version `2.20.94`
- Updated `amqp-client` to version `5.18.0`
- Updated `kafka-clients` to version `3.5.0`

### Changed in 3.4.4

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.6.0`

## [3.4.3] - 2023-06-15

### Changed in 3.4.3

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.5.3`

## [3.4.2] - 2023-06-08

### Changed in 3.4.2

- Upgraded `senzing-api-server` dependency to version `3.5.2`
- Upgraded "Jackson" libraries to version `2.15.2`
- Upgraded "icu4J" to version `73.1`
- Upgraded various maven plugin dependencies for building.

## [3.4.1] - 2023-05-10

### Changed in 3.4.1

- Upgraded `senzing-api-server` dependency to version `3.5.1`
- Upgraded "Jackson" libraries to version `2.15.0`
- Upgraded `junit-jupiter` to version `5.9.3`
- Upgraded `swagger-annotations` to version `2.2.9`
- Upgraded `maven-resources-plugin` to version `3.3.1`
- Updated POC REST API specification to version `3.3.0`
- Updated `BuildInfo` to reflect REST API spec version `3.3.0`
- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.5.2`

## [3.4.0] - 2023-04-03

### Changed in 3.4.0

- Updated `senzing-api-server` dependency to version `3.5.0`
- Updated third-party dependency versions to newer versions
- Updated underlying Docker images.

## [3.3.9] - 2023-03-10

### Changed in 3.3.9

- Updated `senzing-api-server` dependency to version `3.4.12`
- Updated third-party dependency versions to newer versions

## [3.3.8] - 2023-02-09

### Changed in 3.3.8

- Updated `senzing-api-server` dependency to version `3.4.11`
- Updated third-party dependency versions to newer versions

## [3.3.7] - 2023-01-23

### Changed in 3.3.7

- Updated `senzing-api-server` dependency to version `3.4.10`
- Modified bulk-data API to allow JSON-lines (`application/x-jsonlines`)
  content when JSON (`application/json`) Content-Type header is provided.

## [3.3.6] - 2023-01-13

### Changed in 3.3.6

- In `Dockerfile`, fix vulnerability exposed by `software-properties-common`

## [3.3.5] - 2023-01-12

### Changed in 3.3.5

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.4.0`

>>>>>>> main

## [3.3.4] - 2023-01-05

### Changed in 3.3.4

- Updated dependency versions to newer versions along with `senzing-api-server`
  version to `3.4.7`.

## [3.3.3] - 2022-10-27

### Changed in 3.3.3

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.3.2`

## [3.3.2] - 2022-10-11

### Changed in 3.3.2

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.3.1`
- In `pom.xml`,
  - Updated `software.amazon.awssdk` to 2.17.285
  - Updated `senzing-api-server` to 3.4.4

## [3.3.1] - 2022-09-28

### Changed in 3.3.1

- In `Dockerfile`, updated FROM instruction to `senzing/senzingapi-runtime:3.3.0`

## [3.3.0] - 2022-09-26

### Changed in 3.3.0

- Updated to version `3.4.0` of the `senzing-api-server` which changes the
  default HTTP and HTTPS ports to 8250 and 8263, respectively.  In addition to
  adding support for 2 new `SzDetailLevel` values and one new `SzFeatureMode`
  value.

## [3.2.3] - 2022-08-30

### Changed in 3.2.3

- In `Dockerfile`, removed `CMD` to simplify input parameters

## [3.2.2] - 2022-08-26

### Changed in 3.2.2

- In `Dockerfile`, bump from `senzing/senzingapi-runtime:3.1.1` to `senzing/senzingapi-runtime:3.2.0`

## [3.2.1] - 2022-08-23

### Changed in 3.2.1

- Updated dependency on `senzing-garage/senzing-api-server` to a minimum version of
  `3.3.1` to resolve issue with environment variable fallbacks for primary
  options which typically lack dependencies.

## [3.2.0] - 2022-08-18

### Changed in 3.2.0

- Updated to `senzing-api-server` version `3.3.0` including all changes provided
  by that:
  - Adds support for `SENZING_ENGINE_CONFIGURATION_JSON` environment variable
    per Issue #61
  - Adds "how entity" support (both by record ID and entity ID)
  - Adds "get virtual entity" support
  - Adds `detailLevel` support
  - Adds `SzFeatureMode.ATTRIBUTED` support
  - See `CHANGELOG.md` from `senzing-garage/senzing-api-server` for further details
- Updated dependencies to match the `senzing-api-server` dependency versions

## [3.1.1] - 2022-07-20

### Changed in 3.1.1

- In `Dockerfile`, bump from `senzing/senzingapi-runtime:3.1.0` to `senzing/senzingapi-runtime:3.1.1`

## [3.1.0] - 2022-07-12

### Changed in 3.1.0

- Migrated to `senzing/senzingapi-runtime` as Docker base image

## [3.0.2] - 2022-06-08

### Changed in 3.0.2

- Upgrade `Dockerfile` to `FROM debian:11.3-slim@sha256:06a93cbdd49a265795ef7b24fe374fee670148a7973190fb798e43b3cf7c5d0f`

## [3.0.1] - 2022-05-06

### Changed in 3.0.1

- Added `libodbc1` to Dockerfile

## [3.0.0] - 2022-05-04

### Changed in 3.0.0

- Updated version to `3.0.0` to match `senzing-api-server` major version.
- Updated dependency on `senzing-api-server` to `3.x`
- Version 3.0.0 requires Senzing version 3.x

## [1.3.0] - 2022-03-29

### Changed in 1.3.0

- Updated to `senzing-api-server` version 2.8.5 for security vulnerability fix
- Updated other dependencies to more recent versions in `pom.xml`

## [1.2.0] - 2022-02-04

### Changed in 1.2.0

- Updated to `senzing-api-server` version 2.8.3 for dependency on the release
  version 2.x of `senzing-commons-java`
- Changed references to `com.senzing.util.JsonUtils` to
  `com.senzing.util.JsonUtilities`

## [1.1.1] - 2022-01-19

### Changed in 1.1.1

- Updated to `senzing-api-server` version 2.8.1 to reduce repo size
- Updated POC REST API spec to fix bulk-data examples
- Updated spring framework dependencies in `pom.xml` tp address security
  vulnerabilities.

## [1.1.0] - 2021-12-01

### Changed in 1.1.0

- Updated to `senzing-api-server` version 2.8.0 using `senzings-common-java`
- Now masks the value of the Rabbit MQ password in logs
- Upgrade to `senzing/senzing-base:1.6.3`

## [1.0.5] - 2021-10-12

### Changed in 1.0.5

- Upgrade to `senzing-garage/senzing-api-server:2.7.5`

## [1.0.4] - 2021-10-11

### Changed in 1.0.4

- Upgrade to `senzing/senzing-base:1.6.2`

## [1.0.3] - 2021-10-06

### Changed in 1.0.3

- Fixed typo causing a conflict between each Rabbit MQ command-line option and
  every other Rabbit MQ command-line option.

## [1.0.2] - 2021-09-08

### Changed in 1.0.2

- Updated to Senzing API Server version 2.7.4 to add improved debug logging
  and `--debug` usage message.

## [1.0.1] - 2021-09-02

### Changed in 1.0.1

- Updated to Senzing API Server version 2.7.3 to add --debug option and fix
  startup option logging.
- Added debug logging for stream-loading operations if --debug enabled.
- Updated build-info.properties so the Maven build timestamp is properly
  filtered during build and token-replaced.

## [1.0.0] - 2021-08-22

### Initial release
