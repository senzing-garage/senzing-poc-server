# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
[markdownlint](https://dlaa.me/markdownlint/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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

- Upgrade to `senzing/senzing-api-server:2.7.5`

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
