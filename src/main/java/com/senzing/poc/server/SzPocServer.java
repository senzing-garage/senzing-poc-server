package com.senzing.poc.server;

import com.senzing.api.model.SzMeta;
import com.senzing.api.model.SzServerInfo;
import com.senzing.api.model.SzVersionInfo;
import com.senzing.api.server.SzApiServer;
import com.senzing.api.server.SzApiServerOption;
import com.senzing.api.server.mq.SzMessagingEndpoint;
import com.senzing.api.server.mq.SzMessagingEndpointFactory;
import com.senzing.api.services.SzMessageSink;
import com.senzing.cmdline.CommandLineOption;
import com.senzing.cmdline.CommandLineUtilities;
import com.senzing.cmdline.CommandLineValue;
import com.senzing.cmdline.CommandLineException;
import com.senzing.cmdline.MissingDependenciesException;
import com.senzing.cmdline.DeprecatedOptionWarning;
import com.senzing.poc.model.SzPocMeta;
import com.senzing.poc.model.SzPocServerInfo;
import com.senzing.poc.model.SzPocVersionInfo;
import com.senzing.util.AccessToken;
import com.senzing.datamart.SzReplicator;
import com.senzing.listener.communication.sql.SQLConsumer;
import com.senzing.datamart.SzReplicator;
import com.senzing.datamart.SzReplicationProvider;
import com.senzing.datamart.SzReplicatorOption;
import com.senzing.datamart.SzReplicatorOptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import com.senzing.poc.BuildInfo;
import com.senzing.util.LoggingUtilities;
import com.senzing.util.JsonUtilities;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static com.senzing.api.server.SzApiServerOption.*;
import static com.senzing.cmdline.CommandLineUtilities.getJarName;
import static com.senzing.poc.server.SzPocServerConstants.*;
import static com.senzing.poc.server.SzPocServerOption.*;
import static com.senzing.util.LoggingUtilities.multilineFormat;
import static com.senzing.poc.BuildInfo.*;

/**
 * Extends {@link SzApiServer} to provide functions specific to the
 * proof-of-concept server to handle Senzing's one-day proof-of-concept
 * functionality.
 *
 */
public class SzPocServer extends SzApiServer implements SzPocProvider {
  static {
    LoggingUtilities.setProductIdForPackage("com.senzing.poc",
                                            "5026");
  }

  /**
   * The description of the server: {@value}.
   */
  public static final String SERVER_DESCRIPTION = "Senzing POC API Server";

  /**
   * The {@link Map} to convert {@link SzPocServerOption} instances to 
   * {@link SzReplicatorOption} instances for initialization of the
   * contained {@link SzReplicator}.
   */
  private static final Map<CommandLineOption, SzReplicatorOption> DATA_MART_OPTION_MAP;

  static {
    Map<CommandLineOption, SzReplicatorOption> map = new LinkedHashMap<>();
    try {
      map.put(SzApiServerOption.INI_FILE, SzReplicatorOption.INI_FILE);
      map.put(SzApiServerOption.INIT_FILE, SzReplicatorOption.INIT_FILE);
      map.put(SzApiServerOption.INIT_JSON, SzReplicatorOption.INIT_JSON);
      map.put(SzApiServerOption.MODULE_NAME, SzReplicatorOption.MODULE_NAME);
      map.put(SzApiServerOption.VERBOSE, SzReplicatorOption.VERBOSE);
      map.put(SzPocServerOption.SQLITE_DATABASE_FILE, 
              SzReplicatorOption.SQLITE_DATABASE_FILE);
      map.put(SzPocServerOption.POSTGRESQL_HOST, SzReplicatorOption.POSTGRESQL_HOST);
      map.put(SzPocServerOption.POSTGRESQL_PORT, SzReplicatorOption.POSTGRESQL_PORT);
      map.put(SzPocServerOption.POSTGRESQL_DATABASE, 
              SzReplicatorOption.POSTGRESQL_DATABASE);
      map.put(SzPocServerOption.POSTGRESQL_USER, SzReplicatorOption.POSTGRESQL_USER);
      map.put(SzPocServerOption.POSTGRESQL_PASSWORD, 
              SzReplicatorOption.POSTGRESQL_PASSWORD);
    } finally {
      DATA_MART_OPTION_MAP = Collections.unmodifiableMap(map);
    }
  }

  /**
   * The JAR file name for starting the {@link SzPocServer}.
   */
  private static final String JAR_FILE_NAME = getJarName(SzPocServer.class);

  /**
   * The {@link SzMessagingEndpoint} to use for asynchronous load messages.
   */
  private SzMessagingEndpoint loadEndpoint;

  /**
   * The {@link Map} of Web Socket implementation classes to the {@link String}
   * path endpoints.
   */
  private Map<Class, String> webSocketClasses = null;

  /**
   * The {@link SzReplicator} to use for populating the data mart.
   */
  private SzReplicator replicator = null;

  /**
   * The {@link SQLConsumer.MessageQueue} for logging the INFO messages 
   * for consumption by the {@link SzReplicator}.
   */
  private SQLConsumer.MessageQueue sqlMessageQueue = null;

  /**
   * Constructs with the specified {@link SzPocServerOptions} instance.
   *
   * @param options The {@link SzPocServerOptions} instance.
   * @throws Exception If a failure occurs.
   */
  public SzPocServer(SzPocServerOptions options) throws Exception {
    this(null, options);
  }

  /**
   * Constructs with the specified {@Link Map} of {@link CommandLineOption}
   * keys to {@link Object} command-line values.
   *
   * @param options The {@link Map} of {@link CommandLineOption} keys to
   *                {@link }
   * @throws Exception If a failure occurs.
   */
  public SzPocServer(Map<CommandLineOption, Object> options)
      throws Exception {
    this(null, options);
  }

  /**
   * Constructs with the specified {@link AccessToken} and {@Link
   * SzPocServerOptions} instance.  Only the holder of the specified
   * {@link AccessToken} can shutdown the constructed server instance.
   *
   * @param accessToken The {@link AccessToken} to authorize shutdown.
   * @param options     The {@link Map} of {@link CommandLineOption} keys to
   *                    {@link }
   * @throws Exception If a failure occurs.
   */
  public SzPocServer(AccessToken accessToken, SzPocServerOptions options)
      throws Exception {
    this(accessToken, options.buildOptionsMap());
  }

  /**
   * Constructs with the specified {@link AccessToken} and {@Link Map} of
   * {@link CommandLineOption} keys to {@link Object} command-line values.
   * Only the holder of the specified {@link AccessToken} can shutdown the
   * constructed server instance.
   *
   * @param accessToken The {@link AccessToken} to authorize shutdown.
   * @param options     The {@link Map} of {@link CommandLineOption} keys to
   *                    {@link }
   * @throws Exception If a failure occurs.
   */
  public SzPocServer(AccessToken accessToken,
                     Map<CommandLineOption, Object> options)
      throws Exception {
    super(accessToken, options, false);

    Map<SzReplicatorOption, Object> dataMartOptionMap = new LinkedHashMap<>();
    options.forEach((pocOption, value) -> {
      SzReplicatorOption dataMartOption = DATA_MART_OPTION_MAP.get(pocOption);
      if (dataMartOption != null) {
        dataMartOptionMap.put(DATA_MART_OPTION_MAP.get(pocOption), value);
      }
    });

    // check for INIT_ENV_VAR option
    if (options.containsKey(INIT_ENV_VAR)) {
      String envVar = (String) options.get(INIT_ENV_VAR);
      String initJson = System.getenv(envVar);
      if (initJson == null) {
        throw new IllegalArgumentException(
          "Failed to find Senzing INIT JSON via specified "
          + "environment variable: " + envVar);
      }
      JsonObject initObj = JsonUtilities.parseJsonObject(initJson);
      dataMartOptionMap.put(SzReplicatorOption.INIT_JSON, initObj);
    }

    // determine the data mart concurrency (1 or 2)
    Integer pocConcurrency = (Integer) options.get(SzApiServerOption.CONCURRENCY);
    int concurrency = 1;
    System.err.println("POC CONCURRENCY: " + pocConcurrency);
    if (pocConcurrency != null && pocConcurrency > 1) {
      concurrency = 2;
    }
    dataMartOptionMap.put(SzReplicatorOption.CONCURRENCY, concurrency);
    dataMartOptionMap.put(SzReplicatorOption.DATABASE_INFO_QUEUE, true);

    SzReplicatorOptions replicatorOptions 
      = SzReplicatorOptions.build(dataMartOptionMap);

    System.err.println();
    System.err.println(replicatorOptions.toJson());
    System.err.println();

    this.replicator       = new SzReplicator(replicatorOptions);
    this.sqlMessageQueue  = this.replicator.getDatabaseMessageQueue();

    Map<String, Map<String, Object>> optionGroups = new LinkedHashMap<>();

    // organize options into option groups
    options.forEach((option, optionValue) -> {
      // check if its an API server option
      if (optionValue == null) return;
      if (!(option instanceof SzPocServerOption)) return;
      SzPocServerOption pocOption = (SzPocServerOption) option;
      String groupName = pocOption.getGroupName();
      if (groupName == null) return;
      Map<String, Object> groupMap = optionGroups.get(groupName);
      if (groupMap == null) {
        groupMap = new LinkedHashMap<>();
        optionGroups.put(groupName, groupMap);
      }
      String groupProp = pocOption.getGroupPropertyKey();
      groupMap.put(groupProp, optionValue);
    });

    // count the number of specified info queues
    Map<String, Object> loadQueueProps = null;
    for (String key : LOAD_QUEUE_GROUPS) {
      if (!optionGroups.containsKey(key)) continue;
      loadQueueProps = optionGroups.get(key);
      break;
    }

    // build the load endpoint
    this.loadEndpoint = (loadQueueProps == null) ? null
        : SzMessagingEndpointFactory.createEndpoint(loadQueueProps,
                                                    this.getConcurrency());

    this.startHttpServer(options);
    this.replicator.start();
  }

  /**
   * Creates a new instance of {@link SzPocServer} from the specified options.
   *
   * @param options The {@link Map} of {@link CommandLineOption} keys to
   *                {@link Object} values.
   * @return The created instance of {@link SzPocServer}.
   * @throws Exception If a failure occurs.
   */
  private static SzPocServer build(Map<CommandLineOption, Object> options)
      throws Exception {
    return new SzPocServer(options);
  }

  /**
   * Prints the introduction to the usage message to the specified {@link
   * PrintWriter}.
   *
   * @param pw The {@link PrintWriter} to write the usage introduction to.
   */
  protected static void printUsageIntro(PrintWriter pw) {
    pw.println(multilineFormat(
        "java -jar " + JAR_FILE_NAME + " <options>",
        "",
        "<options> includes: ",
        ""));
  }

  /**
   * Prints the data-mart database connectivity options usage to the specified
   * {@link PrintWriter}.
   *
   * @param pw The {@link PrintWriter} to write the data-mart database
   *           connectivity options usage.
   */
  protected static void printDatabaseOptionsUsage(PrintWriter pw) {
    pw.println(multilineFormat(
        "[ Data Mart Database Connectivity Options ]",
        "   The following options pertain to configuring the connection to the data-mart",
        "   database.  Exactly one such database must be configured.",
        "",
        "   --sqlite-database-file <url>",
        "        Specifies an SQLite database file to open (or create) to use as the",
        "        data-mart database.  NOTE: SQLite may be used for testing, but because",
        "        only one connection may be made, it will not scale for production use.",
        "        --> VIA ENVIRONMENT: " + SQLITE_DATABASE_FILE.getEnvironmentVariable(),
        "",
        "   --postgresql-host <hostname>",
        "        Used to specify the hostname for connecting to PostgreSQL as the ",
        "        data-mart database.",
        "        --> VIA ENVIRONMENT: " + POSTGRESQL_HOST.getEnvironmentVariable(),
        "",
        "   --postgresql-port <port>",
        "        Used to specify the port number for connecting to PostgreSQL as the ",
        "        data-mart database.",
        "        --> VIA ENVIRONMENT: " + POSTGRESQL_PORT.getEnvironmentVariable(),
        "",
        "   --postgresql-database <database>",
        "        Used to specify the database name for connecting to PostgreSQL as the ",
        "        data-mart database.",
        "        --> VIA ENVIRONMENT: " + POSTGRESQL_DATABASE.getEnvironmentVariable(),
        "",
        "   --postgresql-user <user name>",
        "        Used to specify the user name for connecting to PostgreSQL as the ",
        "        data-mart database.",
        "        --> VIA ENVIRONMENT: " + POSTGRESQL_USER.getEnvironmentVariable(),
        "",
        "   --postgresql-password <password>",
        "        Used to specify the password for connecting to PostgreSQL as the ",
        "        data-mart database.",
        "        --> VIA ENVIRONMENT: " + POSTGRESQL_PASSWORD.getEnvironmentVariable()));
  }

  /**
   * Prints the load-queue options usage to the specified {@link PrintWriter}.
   *
   * @param pw The {@link PrintWriter} to write the load-queue options usage.
   */
  protected static void printLoadQueueOptionsUsage(PrintWriter pw) {
    pw.println(multilineFormat(
        "[ *** DEPRECATED *** Asynchronous Load Queue Options ]",
        "   The following options pertain to configuring an asynchronous message",
        "   queue on which to send record messages to be loaded by the stream loader.",
        "   At most one such queue can be configured.  If a \"load\" queue is",
        "   configured then endpoints that leverage the load queue become available.",
        "   *** NOTE ***: These options are DEPRECATED since the Senzing stream",
        "   loader is being deprecated.  Loading records via the stream loader will",
        "   only update the data mart if the stream loader is publishing INFO messages",
        "   to a queue that are being consumed by a separate Data Mart Replicator",
        "   configured for the same data mart database",
        "",
        "   --sqs-load-url <url>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -sqsLoadUrl.  Specifies an Amazon SQS queue URL as the load queue.",
        "        --> VIA ENVIRONMENT: " + SQS_LOAD_URL.getEnvironmentVariable(),
        "                             "
            + SQS_LOAD_URL.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-host <hostname>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -rabbitLoadHost.  Used to specify the hostname for connecting to",
        "        RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_HOST.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_HOST.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-port <port>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -rabbitLoadPort.  Used to specify the port number for connecting",
        "        to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_PORT.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_PORT.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-user <user name>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -rabbitLoadUser.  Used to specify the user name for connecting to",
        "        RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_USER.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_USER.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-password <password>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -rabbitLoadPassword.  Used to specify the password for connecting",
        "        to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_PASSWORD.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_PASSWORD.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-virtual-host <virtual host>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -rabbitLoadVirtualHost.  Used to specify the virtual host for",
        "        connecting to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_VIRTUAL_HOST.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_VIRTUAL_HOST.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-exchange <exchange>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -rabbitLoadExchange.  Used to specify the exchange for connecting",
        "        to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_EXCHANGE.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_EXCHANGE.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-routing-key <routing key>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -rabbitLoadRoutingKey.  Used to specify the routing key for",
        "        connecting to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_ROUTING_KEY.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_ROUTING_KEY.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --kafka-load-bootstrap-server <bootstrap servers>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -kafkaLoadBootstrapServer.  Used to specify the bootstrap servers",
        "        for connecting to Kafka as part of specifying a Kafka load topic.",
        "        --> VIA ENVIRONMENT: " + KAFKA_LOAD_BOOTSTRAP_SERVER.getEnvironmentVariable(),
        "                             "
            + KAFKA_LOAD_BOOTSTRAP_SERVER.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --kafka-load-group <group id>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -kafkaLoadGroupId.  Used to specify the group ID for connecting to",
        "        Kafka as part of specifying a Kafka load topic.",
        "        --> VIA ENVIRONMENT: " + KAFKA_LOAD_GROUP.getEnvironmentVariable(),
        "                             "
            + KAFKA_LOAD_GROUP.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --kafka-load-topic <topic>",
        "        *** DEPRECATED: See deprecation note above ***",
        "        Also -kafkaLoadTopic.  Used to specify the topic name for connecting to",
        "        Kafka as part of specifying a Kafka load topic.",
        "        --> VIA ENVIRONMENT: " + KAFKA_LOAD_TOPIC.getEnvironmentVariable(),
        "                             "
            + KAFKA_LOAD_TOPIC.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        ""));
  }

  /**
   * Provides the usage string for the {@link SzPocServer}.
   *
   * @return The usage string for the {@link SzPocServer}.
   */
  public static String getUsageString() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println();
    printUsageIntro(pw);
    printStandardOptionsUsage(pw);
    printDatabaseOptionsUsage(pw);
    printSslOptionsUsage(pw);
    printInfoQueueOptionsUsage(pw);
    printLoadQueueOptionsUsage(pw);
    printAdvancedOptionsUsage(pw);

    pw.println();
    pw.flush();
    sw.flush();

    return sw.toString();
  }

  /**
   * Returns a formatted string describing the version details of the
   * API Server.
   *
   * @return A formatted string describing the version details.
   */
  protected static String getVersionString() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    printJarVersion(pw);
    printSenzingVersions(pw);
    return sw.toString();
  }

  /**
   * Prints the JAR version header for the version string to the specified
   * {@link PrintWriter}.
   *
   * @param pw The {@link PrintWriter} to print the version information to.
   */
  protected static void printJarVersion(PrintWriter pw) {
    pw.println("[ " + JAR_FILE_NAME + " version "
                   + BuildInfo.MAVEN_VERSION + " ]");
  }

  /**
   * Prints the Senzing version information to the specified {@link
   * PrintWriter}.
   *
   * @param pw The {@link PrintWriter} to print the version information to.
   */
  protected static void printSenzingVersions(PrintWriter pw) {
    pw.println(" - POC Server Version           : " + MAVEN_VERSION);
    pw.println(" - POC Specification Version    : " + POC_REST_API_VERSION);
    SzApiServer.printSenzingVersions(pw);
  }

  /**
   * Parses the {@link SzPocServer} command line arguments and produces a
   * {@link Map} of {@link CommandLineOption} keys to {@link Object} command
   * line values.
   *
   * @param args The arguments to parse.
   *
   * @return The {@link Map} describing the command-line arguments.
   */
  protected static Map<CommandLineOption, Object> parseCommandLine(
      String[] args, List<DeprecatedOptionWarning> deprecationWarnings)
      throws CommandLineException
  {
    Map<CommandLineOption, CommandLineValue> optionValues
        = CommandLineUtilities.parseCommandLine(
        SzPocServerOption.class,
        args,
        SzPocServerOption.PARAMETER_PROCESSOR,
        deprecationWarnings);

    // check for the INI_FILE, INIT_FILE and INIT_JSON options
    SzApiServerOption[] initOptions = { INI_FILE, INIT_FILE, INIT_JSON };
    SzApiServerOption   initOption  = null;
    for (SzApiServerOption option : initOptions) {
      if (optionValues.containsKey(option)) {
        initOption = option;
        break;
      }
    }

    // check for the database options
    if (initOption != null) {
      if (!optionValues.containsKey(SQLITE_DATABASE_FILE)
          && !optionValues.containsKey(POSTGRESQL_HOST)) 
      {
        // get the command-line value for the init option
        CommandLineValue initValue = optionValues.get(initOption);

        Set<CommandLineOption> sqliteOptions = Set.of(SQLITE_DATABASE_FILE);
        Set<CommandLineOption> postgresqlOptions = Set.of(
          POSTGRESQL_HOST, POSTGRESQL_PORT, POSTGRESQL_DATABASE, 
          POSTGRESQL_USER, POSTGRESQL_PASSWORD);

        Set<Set<CommandLineOption>> dependencySets
          = Set.of(sqliteOptions, postgresqlOptions);

        throw new MissingDependenciesException(initValue.getSource(),
                                               initValue.getOption(),
                                               dependencySets,
                                               initValue.getSpecifier(),
                                               optionValues.keySet());
      }
    }
    
    // create a result map
    Map<CommandLineOption, Object> result = new LinkedHashMap<>();

    // iterate over the option values and handle them
    JsonObjectBuilder job = Json.createObjectBuilder();
    job.add("message", "Startup Options");

    StringBuilder sb = new StringBuilder();

    CommandLineUtilities.processCommandLine(optionValues, result, job, sb);

    // log the options
    if (!optionValues.containsKey(HELP) && !optionValues.containsKey(VERSION)) {
      System.out.println(
          "[" + (new Date()) + "] Senzing POC Server: " + sb.toString());
    }

    // return the result
    return result;
  }

  /**
   * Handles execution from the command line.
   *
   * @param args The command line arguments.
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    commandLineStart(args,
                     SzPocServer::parseCommandLine,
                     SzPocServer::getUsageString,
                     SzPocServer::getVersionString,
                     SzPocServer::build);
  }

  /**
   * Overridden to return the name of the server given by
   * {@link #SERVER_DESCRIPTION}.
   *
   * @return The description of the server.
   */
  @Override
  public String getDescription() {
    return SERVER_DESCRIPTION;
  }

  @Override
  public boolean hasConfiguredInfoSink() {
    return super.hasInfoSink();
  }

  @Override
  public SzReplicationProvider getReplicationProvider() {
    return this.replicator.getReplicationProvider();
  }

  @Override
  public boolean hasInfoSink() {
    return true;
  }

  @Override
  public SzMessageSink acquireInfoSink() {
    SzMessageSink baseSink = super.acquireInfoSink();
    return new SzDataMartMessageSink(this.sqlMessageQueue, baseSink);
  }

  @Override
  public void releaseInfoSink(SzMessageSink sink) {
    SzDataMartMessageSink dataMartSink  = (SzDataMartMessageSink) sink;
    SzMessageSink         backingSink   = dataMartSink.getBackingSink();

    if (backingSink == null) return;

    super.releaseInfoSink(backingSink);
  }

  @Override
  public boolean hasLoadSink() {
    return (this.loadEndpoint != null);
  }

  @Override
  public SzMessageSink acquireLoadSink() {
    return (this.loadEndpoint == null) ? null
        : this.loadEndpoint.acquireMessageSink();
  }

  @Override
  public void releaseLoadSink(SzMessageSink sink) {
    if (this.loadEndpoint == null) {
      throw new IllegalStateException(
          "No load message endpoint exists for releasing the sink");
    }
    this.loadEndpoint.releaseMessageSink(sink);
  }

  /**
   * Overridden to add the classes that are specific to and overridden by the
   * POC server.  Overridden classes will automatically replace their base
   * classes to avoid conflicts.  The returned {@link List} may be modified
   * directly.
   *
   * @return The {@link List} of {@link Class} instances containing the
   *         endpoints to be added to the REST API Server.
   */
  @Override
  protected List<Class> getServicesClassList() {
    List<Class> result = super.getServicesClassList();

    try {
      // get the properties file
      Properties serviceProps = new Properties();
      serviceProps.load(
          SzPocServer.class.getResourceAsStream(SERVICES_RESOURCE_FILE));

      // loop through the keys and treat them as class names
      for (Object key : serviceProps.keySet()) {
        String className = key.toString();
        Class c = Class.forName(className);
        result.add(c);
      }

      // return the list
      return result;

    } catch (ClassNotFoundException| IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Overridden to install custom model providers specific to the POC server.
   */
  @Override
  protected void installModelProviders() {
    // add "pocServerVersion" and "pocApiVersion" to meta and version models
    SzMeta.FACTORY.installProvider(new SzPocMeta.Provider());
    SzVersionInfo.FACTORY.installProvider(new SzPocVersionInfo.Provider());

    // add "loadQueueConfigured" to server info
    SzServerInfo.FACTORY.installProvider(new SzPocServerInfo.Provider());
  }

  @Override
  /**
   * Obtains the list of Web Socket implementations from the resource properties
   * file by the name given by {@link #WEB_SOCKETS_RESOURCE_FILE}.
   *
   * @return The {@link Map} of {@link Class} objects to URL paths that they
   *         should be mapped to.
   */
  protected Map<Class, String> getWebSocketClasses() {
    synchronized (this.monitor) {
      if (this.webSocketClasses != null) return this.webSocketClasses;

      Map<Class, String> webSocketMap = super.getWebSocketClasses();

      this.populateWebSocketClasses(
          webSocketMap,
          SzPocServer.class.getResourceAsStream(WEB_SOCKETS_RESOURCE_FILE));

      return webSocketMap;
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to shutdown the embedded data mart replicator.
   */
  @Override
  protected void shutdown() {
    this.replicator.shutdown();
    super.shutdown();
  }
}
