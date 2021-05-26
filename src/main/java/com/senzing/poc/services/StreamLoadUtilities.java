package com.senzing.poc.services;

import com.senzing.api.services.SzMessage;
import com.senzing.util.ErrorLogSuppressor;
import com.senzing.util.JsonUtils;

import javax.json.JsonObject;
import java.util.Date;
import static com.senzing.api.services.ServicesUtil.*;

public class StreamLoadUtilities {
  /**
   * The suppression state for async load errors.
   */
  private static final ErrorLogSuppressor LOAD_ERROR_SUPPRESSOR
      = new ErrorLogSuppressor();

  /**
   * The hash of the system identity codes of the last load message exception
   * as well as the the message object that was sent.
   */
  private static final ThreadLocal<Long> LAST_LOAD_ERROR_HASH
      = new ThreadLocal<>();

  /**
   * Logs an error related to sending asynchronous load messages.
   *
   * @param e The {@link Exception} that occurred.
   * @param message The info message that failed.
   */
  public static void logFailedAsyncLoad(Exception e, SzMessage message) {
    // check what was previously logged and avoid double-logging
    Long previous = LAST_LOAD_ERROR_HASH.get();
    long hash = identityPairHash(e, message);
    if (previous != null && previous == hash) return;
    LAST_LOAD_ERROR_HASH.set(hash);

    Date    timestamp = new Date();
    String  record    = message.getBody();

    StringBuilder sb = new StringBuilder();
    sb.append(timestamp);
    sb.append(": FAILED TO ENQUEUE RECORD FOR LOAD");

    try {
      JsonObject  jsonRecord  = JsonUtils.parseJsonObject(record);
      String      prefix      = ": ";
      String      dataSource  = jsonRecord.getString("DATA_SOURCE");
      String      recordId    = jsonRecord.getString("RECORD_ID");

      // append the data source
      if (dataSource != null) {
        sb.append(prefix).append("DATA_SOURCE=[ ").append(dataSource)
            .append(" ]");
        prefix = ", ";
      }

      // append the record ID
      if (recordId != null) {
        sb.append(prefix).append("RECORD_ID=[ ").append(recordId)
            .append(" ]");
        prefix = ", ";
      }
    } catch(Exception ignore) {
      ignore.printStackTrace();
    }

    System.err.println(sb.toString());
    synchronized (LOAD_ERROR_SUPPRESSOR) {
      boolean suppressing = LOAD_ERROR_SUPPRESSOR.isSuppressing();
      ErrorLogSuppressor.Result result = LOAD_ERROR_SUPPRESSOR.updateOnError();
      switch (result.getState()) {
        case SUPPRESSED:
          if (!suppressing) {
            System.err.println(
                timestamp + ": SUPPRESSING ASYNC INFO MESSAGE ERRORS FOR "
                    + LOAD_ERROR_SUPPRESSOR.getSuppressDuration() + "ms");
          }
          break;
        case REACTIVATED:
          if (result.getSuppressedCount() > 0) {
            System.err.println(
                timestamp + ": RESUMING ASYNC INFO MESSAGE ERRORS ("
                    + result.getSuppressedCount() + " SUPPRESSED)");
          }
        case ACTIVE:
          e.printStackTrace();
          break;
      }
    }
  }

}
