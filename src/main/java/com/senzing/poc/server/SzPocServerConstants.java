package com.senzing.poc.server;

import java.util.Set;

/**
 * Utility class to provide common constants pertaining to the Senzing POC
 * Server.  These are factored into their own class to avoid circular
 * dependencies.
 */
public final class SzPocServerConstants {
  /**
   * The {@link SzPocServerOption} group for the RabbitMQ load queue options.
   */
  static final String RABBITMQ_LOAD_QUEUE_GROUP = "rabbitmq-load";

  /**
   * The {@link SzPocServerOption} group for the Kafka load queue options.
   */
  static final String KAFKA_LOAD_QUEUE_GROUP = "kafka-load";

  /**
   * The {@link SzPocServerOption} group for the SQS load queue options.
   */
  static final String SQS_LOAD_QUEUE_GROUP = "sqs-load";

  /***
   * The <b>unmodifiable</b> {@link Set} of group names for load queue groups.
   */
  static final Set<String> LOAD_QUEUE_GROUPS = Set.of(
      RABBITMQ_LOAD_QUEUE_GROUP, KAFKA_LOAD_QUEUE_GROUP, SQS_LOAD_QUEUE_GROUP);

  /**
   * The prefix for environment variables used that are specific to the
   * Senzing REST API Server.
   */
  static final String ENV_PREFIX = "SENZING_POC_SERVER_";
}
