package com.senzing.poc.server;

import com.senzing.api.model.SzMeta;
import com.senzing.api.model.SzServerInfo;
import com.senzing.api.model.SzVersionInfo;
import com.senzing.api.server.SzApiServer;
import com.senzing.api.server.mq.SzMessagingEndpoint;
import com.senzing.api.server.mq.SzMessagingEndpointFactory;
import com.senzing.api.services.SzMessageSink;
import com.senzing.cmdline.CommandLineOption;
import com.senzing.cmdline.CommandLineUtilities;
import com.senzing.cmdline.CommandLineValue;
import com.senzing.poc.model.SzPocMeta;
import com.senzing.poc.model.SzPocServerInfo;
import com.senzing.poc.model.SzPocVersionInfo;
import com.senzing.util.AccessToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import com.senzing.poc.BuildInfo;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static com.senzing.api.server.SzApiServerOption.HELP;
import static com.senzing.api.server.SzApiServerOption.VERSION;
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
  /**
   * The description of the server: {@value}.
   */
  public static final String SERVER_DESCRIPTION = "Senzing POC API Server";

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
   * Prints the load-queue options usage to the specified {@link PrintWriter}.
   *
   * @param pw The {@link PrintWriter} to write the load-queue options usage.
   */
  protected static void printLoadQueueOptionsUsage(PrintWriter pw) {
    pw.println(multilineFormat(
        "[ Asynchronous Load Queue Options ]",
        "   The following options pertain to configuring an asynchronous message",
        "   queue on which to send record messages to be loaded by the stream loader.",
        "   At most one such queue can be configured.  If a \"load\" queue is",
        "   configured then endpoints that leverage the load queue become available.",
        "",
        "   --sqs-load-url <url>",
        "        Also -sqsLoadUrl.  Specifies an Amazon SQS queue URL as the load queue.",
        "        --> VIA ENVIRONMENT: " + SQS_LOAD_URL.getEnvironmentVariable(),
        "",
        "   --rabbit-load-host <hostname>",
        "        Also -rabbitLoadHost.  Used to specify the hostname for connecting to",
        "        RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_HOST.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_HOST.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-port <port>",
        "        Also -rabbitLoadPort.  Used to specify the port number for connecting",
        "        to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_PORT.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_PORT.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-user <user name>",
        "        Also -rabbitLoadUser.  Used to specify the user name for connecting to",
        "        RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_USER.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_USER.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-password <password>",
        "        Also -rabbitLoadPassword.  Used to specify the password for connecting",
        "        to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_PASSWORD.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_PASSWORD.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-virtual-host <virtual host>",
        "        Also -rabbitLoadVirtualHost.  Used to specify the virtual host for",
        "        connecting to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_VIRTUAL_HOST.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_VIRTUAL_HOST.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-exchange <exchange>",
        "        Also -rabbitLoadExchange.  Used to specify the exchange for connecting",
        "        to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_EXCHANGE.getEnvironmentVariable(),
        "                             "
            + RABBIT_LOAD_EXCHANGE.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --rabbit-load-routing-key <routing key>",
        "        Also -rabbitLoadRoutingKey.  Used to specify the routing key for",
        "        connecting to RabbitMQ as part of specifying a RabbitMQ load queue.",
        "        --> VIA ENVIRONMENT: " + RABBIT_LOAD_ROUTING_KEY.getEnvironmentVariable(),
        "",
        "   --kafka-load-bootstrap-server <bootstrap servers>",
        "        Also -kafkaLoadBootstrapServer.  Used to specify the bootstrap servers",
        "        for connecting to Kafka as part of specifying a Kafka load topic.",
        "        --> VIA ENVIRONMENT: " + KAFKA_LOAD_BOOTSTRAP_SERVER.getEnvironmentVariable(),
        "                             "
            + KAFKA_LOAD_BOOTSTRAP_SERVER.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --kafka-load-group <group id>",
        "        Also -kafkaLoadGroupId.  Used to specify the group ID for connecting to",
        "        Kafka as part of specifying a Kafka load topic.",
        "        --> VIA ENVIRONMENT: " + KAFKA_LOAD_GROUP.getEnvironmentVariable(),
        "                             "
            + KAFKA_LOAD_GROUP.getEnvironmentFallbacks().iterator().next()
            + " (fallback)",
        "",
        "   --kafka-load-topic <topic>",
        "        Also -kafkaLoadTopic.  Used to specify the topic name for connecting to",
        "        Kafka as part of specifying a Kafka load topic.",
        "        --> VIA ENVIRONMENT: " + KAFKA_LOAD_TOPIC.getEnvironmentVariable(),
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
  protected static Map<CommandLineOption, Object> parseCommandLine(String[] args) {
    Map<CommandLineOption, CommandLineValue> optionValues
        = CommandLineUtilities.parseCommandLine(
        SzPocServerOption.class,
        args,
        SzPocServerOption.PARAMETER_PROCESSOR);

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

}
