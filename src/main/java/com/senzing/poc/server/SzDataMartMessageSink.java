package com.senzing.poc.server;

import java.sql.SQLException;

import com.senzing.api.services.SzMessageSink;
import com.senzing.api.services.SzMessage;

import static com.senzing.listener.communication.sql.SQLConsumer.MessageQueue;
import static com.senzing.util.LoggingUtilities.*;

/**
 * Provides an {@link SzMessageSink} that will always enqueue INFO messages
 * on the embedded data mart's database message queue <b>and</b> may optionally
 * also forward those messages to a message queue configured in the startup
 * parameters of the {@link SzPocServer}.
 */
public class SzDataMartMessageSink implements SzMessageSink {
    /**
     * The message sink provider type for the data mart.
     */  
    public static final String DATA_MART_PROVIDER_TYPE = "Data Mart Database Queue";

    /**
     * The backing message sink.
     */
    private SzMessageSink backingSink = null;

    /**
     * The backing {@link MessageQueue}.
     */
    private MessageQueue messageQueue = null;

    /**
     * Constructs with the specified message queue
     */
    public SzDataMartMessageSink(MessageQueue   messageQueue,
                                 SzMessageSink  messageSink)
    {
        this.backingSink    = messageSink;
        this.messageQueue   = messageQueue;
    }

    /**
     * Package-protected method for obtaining the backing sink to use for releasing
     * the acquired sink.  This returns <code>null</code> if none.
     * 
     * @return The backing sink to use for releasing the acquired sink, or
     *         <code>null</code> if none.
     */
    SzMessageSink getBackingSink() {
        return this.backingSink;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to also enqueue the message to the backing message queue in 
     * addition to any backing {@link SzMessageSink} (if one exists).
     */
    @Override
    public void send(SzMessage message, FailureHandler onFailure)
        throws Exception
    {
        Exception failure = null;
        try {
            this.messageQueue.enqueueMessage(message.getBody());
        } catch (SQLException e) {
            onFailure.handle(e, message);
            failure = e;
        }
        if (this.backingSink != null) {
            try {
                this.backingSink.send(message, onFailure);
            } catch (Exception e) {
                failure = e;
            }
        }
        if (failure != null) throw failure;
    }

   /**
    * {@inheritDoc}
    * <p>
    * Overridden to return the provider type from the backing message sink if one
    * exists, otherwise this returns {@link #DATA_MART_PROVIDER_TYPE}.
    */
    @Override
    public String getProviderType() {
        if (this.backingSink == null) {
            return "Data Mart Database Queue";
        } else {
            return this.backingSink.getProviderType();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to return the message count from the backing message sink if one
     * exists, otherwise this returns the result from {@link 
     * MessageQueue#getMessageCount()}.
     */
    @Override
    public Integer getMessageCount() {
        if (this.backingSink == null) {
            try {
                return this.messageQueue.getMessageCount();
            } catch (SQLException e) {
                logWarning(e, "Failed to get message count from SQL message queue");
                return null;
            }

        } else {
            return this.backingSink.getMessageCount();
        }
    }

    /**
     * Checks if this instance has a backing sync whereby it will also 
     * send the INFO messages to a message queue in addition to the database
     * messge queue.
     * 
     * @return <code>true</code> if this instance has a backing sync and 
     *         <code>false</code> if it does not.
     */
    public boolean hasBackingSync() {
        return (this.backingSink != null);
    }
}
