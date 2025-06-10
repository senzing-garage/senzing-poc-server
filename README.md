# senzing-poc-server

If you are beginning your journey with [Senzing],
please start with [Senzing Quick Start guides].

You are in the [Senzing Garage] where projects are "tinkered" on.
Although this GitHub repository may help you understand an approach to using Senzing,
it's not considered to be "production ready" and is not considered to be part of the Senzing product.
Heck, it may not even be appropriate for your application of Senzing!

## Overview

The Senzing POC API Server serves as a backend to the Senzing POC application.
No guarantee is made for backwards compatibility with this code base; however,
the code provided serves as an example for how to extend the [Senzing API Server].

The [Senzing POC API OAS specification] documents the available API methods,
their parameters and the response formats.

### Contents

1. [Demonstrate using Command Line]
   1. [Dependencies]
   1. [Building]
   1. [Running]
1. [License]

### Legend

1. :thinking: - A "thinker" icon means that a little extra thinking may be required.
   Perhaps you'll need to make some choices.
   Perhaps it's an optional step.
1. :pencil2: - A "pencil" icon means that the instructions may need modification before performing.
1. :warning: - A "warning" icon means that something tricky is happening, so pay attention.

## Demonstrate using Command Line

### Dependencies

To build the Senzing POC API Server you will need Apache Maven (recommend version 3.6.1 or later)
as well as OpenJDK version 11.0.x (recommend version 11.0.6+10 or later).

You will also need the Senzing API Server installed in your Maven repository
which can be built from the `senzing-api-server` sub-repository via `mvn install`.

1. Ensure the GIT submodules are cloned (`senzing-api-server` and
   `senzing-api-server/senzing-rest-api-specification`) are cloned:

   ```console
   git submodule update --init --recursive
   ```

1. First build / install the `senzing-api-server` sub-repository using the
   instructions from the [Senzing API Server README.md]

1. Setup your environment. The API's rely on native libraries and the
   environment must be properly setup to find those libraries:

   1. Linux

      ```console
      export SENZING_G2_DIR=/opt/senzing/g2

      export LD_LIBRARY_PATH=${SENZING_G2_DIR}/lib:${SENZING_G2_DIR}/lib/debian:$LD_LIBRARY_PATH
      ```

   1. Windows

      ```console
      set SENZING_G2_DIR="C:\Program Files\Senzing\g2"

      set Path=%SENZING_G2_DIR%\lib;%Path%
      ```

### Building

To build simply execute:

```console
mvn install
```

The JAR file will be contained in the `target` directory under the name
`senzing-poc-server-[version].jar`.

Where `[version]` is the version number from the `pom.xml` file.

### Running

To execute the server you will use `java -jar`. It assumed that your environment
is properly configured as described in the "Dependencies" section above.

To start up you must provide the initialization parameters for the Senzing
native API. This is done through one of: `--init-file`, `--init-env-var` or the
`--init-json` options to specify how to obtain the initialization JSON parameters.

Other command-line options may be useful to you as well. Execute

```console
java -jar target/senzing-poc-server-3.5.0.jar --help
```

to obtain a help message describing all available options.
For example:

```console

java -jar senzing-poc-server-3.5.0.jar <options>

<options> includes:


[ Standard Options ]
   --help
        Also -help.  Should be the first and only option if provided.
        Causes this help message to be displayed.
        NOTE: If this option is provided, the server will not start.

   --version
        Also -version.  Should be the first and only option if provided.
        Causes the version of the G2 REST API Server to be displayed.
        NOTE: If this option is provided, the server will not start.

   --read-only [true|false]
        Also -readOnly.  Disables functions that would modify the entity
        repository data, causing those functions to return a 403 Forbidden
        response.  The true/false parameter is optional, if not specified
        then true is assumed.  If specified as false then it is the same as
        omitting the option with the exception that omission falls back to the
        environment variable setting whereas an explicit false overrides any
        environment variable.  NOTE: this option will not only disable loading
        data to the entity repository, but will also disable modifications to
        the configuration even if the --enable-admin option is provided.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_READ_ONLY

   --enable-admin [true|false]
        Also -enableAdmin.  Enables administrative functions via the API
        server.  Administrative functions include those that would modify
        the active configuration (e.g.: adding data sources, entity types,
        or entity classes).  The true/false parameter is optional, if not
        specified then true is assumed.  If specified as false then it is
        the same as omitting the option with the exception that omission
        falls back to the environment variable setting whereas an explicit
        false overrides any environment variable.  If not specified then
        administrative functions will return a 403 Forbidden response.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_ENABLE_ADMIN

   --http-port <port-number>
        Also -httpPort.  Sets the port for HTTP communication.  If not
        specified, then the default port (8250) is used.
        Specify 0 for a randomly selected available port number.  This
        option cannot be specified if SSL client authentication is configured.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_PORT

   --bind-addr <ip-address|loopback|all>
        Also -bindAddr.  Sets the bind address for HTTP communication.  If not
        provided the bind address defaults to the loopback address.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_BIND_ADDR

   --url-base-path <base-path>
        Also -urlBasePath.  Sets the URL base path for the API Server.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_URL_BASE_PATH

   --allowed-origins <url-domain>
        Also -allowedOrigins.  Sets the CORS Access-Control-Allow-Origin header
        for all endpoints.  There is no default value.  If not specified then
        the Access-Control-Allow-Origin is not included with responses.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_ALLOWED_ORIGINS

   --concurrency <thread-count>
        Also -concurrency.  Sets the number of threads available for executing
        Senzing API functions (i.e.: the number of engine threads).
        If not specified, then this defaults to 8.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_CONCURRENCY

   --http-concurrency <thread-count>
        Also -httpConcurrency.  Sets the maximum number of threads available
        for the HTTP server.  The single parameter to this option should be
        a positive integer.  If not specified, then this defaults to 200.  If
        the specified thread count is less than 10 then an error is reported
        --> VIA ENVIRONMENT: SENZING_API_SERVER_HTTP_CONCURRENCY

   --module-name <module-name>
        Also -moduleName.  The module name to initialize with.  If not
        specified, then the module name defaults to "senzing-api-server".
        --> VIA ENVIRONMENT: SENZING_API_SERVER_MODULE_NAME

   --ini-file <ini-file-path>
        Also -iniFile.  The path to the Senzing INI file to with which to
        initialize.
        EXAMPLE: -iniFile /etc/opt/senzing/G2Module.ini
        --> VIA ENVIRONMENT: SENZING_API_SERVER_INI_FILE

   --init-file <json-init-file>
        Also -initFile.  The path to the file containing the JSON text to
        use for Senzing initialization.
        EXAMPLE: -initFile ~/senzing/g2-init.json
        --> VIA ENVIRONMENT: SENZING_API_SERVER_INIT_FILE

   --init-env-var <environment-variable-name>
        Also -initEnvVar.  The environment variable from which to extract
        the JSON text to use for Senzing initialization.
        *** SECURITY WARNING: If the JSON text contains a password
        then it may be visible to other users via process monitoring.
        EXAMPLE: -initEnvVar SENZING_INIT_JSON
        --> VIA ENVIRONMENT: SENZING_API_SERVER_INIT_ENV_VAR

   --init-json <json-init-text>
        Also -initJson.  The JSON text to use for Senzing initialization.
        *** SECURITY WARNING: If the JSON text contains a password
        then it may be visible to other users via process monitoring.
        EXAMPLE: -initJson "{"PIPELINE":{ ... }}"
        --> VIA ENVIRONMENT: SENZING_API_SERVER_INIT_JSON
                             SENZING_ENGINE_CONFIGURATION_JSON (fallback)

   --config-id <config-id>
        Also -configId.  Use with the -iniFile, -initFile, -initEnvVar or
        -initJson options to force a specific configuration ID to use for
        initialization.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_CONFIG_ID

   --auto-refresh-period <positive-integer-seconds|0|negative-integer>
        Also -autoRefreshPeriod.  If leveraging the default configuration
        stored in the database, this is used to specify how often the API
        server should background check that the current active config is the
        same as the current default config, and if different reinitialize
        with the current default config.  If zero is specified, then the
        auto-refresh is disabled and it will only occur when a requested
        configuration element is not found in the current active config.
        Specifying a negative integer is allowed but is used to enable a
        check and conditional refresh only when manually requested
        (programmatically).  NOTE: This is option ignored if auto-refresh is
        disabled because the config was specified via the G2CONFIGFILE init
        option or if --config-id has been specified to lock to a specific
        configuration.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_AUTO_REFRESH_PERIOD

   --stats-interval <milliseconds>
        Also -statsInterval.  The minimum number of milliseconds between
        logging of stats.  This is minimum because stats logging is suppressed
        if the API Server is idle or active but not performing activities
        pertaining to entity scoring.  In such cases, stats logging is delayed
        until an activity pertaining to entity scoring is performed.  By
        default this is set to the millisecond equivalent of 15 minutes.  If
        zero (0) is specified then the logging of stats will be suppressed.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_STATS_INTERVAL

   --skip-startup-perf [true|false]
        Also -skipStartupPerf.  If specified then the performance check on
        startup is skipped.  The true/false parameter is optional, if not
        specified then true is assumed.  If specified as false then it is the
        same as omitting the option with the exception that omission falls back
        to the environment variable setting whereas an explicit false overrides
        any environment variable.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_SKIP_STARTUP_PERF

   --skip-engine-priming [true|false]
        Also -skipEnginePriming.  If specified then the API Server will not
        prime the engine on startup.  The true/false parameter is optional, if
        not specified then true is assumed.  If specified as false then it is
        the same as omitting the option with the exception that omission falls
        back to the environment variable setting whereas an explicit false
        overrides any environment variable.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_SKIP_ENGINE_PRIMING

   --verbose [true|false]
        Also -verbose.  If specified then initialize in verbose mode.  The
        true/false parameter is optional, if not specified then true is assumed.
        If specified as false then it is the same as omitting the option with
        the exception that omission falls back to the environment variable
        setting whereas an explicit false overrides any environment variable.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_VERBOSE

   --quiet [true|false]
        Also -quiet.  If specified then the API server reduces the number of
        messages provided as feedback to standard output.  This applies only to
        messages generated by the API server and not by the underlying API
        which can be quite prolific if --verbose is provided.  The true/false
        parameter is optional, if not specified then true is assumed.  If
        specified as false then it is the same as omitting the option with
        the exception that omission falls back to the environment variable
        setting whereas an explicit false overrides any environment variable.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_QUIET

   --debug [true|false]
        Also -debug.  If specified then debug logging is enabled.  The
        true/false parameter is optional, if not specified then true is assumed.
        If specified as false then it is the same as omitting the option with
        the exception that omission falls back to the environment variable
        setting whereas an explicit false overrides any environment variable.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_DEBUG

   --monitor-file <file-path>
        Also -monitorFile.  Specifies a file whose timestamp is monitored to
        determine when to shutdown.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_MONITOR_FILE


[ Data Mart Database Connectivity Options ]
   The following options pertain to configuring the connection to the data-mart
   database.  Exactly one such database must be configured.

   --sqlite-database-file <url>
        Specifies an SQLite database file to open (or create) to use as the
        data-mart database.  NOTE: SQLite may be used for testing, but because
        only one connection may be made, it will not scale for production use.
        --> VIA ENVIRONMENT: SENZING_DATA_MART_SQLITE_DATABASE_FILE

   --postgresql-host <hostname>
        Used to specify the hostname for connecting to PostgreSQL as the
        data-mart database.
        --> VIA ENVIRONMENT: SENZING_DATA_MART_POSTGRESQL_HOST

   --postgresql-port <port>
        Used to specify the port number for connecting to PostgreSQL as the
        data-mart database.
        --> VIA ENVIRONMENT: SENZING_DATA_MART_POSTGRESQL_PORT

   --postgresql-database <database>
        Used to specify the database name for connecting to PostgreSQL as the
        data-mart database.
        --> VIA ENVIRONMENT: SENZING_DATA_MART_POSTGRESQL_DATABASE

   --postgresql-user <user name>
        Used to specify the user name for connecting to PostgreSQL as the
        data-mart database.
        --> VIA ENVIRONMENT: SENZING_DATA_MART_POSTGRESQL_USER

   --postgresql-password <password>
        Used to specify the password for connecting to PostgreSQL as the
        data-mart database.
        --> VIA ENVIRONMENT: SENZING_DATA_MART_POSTGRESQL_PASSWORD

[ HTTPS / SSL Options ]
   The following options pertain to HTTPS / SSL configuration.  The
   --key-store and --key-store-password options are the minimum required
   options to enable HTTPS / SSL communication.  If HTTPS / SSL communication
   is enabled, then HTTP communication is disabled UNLESS the --http-port
   option is specified.  However, if client SSL authentication is configured
   via the --client-key-store and --client-key-store-password options then
   enabling HTTP communication via the --http-port option is prohibited.

   --https-port <port-number>
        Also -httpsPort.  Sets the port for secure HTTPS communication.
        While the default HTTPS port is 8263 if not specified,
        HTTPS is only enabled if the --key-store option is specified.
        Specify 0 for a randomly selected available port number.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_SECURE_PORT

   --key-store <path-to-pkcs12-keystore-file>
        Also -keyStore.  Specifies the key store file that holds the private
        key that the sever uses to identify itself for HTTPS communication.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_KEY_STORE

   --key-store-password <password>
        Also -keyStorePassword.  Specifies the password for decrypting the
        key store file specified with the --key-store option.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_KEY_STORE_PASSWORD

   --key-alias <server-key-alias>
        Also -keyAlias.  Optionally specifies the alias for the private server
        key in the specified key store.  If not specified, then the first key
        found in the specified key store is used.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_KEY_ALIAS

   --client-key-store <path-to-pkcs12-keystore-file>
        Also -clientKeyStore.  Specifies the key store file that holds the
        public keys of those clients that are authorized to connect.  If this
        option is specified then SSL client authentication is required and
        the --http-port option is forbidden.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_CLIENT_KEY_STORE

   --client-key-store-password <password>
        Also -clientKeyStorePassword.  Specifies the password for decrypting
        the key store file specified with the --client-key-store option.
        --> VIA ENVIRONMENT: SENZING_API_SERVER_CLIENT_KEY_STORE_PASSWORD


[ Asynchronous Info Queue Options ]
   The following options pertain to configuring an asynchronous message
   queue on which to send "info" messages generated when records are
   loaded, deleted or entities are re-evaluated.  At most one such queue
   can be configured.  If an "info" queue is configured then every load,
   delete and re-evaluate operation is performed with the variant to
   generate an info message.  The info messages that are sent on the queue
   (or topic) are the relevant "raw data" JSON segments.

   --sqs-info-url <url>
        Also -sqsInfoUrl.  Specifies an Amazon SQS queue URL as the info queue.
        --> VIA ENVIRONMENT: SENZING_SQS_INFO_QUEUE_URL

   --rabbit-info-host <hostname>
        Also -rabbitInfoHost.  Used to specify the hostname for connecting to
        RabbitMQ as part of specifying a RabbitMQ info queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_INFO_HOST
                             SENZING_RABBITMQ_HOST (fallback)

   --rabbit-info-port <port>
        Also -rabbitInfoPort.  Used to specify the port number for connecting
        to RabbitMQ as part of specifying a RabbitMQ info queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_INFO_PORT
                             SENZING_RABBITMQ_PORT (fallback)

   --rabbit-info-user <user name>
        Also -rabbitInfoUser.  Used to specify the user name for connecting to
        RabbitMQ as part of specifying a RabbitMQ info queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_INFO_USERNAME
                             SENZING_RABBITMQ_USERNAME (fallback)

   --rabbit-info-password <password>
        Also -rabbitInfoPassword.  Used to specify the password for connecting
        to RabbitMQ as part of specifying a RabbitMQ info queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_INFO_PASSWORD
                             SENZING_RABBITMQ_PASSWORD (fallback)

   --rabbit-info-virtual-host <virtual host>
        Also -rabbitInfoVirtualHost.  Used to specify the virtual host for
        connecting to RabbitMQ as part of specifying a RabbitMQ info queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_INFO_VIRTUAL_HOST
                             SENZING_RABBITMQ_VIRTUAL_HOST (fallback)

   --rabbit-info-exchange <exchange>
        Also -rabbitInfoExchange.  Used to specify the exchange for connecting
        to RabbitMQ as part of specifying a RabbitMQ info queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_INFO_EXCHANGE
                             SENZING_RABBITMQ_EXCHANGE (fallback)

   --rabbit-info-routing-key <routing key>
        Also -rabbitInfoRoutingKey.  Used to specify the routing key for
        connecting to RabbitMQ as part of specifying a RabbitMQ info queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_INFO_ROUTING_KEY

   --kafka-info-bootstrap-server <bootstrap servers>
        Also -kafkaInfoBootstrapServer.  Used to specify the bootstrap servers
        for connecting to Kafka as part of specifying a Kafka info topic.
        --> VIA ENVIRONMENT: SENZING_KAFKA_INFO_BOOTSTRAP_SERVER
                             SENZING_KAFKA_BOOTSTRAP_SERVER (fallback)

   --kafka-info-group <group id>
        Also -kafkaInfoGroupId.  Used to specify the group ID for connecting to
        Kafka as part of specifying a Kafka info topic.
        --> VIA ENVIRONMENT: SENZING_KAFKA_INFO_GROUP
                             SENZING_KAFKA_GROUP (fallback)

   --kafka-info-topic <topic>
        Also -kafkaInfoTopic.  Used to specify the topic name for connecting to
        Kafka as part of specifying a Kafka info topic.
        --> VIA ENVIRONMENT: SENZING_KAFKA_INFO_TOPIC


[ *** DEPRECATED *** Asynchronous Load Queue Options ]
   The following options pertain to configuring an asynchronous message
   queue on which to send record messages to be loaded by the stream loader.
   At most one such queue can be configured.  If a "load" queue is
   configured then endpoints that leverage the load queue become available.
   *** NOTE ***: These options are DEPRECATED since the Senzing stream
   loader is being deprecated.  Loading records via the stream loader will
   only update the data mart if the stream loader is publishing INFO messages
   to a queue that are being consumed by a separate Data Mart Replicator
   configured for the same data mart database

   --sqs-load-url <url>
        *** DEPRECATED: See deprecation note above ***
        Also -sqsLoadUrl.  Specifies an Amazon SQS queue URL as the load queue.
        --> VIA ENVIRONMENT: SENZING_SQS_LOAD_QUEUE_URL
                             SENZING_SQS_QUEUE_URL (fallback)

   --rabbit-load-host <hostname>
        *** DEPRECATED: See deprecation note above ***
        Also -rabbitLoadHost.  Used to specify the hostname for connecting to
        RabbitMQ as part of specifying a RabbitMQ load queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_LOAD_HOST
                             SENZING_RABBITMQ_HOST (fallback)

   --rabbit-load-port <port>
        *** DEPRECATED: See deprecation note above ***
        Also -rabbitLoadPort.  Used to specify the port number for connecting
        to RabbitMQ as part of specifying a RabbitMQ load queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_LOAD_PORT
                             SENZING_RABBITMQ_PORT (fallback)

   --rabbit-load-user <user name>
        *** DEPRECATED: See deprecation note above ***
        Also -rabbitLoadUser.  Used to specify the user name for connecting to
        RabbitMQ as part of specifying a RabbitMQ load queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_LOAD_USERNAME
                             SENZING_RABBITMQ_USERNAME (fallback)

   --rabbit-load-password <password>
        *** DEPRECATED: See deprecation note above ***
        Also -rabbitLoadPassword.  Used to specify the password for connecting
        to RabbitMQ as part of specifying a RabbitMQ load queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_LOAD_PASSWORD
                             SENZING_RABBITMQ_PASSWORD (fallback)

   --rabbit-load-virtual-host <virtual host>
        *** DEPRECATED: See deprecation note above ***
        Also -rabbitLoadVirtualHost.  Used to specify the virtual host for
        connecting to RabbitMQ as part of specifying a RabbitMQ load queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_LOAD_VIRTUAL_HOST
                             SENZING_RABBITMQ_VIRTUAL_HOST (fallback)

   --rabbit-load-exchange <exchange>
        *** DEPRECATED: See deprecation note above ***
        Also -rabbitLoadExchange.  Used to specify the exchange for connecting
        to RabbitMQ as part of specifying a RabbitMQ load queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_LOAD_EXCHANGE
                             SENZING_RABBITMQ_EXCHANGE (fallback)

   --rabbit-load-routing-key <routing key>
        *** DEPRECATED: See deprecation note above ***
        Also -rabbitLoadRoutingKey.  Used to specify the routing key for
        connecting to RabbitMQ as part of specifying a RabbitMQ load queue.
        --> VIA ENVIRONMENT: SENZING_RABBITMQ_LOAD_ROUTING_KEY
                             SENZING_RABBITMQ_ROUTING_KEY (fallback)

   --kafka-load-bootstrap-server <bootstrap servers>
        *** DEPRECATED: See deprecation note above ***
        Also -kafkaLoadBootstrapServer.  Used to specify the bootstrap servers
        for connecting to Kafka as part of specifying a Kafka load topic.
        --> VIA ENVIRONMENT: SENZING_KAFKA_LOAD_BOOTSTRAP_SERVER
                             SENZING_KAFKA_BOOTSTRAP_SERVER (fallback)

   --kafka-load-group <group id>
        *** DEPRECATED: See deprecation note above ***
        Also -kafkaLoadGroupId.  Used to specify the group ID for connecting to
        Kafka as part of specifying a Kafka load topic.
        --> VIA ENVIRONMENT: SENZING_KAFKA_LOAD_GROUP
                             SENZING_KAFKA_GROUP (fallback)

   --kafka-load-topic <topic>
        *** DEPRECATED: See deprecation note above ***
        Also -kafkaLoadTopic.  Used to specify the topic name for connecting to
        Kafka as part of specifying a Kafka load topic.
        --> VIA ENVIRONMENT: SENZING_KAFKA_LOAD_TOPIC
                             SENZING_KAFKA_TOPIC (fallback)


[ Advanced Options ]
   --config-mgr [config manager options]...
        Also --configmgr.  Should be the first option if provided.  All
        subsequent options are interpreted as configuration manager options.
        If this option is specified by itself then a help message on
        configuration manager options will be displayed.
        NOTE: If this option is provided, the server will not start.

```

## License

View [license information] for the software container in this Docker image.
Note that this license does not permit further distribution.

This Docker image may also contain software from the
[Senzing GitHub community] under the [Apache License 2.0].

Further, as with all Docker images,
this likely also contains other software which may be under other licenses
(such as Bash, etc. from the base distribution,
along with any direct or indirect dependencies of the primary software being contained).

As for any pre-built image usage,
it is the image user's responsibility to ensure that any use of this image complies
with any relevant licenses for all software contained within.

[Apache License 2.0]: https://www.apache.org/licenses/LICENSE-2.0
[Building]: #building
[Demonstrate using Command Line]: #demonstrate-using-command-line
[Dependencies]: #dependencies
[license information]: https://senzing.com/end-user-license-agreement/
[License]: #license
[Running]: #running
[Senzing API Server README.md]: https://github.com/senzing-garage/senzing-api-server
[Senzing API Server]: https://github.com/senzing-garage/senzing-api-server
[Senzing Garage]: https://github.com/senzing-garage
[Senzing GitHub community]: https://github.com/Senzing/
[Senzing POC API OAS specification]: http://editor.swagger.io/?url=https://raw.githubusercontent.com/Senzing/poc-api-server/main/senzing-poc-rest-api.yaml
[Senzing Quick Start guides]: https://docs.senzing.com/quickstart/
[Senzing]: https://senzing.com/
