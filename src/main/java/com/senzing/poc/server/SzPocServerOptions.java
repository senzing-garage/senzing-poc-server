package com.senzing.poc.server;

import com.senzing.api.server.SzApiServer;
import com.senzing.api.server.SzApiServerOptions;
import com.senzing.cmdline.CommandLineOption;

import javax.json.JsonObject;
import java.util.Map;

import static com.senzing.poc.server.SzPocServerOption.*;

/**
 * Extends {@link SzApiServerOptions} to provide options that are specific to
 * the {@link SzPocServer}. This only adds those options specific to the
 * Senzing POC Server. The only weakness in this approach is that the builder
 * pattern whereby a reference to this instance is returned from each setter
 * method returns an instance of {@link SzApiServerOptions} on the base methods
 * and {@link SzPocServerOptions} on the derived methods, therefore setting the
 * options specific to Senzing POC Server should be done first to avoid having
 * to cast.
 */
public class SzPocServerOptions extends SzApiServerOptions {
  private String kafkaLoadServers = null;
  private String kafkaLoadGroupId = null;
  private String kafkaLoadTopic = null;
  private String rabbitLoadUser = null;
  private String rabbitLoadPassword = null;
  private String rabbitLoadHost = null;
  private Integer rabbitLoadPort = null;
  private String rabbitLoadVHost = null;
  private String rabbitLoadExchange = null;
  private String rabbitLoadRoutingKey = null;
  private String sqsLoadUrl = null;

  /**
   * Constructs with the native Senzing JSON initialization parameters as a
   * {@link JsonObject}.
   *
   * @param jsonInit The JSON initialization parameters.
   */
  public SzPocServerOptions(JsonObject jsonInit) {
    super(jsonInit);
  }

  /**
   * Constructs with the native Senzing JSON initialization parameters as JSON
   * text.
   *
   * @param jsonInitText The JSON initialization parameters as JSON text.
   */
  public SzPocServerOptions(String jsonInitText) {
    super(jsonInitText);
  }

  /**
   * Returns the Kafka bootstrap servers to connect to for the "load" queue.
   * This is part of the load queue configuration to push records to be loaded
   * by the stream loader.
   *
   * @return The Kafka boostrap servers for the "load" queue.
   */
  public String getKafkaLoadBootstrapServers() {
    return this.kafkaLoadServers;
  }

  /**
   * Sets the Kafka server to connect to for the "load" queue. This is part
   * of the load queue configuration to push records to be loaded by the stream
   * loader.
   *
   * @param servers The Kafka bootstrap servers for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setKafkaLoadBootstrapServers(String servers) {
    this.kafkaLoadServers = servers;
    return this;
  }

  /**
   * Returns the Kafka group ID to for the "load" queue. This is part of the
   * load queue configuration to push records to be loaded by the stream loader.
   *
   * @return The Kafka group ID for the "load" queue.
   */
  public String getKafkaLoadGroupId() {
    return this.kafkaLoadGroupId;
  }

  /**
   * Sets the Kafka group ID to for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @param groupId The Kafka group ID for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setKafkaLoadGroupId(String groupId) {
    this.kafkaLoadGroupId = groupId;
    return this;
  }

  /**
   * Returns the Kafka topic for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @return The Kafka topic for the "load" queue.
   */
  public String getKafkaLoadTopic() {
    return this.kafkaLoadTopic;
  }

  /**
   * Sets the Kafka topic for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @param topic The Kafka topic for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setKafkaLoadTopic(String topic) {
    this.kafkaLoadTopic = topic;
    return this;
  }

  /**
   * Returns the RabbitMQ user for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @return The RabbitMQ user for the "load" queue.
   */
  public String getRabbitLoadUser() {
    return this.rabbitLoadUser;
  }

  /**
   * Sets the RabbitMQ user for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @param user The RabbitMQ user for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setRabbitLoadUser(String user) {
    this.rabbitLoadUser = user;
    return this;
  }

  /**
   * Returns the RabbitMQ password for the "load" queue. This is part of the
   * load queue configuration to push records to be loaded by the stream loader.
   *
   * @return The RabbitMQ password for the "load" queue.
   */
  public String getRabbitLoadPassword() {
    return this.rabbitLoadPassword;
  }

  /**
   * Sets the RabbitMQ password for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @param password The RabbitMQ password for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setRabbitLoadPassword(String password) {
    this.rabbitLoadPassword = password;
    return this;
  }

  /**
   * Returns the RabbitMQ host for the "load" queue. This is part of the
   * load queue configuration to push records to be loaded by the stream loader.
   *
   * @return The RabbitMQ host for the "load" queue.
   */
  public String getRabbitLoadHost() {
    return this.rabbitLoadHost;
  }

  /**
   * Sets the RabbitMQ host for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @param host The RabbitMQ host for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setRabbitLoadHost(String host) {
    this.rabbitLoadHost = host;
    return this;
  }

  /**
   * Returns the RabbitMQ port for the "load" queue. This is part of the
   * load queue configuration to push records to be loaded by the stream loader.
   *
   * @return The RabbitMQ port for the "load" queue.
   */
  public Integer getRabbitLoadPort() {
    return this.rabbitLoadPort;
  }

  /**
   * Sets the RabbitMQ port for the "load" queue. This is part of the info
   * queue configuration to push records to be loaded by the stream loader.
   *
   * @param port The RabbitMQ port for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setRabbitLoadPort(Integer port) {
    this.rabbitLoadPort = port;
    return this;
  }

  /**
   * Returns the RabbitMQ virtual host for the "load" queue. This is part of
   * the load queue configuration to push records to be loaded by the stream
   * loader.
   *
   * @return The RabbitMQ virtual host for the "load" queue.
   */
  public String getRabbitLoadVirtualHost() {
    return this.rabbitLoadVHost;
  }

  /**
   * Sets the RabbitMQ virtual host for the "load" queue. This is part of the
   * load queue configuration to push records to be loaded by the stream loader.
   *
   * @param virtualHost The RabbitMQ virtual host for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setRabbitLoadVirtualHost(String virtualHost) {
    this.rabbitLoadVHost = virtualHost;
    return this;
  }

  /**
   * Returns the RabbitMQ exchange for the "load" queue. This is part of
   * the load queue configuration to push records to be loaded by the stream
   * loader.
   *
   * @return The RabbitMQ exchange for the "load" queue.
   */
  public String getRabbitLoadExchange() {
    return this.rabbitLoadExchange;
  }

  /**
   * Sets the RabbitMQ exchange for the "load" queue. This is part of the
   * load queue configuration to push records to be loaded by the stream loader.
   *
   * @param exchange The RabbitMQ exchange for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setRabbitLoadExchange(String exchange) {
    this.rabbitLoadExchange = exchange;
    return this;
  }

  /**
   * Returns the RabbitMQ routing key for the "load" queue. This is part of
   * the load queue configuration to push records to be loaded by the stream
   * loader.
   *
   * @return The RabbitMQ routing key for the "load" queue.
   */
  public String getRabbitLoadRoutingKey() {
    return this.rabbitLoadRoutingKey;
  }

  /**
   * Sets the RabbitMQ routing key for the "load" queue. This is part of the
   * load queue configuration to push records to be loaded by the stream loader.
   *
   * @param routingKey The RabbitMQ routing key for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setRabbitLoadRoutingKey(String routingKey) {
    this.rabbitLoadRoutingKey = routingKey;
    return this;
  }

  /**
   * Returns the SQS URL for the "load" queue. This is part of
   * the load queue configuration to push records to be loaded by the stream
   * loader.
   *
   * @return The SQS URL for the "load" queue.
   */
  public String getSqsLoadUrl() {
    return sqsLoadUrl;
  }

  /**
   * Sets the SQS URL for the "load" queue. This is part of the info queue
   * configuration to push records to be loaded by the stream loader.
   *
   * @param url The SQS URL for the "load" queue.
   *
   * @return A reference to this instance.
   */
  public SzPocServerOptions setSqsLoadUrl(String url) {
    this.sqsLoadUrl = url;
    return this;
  }

  /**
   * Creates a {@link Map} of {@link CommandLineOption} keys to {@link Object}
   * values for initializing an {@link SzApiServer} instance.
   *
   * @return The {@link Map} of {@link CommandLineOption} keys to {@link Object}
   *         values for initializing an {@link SzApiServer} instance
   */
  protected Map<CommandLineOption, Object> buildOptionsMap() {
    Map<CommandLineOption, Object> map = super.buildOptionsMap();

    put(map, KAFKA_LOAD_BOOTSTRAP_SERVER, this.getKafkaLoadBootstrapServers());
    put(map, KAFKA_LOAD_GROUP, this.getKafkaLoadGroupId());
    put(map, KAFKA_LOAD_TOPIC, this.getKafkaLoadTopic());
    put(map, RABBIT_LOAD_USER, this.getRabbitLoadUser());
    put(map, RABBIT_LOAD_PASSWORD, this.getRabbitLoadPassword());
    put(map, RABBIT_LOAD_HOST, this.getRabbitLoadHost());
    put(map, RABBIT_LOAD_PORT, this.getRabbitLoadPort());
    put(map, RABBIT_LOAD_VIRTUAL_HOST, this.getRabbitLoadVirtualHost());
    put(map, RABBIT_LOAD_EXCHANGE, this.getRabbitLoadExchange());
    put(map, RABBIT_LOAD_ROUTING_KEY, this.getRabbitLoadRoutingKey());
    put(map, SQS_LOAD_URL, this.getSqsLoadUrl());

    return map;
  }

  /**
   * Utility method to only put non-null values in the specified {@link Map}
   * with the specified {@link SzPocServerOption} key and {@link Object} value.
   *
   * @param map    The {@link Map} to put the key-value pair into.
   * @param option The {@link SzPocServerOption} key.
   * @param value  The {@link Object} value.
   */
  private static void put(Map<CommandLineOption, Object> map,
      SzPocServerOption option,
      Object value) {
    if (value != null) {
      map.put(option, value);
    }
  }

}
