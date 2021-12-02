package com.senzing.poc.server;

import java.util.*;

import com.senzing.api.server.SzApiServerOption;
import com.senzing.cmdline.*;

import static com.senzing.util.CollectionUtilities.recursivelyUnmodifiableMap;
import static com.senzing.api.server.mq.KafkaEndpoint.*;
import static com.senzing.api.server.mq.SqsEndpoint.*;
import static com.senzing.api.server.mq.RabbitEndpoint.*;
import static com.senzing.poc.server.SzPocServerConstants.*;

/**
 * Describes the command-line options for {@link SzPocServer}.
 */
public enum SzPocServerOption
    implements CommandLineOption<SzPocServerOption, SzApiServerOption>
{
  /**
   * <p>
   * This option is used to specify the URL to an Amazon SQS queue to be used
   * for loading records.  The single parameter to this option is the URL.  If
   * this option is specified then the load queue parameters for RabbitMQ and
   * Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--sqs-load-url {url}</tt></li>
   *   <li>Command Line: <tt>-sqsLoadUrl {url}</tt></li>
   *   <li>Environment: <tt>SENZING_SQS_LOAD_URL="{url}"</tt></tt></li>
   * </ul>
   * </p>
   */
  SQS_LOAD_URL(
      "--sqs-load-url", Set.of("-sqsLoadUrl"),
      "SENZING_SQS_LOAD_QUEUE_URL",
      List.of("SENZING_SQS_QUEUE_URL"), 1,
      SQS_LOAD_QUEUE_GROUP, URL_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the user name for connecting to RabbitMQ as
   * part of specifying a RabbitMQ load queue.  The single parameter to this
   * option is a user name.  If this option is specified then the other options
   * required for a RabbitMQ load queue are required and the load queue
   * parameters pertaining to SQS and Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--rabbit-load-host {username}</tt></li>
   *   <li>Command Line: <tt>-rabbitLoadHost {username}</tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_LOAD_USERNAME="{username}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_USERNAME="{username}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  RABBIT_LOAD_USER(
      "--rabbit-load-user", Set.of("-rabbitLoadUser"),
      "SENZING_RABBITMQ_LOAD_USERNAME",
      List.of("SENZING_RABBITMQ_USERNAME"), 1,
      RABBITMQ_LOAD_QUEUE_GROUP, USER_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the password for connecting to RabbitMQ as
   * part of specifying a RabbitMQ load queue.  The single parameter to this
   * option is a password.  If this option is specified then the other options
   * required for a RabbitMQ load queue are required and the load queue
   * parameters pertaining to SQS and Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--rabbit-load-password {password}</tt></li>
   *   <li>Command Line: <tt>-rabbitLoadPassword {password}</tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_LOAD_PASSWORD="{password}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_PASSWORD="{password}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  RABBIT_LOAD_PASSWORD(
      "--rabbit-load-password", Set.of("-rabbitLoadPassword"),
      "SENZING_RABBITMQ_LOAD_PASSWORD",
      List.of("SENZING_RABBITMQ_PASSWORD"), 1,
      RABBITMQ_LOAD_QUEUE_GROUP, PASSWORD_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the hostname for connecting to RabbitMQ as
   * part of specifying a RabbitMQ load queue.  The single parameter to this
   * option is a hostname or IP address.  If this option is specified then the
   * other options required for a RabbitMQ load queue are required and the
   * load queue parameters pertaining to SQS and Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--rabbit-load-host {hostname}</tt></li>
   *   <li>Command Line: <tt>-rabbitLoadHost {hostname}</tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_LOAD_HOST="{hostname}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_HOST="{hostname}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  RABBIT_LOAD_HOST(
      "--rabbit-load-host", Set.of("-rabbitLoadHost"),
      "SENZING_RABBITMQ_LOAD_HOST",
      List.of("SENZING_RABBITMQ_HOST"), 1,
      RABBITMQ_LOAD_QUEUE_GROUP, HOST_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the port number for connecting to RabbitMQ
   * as part of specifying a RabbitMQ load queue.  The single parameter to this
   * option is a port number.  If this option is specified then the other
   * options required for a RabbitMQ load queue are required and the load queue
   * parameters pertaining to SQS and Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--rabbit-load-port {port}</tt></li>
   *   <li>Command Line: <tt>-rabbitLoadPort {port}</tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_LOAD_PORT="{port}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_PORT="{port}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  RABBIT_LOAD_PORT(
      "--rabbit-load-port", Set.of("-rabbitLoadPort"),
      "SENZING_RABBITMQ_LOAD_PORT",
      List.of("SENZING_RABBITMQ_PORT"), 1,
      RABBITMQ_LOAD_QUEUE_GROUP, PORT_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the virtual host for connecting to RabbitMQ
   * as part of specifying a RabbitMQ load queue.  The single parameter to this
   * option is a virtual host name.  If this option is specified then the other
   * options required for a RabbitMQ load queue are required and the load queue
   * parameters pertaining to SQS and Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--rabbit-load-virtual-host {virtual-host}</tt></li>
   *   <li>Command Line: <tt>-rabbitLoadVirtualHost {virtual-host}</tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_LOAD_VIRTUAL_HOST="{virtual-host}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_VIRTUAL_HOST="{virtual-host}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  RABBIT_LOAD_VIRTUAL_HOST(
      "--rabbit-load-virtual-host", Set.of("-rabbitLoadVirtualHost"),
      "SENZING_RABBITMQ_LOAD_VIRTUAL_HOST",
      List.of("SENZING_RABBITMQ_VIRTUAL_HOST"), 1,
      RABBITMQ_LOAD_QUEUE_GROUP, VIRTUAL_HOST_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the exchange for connecting to RabbitMQ
   * as part of specifying a RabbitMQ load queue.  The single parameter to this
   * option is an exchange name.  If this option is specified then the other
   * options required for a RabbitMQ load queue are required and the load queue
   * parameters pertaining to SQS and Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--rabbit-load-exchange {exchange}</tt></li>
   *   <li>Command Line: <tt>-rabbitLoadExchange {exchange}</tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_LOAD_EXCHANGE="{exchange}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_EXCHANGE="{exchange}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  RABBIT_LOAD_EXCHANGE(
      "--rabbit-load-exchange", Set.of("-rabbitLoadExchange"),
      "SENZING_RABBITMQ_LOAD_EXCHANGE",
      List.of("SENZING_RABBITMQ_EXCHANGE"), 1,
      RABBITMQ_LOAD_QUEUE_GROUP, EXCHANGE_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the routing key for connecting to RabbitMQ
   * as part of specifying a RabbitMQ load queue.  The single parameter to this
   * option is a routing key.  If this option is specified then the other
   * options required for a RabbitMQ load queue are required and the load queue
   * parameters pertaining to SQS and Kafka are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--rabbit-load-routing-key {routing-key}</tt></li>
   *   <li>Command Line: <tt>-rabbitLoadRoutingKey {routing-key}</tt></li>
   *   <li>Environment: <tt>SENZING_RABBITMQ_LOAD_ROUTING_KEY="{routing-key}"</tt></tt></li>
   * </ul>
   * </p>
   */
  RABBIT_LOAD_ROUTING_KEY(
      "--rabbit-load-routing-key", Set.of("-rabbitLoadRoutingKey"),
      "SENZING_RABBITMQ_LOAD_ROUTING_KEY",
      List.of("SENZING_RABBITMQ_ROUTING_KEY"), 1,
      RABBITMQ_LOAD_QUEUE_GROUP, ROUTING_KEY_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the bootstrap servers for connecting to
   * Kafka as part of specifying a Kafka load topic.  The single parameter to
   * this option is the Kafka bootstrap servers specification (typically a
   * hostname or IP address and port number separated by a colon).  If this
   * option is specified then the {@link #KAFKA_LOAD_TOPIC} option is required
   * and the load queue parameters pertaining to RabbitMQ and SQS are not
   * allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--kafka-load-bootstrap-servers {bootstrap-servers}</tt></li>
   *   <li>Command Line: <tt>-kafkaLoadBootstrapServers {bootstrap-servers}</tt></li>
   *   <li>Environment: <tt>SENZING_KAFKA_LOAD_BOOTSTRAP_SERVER="{bootstrap-servers}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_KAFKA_BOOTSTRAP_SERVER="{bootstrap-servers}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  KAFKA_LOAD_BOOTSTRAP_SERVER(
      "--kafka-load-bootstrap-server", Set.of("-kafkaLoadBootstrapServer"),
      "SENZING_KAFKA_LOAD_BOOTSTRAP_SERVER",
      List.of("SENZING_KAFKA_BOOTSTRAP_SERVER"), 1,
      KAFKA_LOAD_QUEUE_GROUP, BOOTSTRAP_SERVERS_PROPERTY_KEY, false),

  /**
   * <p>
   * This option is used to specify the <b>optional</b> group ID to set when
   * connecting to Kafka as part of specifying a Kafka load topic.  The single
   * parameter to this option is the Kafka group ID.  If this option is
   * specified then the {@link #KAFKA_LOAD_BOOTSTRAP_SERVER} and {@link
   * #KAFKA_LOAD_TOPIC} options are required and the load queue parameters
   * pertaining to RabbitMQ and SQS are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--kafka-load-group {group-id}</tt></li>
   *   <li>Command Line: <tt>-kafkaLoadGroup {group-id}</tt></li>
   *   <li>Environment: <tt>SENZING_KAFKA_LOAD_GROUP="{group-id}"</tt></tt></li>
   *   <li>Environment: <tt>SENZING_KAFKA_GROUP="{group-id}" (fallback)</tt></tt></li>
   * </ul>
   * </p>
   */
  KAFKA_LOAD_GROUP(
      "--kafka-load-group", Set.of("-kafkaLoadGroup"),
      "SENZING_KAFKA_LOAD_GROUP",
      List.of("SENZING_KAFKA_GROUP"), 1, KAFKA_LOAD_QUEUE_GROUP,
      GROUP_ID_PROPERTY_KEY, true),

  /**
   * <p>
   * This option is used to specify the topic when connecting to Kafka as part
   * of specifying a Kafka load topic.  The single parameter to this option is
   * the Kafka topic.  If this option is specified then the {@link
   * #KAFKA_LOAD_BOOTSTRAP_SERVER} option is required and the load queue
   * parameters pertaining to RabbitMQ and SQS are not allowed.
   * </p>
   * <p>
   * This option can be specified in the following ways:
   * <ul>
   *   <li>Command Line: <tt>--kafka-load-topic {topic-name}</tt></li>
   *   <li>Command Line: <tt>-kafkaLoadTopic {topic-name}</tt></li>
   *   <li>Environment: <tt>SENZING_KAFKA_LOAD_TOPIC="{topic-name}"</tt></tt></li>
   * </ul>
   * </p>
   */
  KAFKA_LOAD_TOPIC(
      "--kafka-load-topic", Set.of("-kafkaLoadTopic"),
      "SENZING_KAFKA_LOAD_TOPIC",
      List.of("SENZING_KAFKA_TOPIC"), 1,
      KAFKA_LOAD_QUEUE_GROUP, TOPIC_PROPERTY_KEY, false);

  /**
   * The {@link Map} of {@link SzPocServerOption} keys to unmodifiable
   * {@link Set} values containing the {@link SzPocServerOption} values that
   * conflict with the key {@link SzPocServerOption} value.
   */
  private static Map<SzPocServerOption, Set<CommandLineOption>> CONFLICTING_OPTIONS;

  /**
   * The {@link Map} of {@link String} option flags to their corresponding
   * {@link SzPocServerOption} values.
   */
  private static Map<String, SzPocServerOption> OPTIONS_BY_FLAG;

  /**
   * The {@link Map} of {@link SzPocServerOption} keys to <b>unmodifiable</b>
   * {@link Set} values containing alternative {@link Set}'s of {@link
   * SzPocServerOption} that the key option is dependent on if specified.
   */
  private static Map<SzPocServerOption, Set<Set<CommandLineOption>>> DEPENDENCIES;

  /**
   * Flag indicating if this option is considered a "primary" option.
   */
  private boolean primary;

  /**
   * Flag indicating if this option is considered a deprecated option.
   */
  private boolean deprecated;

  /**
   * The primary command-line flag.
   */
  private String cmdLineFlag;

  /**
   * The {@link Set} of synonym command-line flags for this option.
   */
  private Set<String> synonymFlags;

  /**
   * The optional environment variable associated with option.
   */
  private String envVariable;

  /**
   * The environment variable fallbacks for this option.
   */
  private List<String> envFallbacks;

  /**
   * The minimum number of parameters that can be specified for this option.
   */
  private int minParamCount;

  /**
   * The maximum number of parmaeters that can be specifeid for this option.
   */
  private int maxParamCount;

  /**
   * The {@link List} o {@link String} default parameters for this option.  This
   * is <tt>null</tt> if no default and an empty {@link List} if the option is
   * specified by default with no parameters.
   */
  private List<String> defaultParameters;

  /**
   * The group name for the option group that this parameter belongs to.
   */
  private String groupName;

  /**
   * The property key to map the option to for the group for initializing a
   * sub-object with the options in that group.
   */
  private String groupPropertyKey;

  /**
   * The property indicating if the option is not required for the validity of
   * the group to which it belongs.
   */
  private boolean groupOptional;

  SzPocServerOption(String      cmdLineFlag,
                    Set<String> synonymFlags,
                    int         parameterCount)
  {
    this(cmdLineFlag,
         synonymFlags,
         null,
         null,
         false,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         Collections.emptyList(),
         false,
         null,
         null,
         true);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    int           parameterCount,
                    String...     defaultParams) {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         false,
         parameterCount,
         List.of(defaultParams),
         false,
         null,
         null,
         true);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    int           minParamCount,
                    int           maxParamCount,
                    String...     defaultParams) {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         false,
         minParamCount,
         maxParamCount,
         List.of(defaultParams),
         false,
         null,
         null,
         true);
  }

  SzPocServerOption(String      cmdLineFlag,
                    Set<String> synonymFlags,
                    int         parameterCount,
                    String      groupName,
                    String      groupPropertyKey,
                    boolean     groupOptional)
  {
    this(cmdLineFlag,
         synonymFlags,
         null,
         null,
         false,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         Collections.emptyList(),
         false,
         groupName,
         groupPropertyKey,
         groupOptional);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    int           parameterCount,
                    String        groupName,
                    String        groupPropertyKey,
                    boolean       groupOptional)
  {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         false,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         Collections.emptyList(),
         false,
         groupName,
         groupPropertyKey,
         groupOptional);
  }

  SzPocServerOption(String      cmdLineFlag,
                    Set<String> synonymFlags,
                    boolean     primary,
                    int         parameterCount)
  {
    this(cmdLineFlag,
         synonymFlags,
         null,
         null,
         primary,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         Collections.emptyList(),
         false,
         null,
         null,
         true);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    boolean       primary,
                    int           parameterCount)
  {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         primary,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         Collections.emptyList(),
         false,
         null,
         null,
         true);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    int           parameterCount)
  {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         false,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         Collections.emptyList(),
         false,
         null,
         null,
         true);
  }

  SzPocServerOption(String      cmdLineFlag,
                    Set<String> synonymFlags,
                    int         parameterCount,
                    String...   defaultParams)
  {
    this(cmdLineFlag,
         synonymFlags,
         null,
         null,
         false,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         List.of(defaultParams),
         false,
         null,
         null,
         true);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    boolean       primary,
                    int           parameterCount,
                    boolean       deprecated,
                    String        groupName,
                    String        groupPropertyKey,
                    boolean       groupOptional)
  {
    this(cmdLineFlag,
         synonymFlags,
         null,
         null,
         primary,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         Collections.emptyList(),
         deprecated,
         groupName,
         groupPropertyKey,
         groupOptional);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    boolean       primary,
                    int           parameterCount,
                    boolean       deprecated,
                    String        groupName,
                    String        groupPropertyKey,
                    boolean       groupOptional)
  {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         primary,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         deprecated,
         groupName,
         groupPropertyKey,
         groupOptional);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    boolean       primary,
                    int           parameterCount,
                    List<String>  defaultParameters,
                    boolean       deprecated,
                    String        groupName,
                    String        groupPropertyKey,
                    boolean       groupOptional)
  {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         primary,
         parameterCount < 0 ? 0 : parameterCount,
         parameterCount,
         defaultParameters,
         deprecated,
         groupName,
         groupPropertyKey,
         groupOptional);
  }

  SzPocServerOption(String      cmdLineFlag,
                    Set<String> synonymFlags,
                    boolean     primary,
                    int         minParameterCount,
                    int         maxParameterCount,
                    boolean     deprecated,
                    String      groupName,
                    String      groupPropertyKey,
                    boolean     groupOptional)
  {
    this(cmdLineFlag,
         synonymFlags,
         null,
         null,
         primary,
         minParameterCount,
         maxParameterCount,
         null,
         deprecated,
         groupName,
         groupPropertyKey,
         groupOptional);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    boolean       primary,
                    int           minParameterCount,
                    int           maxParameterCount,
                    boolean       deprecated,
                    String        groupName,
                    String        groupPropertyKey,
                    boolean       groupOptional)
  {
    this(cmdLineFlag,
         synonymFlags,
         envVariable,
         envFallbacks,
         primary,
         minParameterCount,
         maxParameterCount,
         null,
         deprecated,
         groupName,
         groupPropertyKey,
         groupOptional);
  }

  SzPocServerOption(String        cmdLineFlag,
                    Set<String>   synonymFlags,
                    String        envVariable,
                    List<String>  envFallbacks,
                    boolean       primary,
                    int           minParameterCount,
                    int           maxParameterCount,
                    List<String>  defaultParameters,
                    boolean       deprecated,
                    String        groupName,
                    String        groupPropertyKey,
                    boolean       groupOptional)
  {
    this.cmdLineFlag        = cmdLineFlag;
    this.synonymFlags       = Set.copyOf(synonymFlags);
    this.envVariable        = envVariable;
    this.primary            = primary;
    this.minParamCount      = minParameterCount;
    this.maxParamCount      = maxParameterCount;
    this.deprecated         = deprecated;
    this.groupName          = groupName;
    this.groupPropertyKey   = groupPropertyKey;
    this.groupOptional      = groupOptional;
    this.envFallbacks       = (envFallbacks == null)
        ? Collections.emptyList() : List.copyOf(envFallbacks);
    this.defaultParameters  = (defaultParameters == null)
        ? Collections.emptyList() : List.copyOf(defaultParameters);
  }

  /**
   * Overridden to return {@link SzApiServerOption}.
   */
  @Override
  public Class<SzApiServerOption> getBaseOptionType() {
    return SzApiServerOption.class;
  }

  @Override
  public int getMinimumParameterCount() { return this.minParamCount; }

  @Override
  public int getMaximumParameterCount() { return this.maxParamCount; }

  @Override
  public List<String> getDefaultParameters() {
    return this.defaultParameters;
  }

  public boolean isPrimary() {
    return this.primary;
  }

  @Override
  public boolean isDeprecated() {
    return this.deprecated;
  }

  @Override
  public String getCommandLineFlag() {
    return this.cmdLineFlag;
  }

  @Override
  public Set<String> getSynonymFlags() {
    return this.synonymFlags;
  }

  @Override
  public String getEnvironmentVariable() {
    return this.envVariable;
  }

  @Override
  public List<String> getEnvironmentFallbacks() {
    return this.envFallbacks;
  }

  public String getGroupName() {
    return this.groupName;
  }

  public String getGroupPropertyKey() {
    return this.groupPropertyKey;
  }

  public boolean isGroupOptional() {
    return this.groupOptional;
  }

  @Override
  public Set<CommandLineOption> getConflicts() {
    return CONFLICTING_OPTIONS.get(this);
  }

  @Override
  public Set<Set<CommandLineOption>> getDependencies() {
    Set<Set<CommandLineOption>> set = DEPENDENCIES.get(this);
    return (set == null) ? Collections.emptySet() : set;
  }

  public static SzPocServerOption lookup(String commandLineFlag) {
    return OPTIONS_BY_FLAG.get(commandLineFlag.toLowerCase());
  }

  static {
    try {
      Map<SzPocServerOption,Set<CommandLineOption>> conflictMap = new LinkedHashMap<>();
      Map<SzPocServerOption,Set<SzPocServerOption>> altMap = new LinkedHashMap<>();
      Map<String, SzPocServerOption> lookupMap = new LinkedHashMap<>();

      for (SzPocServerOption option : SzPocServerOption.values()) {
        conflictMap.put(option, new LinkedHashSet<>());
        altMap.put(option, new LinkedHashSet<>());
        lookupMap.put(option.getCommandLineFlag().toLowerCase(), option);
      }

      CommandLineOption[] exclusiveOptions = {
          SzApiServerOption.HELP, SzApiServerOption.VERSION };
      for (SzPocServerOption option : SzPocServerOption.values()) {
        for (CommandLineOption exclOption : exclusiveOptions) {
          if (option == exclOption) continue;
          Set<CommandLineOption> set = conflictMap.get(option);
          set.add(exclOption);
        }
      }

      Set<SzPocServerOption> kafkaLoadOptions = Set.of(
          KAFKA_LOAD_BOOTSTRAP_SERVER,
          KAFKA_LOAD_GROUP,
          KAFKA_LOAD_TOPIC);

      Set<SzPocServerOption> rabbitLoadOptions = Set.of(
          RABBIT_LOAD_USER,
          RABBIT_LOAD_PASSWORD,
          RABBIT_LOAD_HOST,
          RABBIT_LOAD_PORT,
          RABBIT_LOAD_VIRTUAL_HOST,
          RABBIT_LOAD_EXCHANGE,
          RABBIT_LOAD_ROUTING_KEY);

      Set<SzPocServerOption> sqsLoadOptions = Set.of(SQS_LOAD_URL);

      // enforce that we only have one load queue
      for (SzPocServerOption option: kafkaLoadOptions) {
        Set<CommandLineOption> conflictSet = conflictMap.get(option);
        conflictSet.addAll(rabbitLoadOptions);
        conflictSet.addAll(sqsLoadOptions);
      }
      for (SzPocServerOption option: rabbitLoadOptions) {
        Set<CommandLineOption> conflictSet = conflictMap.get(option);
        conflictSet.addAll(kafkaLoadOptions);
        conflictSet.addAll(sqsLoadOptions);
      }
      for (SzPocServerOption option: sqsLoadOptions) {
        Set<CommandLineOption> conflictSet = conflictMap.get(option);
        conflictSet.addAll(kafkaLoadOptions);
        conflictSet.addAll(rabbitLoadOptions);
      }

      // gather a set of all load options
      Set<SzPocServerOption> loadOptions = new LinkedHashSet<>();
      loadOptions.addAll(kafkaLoadOptions);
      loadOptions.addAll(rabbitLoadOptions);
      loadOptions.addAll(sqsLoadOptions);

      // all load options conflict with read only mode
      for (SzPocServerOption option : loadOptions) {
        Set<CommandLineOption> conflicts = conflictMap.get(option);
        conflicts.add(SzApiServerOption.READ_ONLY);
      }

      Map<SzPocServerOption, Set<Set<CommandLineOption>>> dependencyMap
          = new LinkedHashMap<>();

      // handle dependencies for groups of options that go together
      Map<String, Set<SzPocServerOption>> groups = new LinkedHashMap<>();
      for (SzPocServerOption option: SzPocServerOption.values()) {
        String groupName = option.getGroupName();
        if (groupName == null) continue;
        Set<SzPocServerOption> set = groups.get(groupName);
        if (set == null) {
          set = new LinkedHashSet<>();
          groups.put(groupName, set);
        }
        set.add(option);
      }

      // create the dependencies using the groupings
      groups.forEach((groupName, group) -> {
        for (SzPocServerOption option : group) {
          Set<CommandLineOption> others = new LinkedHashSet<>(group);

          // remove self from the group (can't depend on itself)
          others.remove(option);

          // remove any options that are not required
          for (SzPocServerOption opt: group) {
            if (opt.isGroupOptional()) others.remove(opt);
          }

          // make the others set unmodifiable
          others = Collections.unmodifiableSet(others);

          // add the dependency
          dependencyMap.put(option, Set.of(others));
        }
      });

      CONFLICTING_OPTIONS = recursivelyUnmodifiableMap(conflictMap);
      OPTIONS_BY_FLAG = Collections.unmodifiableMap(lookupMap);
      DEPENDENCIES = Collections.unmodifiableMap(dependencyMap);

    } catch (Exception e) {
      e.printStackTrace();
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * The {@link ParameterProcessor} implementation for this class.
   */
  private static class ParamProcessor implements ParameterProcessor
  {
    /**
     * Processes the parameters for the specified option.
     *
     * @param option The {@link SzApiServerOption} to process.
     * @param params The {@link List} of parameters for the option.
     * @return The processed value.
     * @throws IllegalArgumentException If the specified {@link
     *         CommandLineOption} is not an instance of {@link
     *         SzApiServerOption} or is otherwise unrecognized.
     */
    public Object process(CommandLineOption option,
                          List<String>      params)
      throws BadOptionParametersException
    {
      // check if an instance of SzApiServerOption
      if (option instanceof SzApiServerOption) {
        return SzApiServerOption.PARAMETER_PROCESSOR.process(option, params);
      }

      // check if unhandled
      if (!(option instanceof SzPocServerOption)) {
        throw new IllegalArgumentException(
            "Unhandled command line option: " + option.getCommandLineFlag()
                + " / " + option);
      }

      // down-cast
      SzPocServerOption pocOption = (SzPocServerOption) option;
      switch (pocOption) {
        case KAFKA_LOAD_BOOTSTRAP_SERVER:
        case KAFKA_LOAD_GROUP:
        case KAFKA_LOAD_TOPIC:
        case RABBIT_LOAD_HOST:
        case RABBIT_LOAD_USER:
        case RABBIT_LOAD_PASSWORD:
        case RABBIT_LOAD_VIRTUAL_HOST:
        case RABBIT_LOAD_EXCHANGE:
        case RABBIT_LOAD_ROUTING_KEY:
        case SQS_LOAD_URL:
          return params.get(0);

        case RABBIT_LOAD_PORT: {
          int port = Integer.parseInt(params.get(0));
          if (port < 0) {
            throw new IllegalArgumentException(
                "Negative RabbitMQ port numbers are not allowed: " + port);
          }
          return port;
        }

        default:
          throw new IllegalArgumentException(
              "Unhandled command line option: "
                  + option.getCommandLineFlag()
                  + " / " + option);
      }
    }
  }

  /**
   * The {@link ParameterProcessor} for {@link SzPocServerOption}.
   * This will also handle instances of {@link SzApiServerOption}.
   * This instance will only handle instances of {@link CommandLineOption}
   * instances of type {@link SzApiServerOption}.
   */
  public static final ParameterProcessor PARAMETER_PROCESSOR
      = new SzPocServerOption.ParamProcessor();

}
