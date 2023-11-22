package com.senzing.poc.services;

import java.sql.Connection;
import java.sql.SQLException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.UriInfo;

import com.senzing.util.Timers;
import com.senzing.api.services.ServicesSupport;
import com.senzing.api.model.SzHttpMethod;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.datamart.SzReplicationProvider;
import com.senzing.sql.ConnectionProvider;

/**
 * Extends {@link ServicesSupport} to add additional functionality 
 * specific to the data mart replicator services.
 */
public interface DataMartServicesSupport extends ServicesSupport {
    /**
     * The number of milliseconds to wait for the data mart replicator
     * to become ready before throwing a {@link ServiceUnavailableException}.
     */
    long REPLICATOR_READY_WAIT_TIME = 10000L;

    /**
     * The standardized {@link Timers} key used for SQL queries. 
     */
    String DATABASE_QUERY_TIMING = "sqlQuery";

    /**
     * Transitions the specified {@link Timers} into the {@link
     * #DATABASE_QUERY_TIMING} stage.
     *
     * @param timers The {@link Timers} instance to transition.
     * @param queryDescription A description of the query being executed.
     */
    default void queryingDatabase(Timers timers, String queryDescription) {
        if (timers == null) return;
        timers.start(DATABASE_QUERY_TIMING,
                     DATABASE_QUERY_TIMING + ":" + queryDescription);
    }

    /**
     * Concludes the {@link #DATABASE_QUERY_TIMING} stage for the specified
     * {@link Timers}.
     *
     * @param timers   The {@link Timers} instance to transition.
     * @param queryDescription A description of the query being executed.
     */
    default void queriedDatabase(Timers timers, String queryDescription) {
        if (timers == null) return;
        timers.pause(DATABASE_QUERY_TIMING,
                     DATABASE_QUERY_TIMING + ":" + queryDescription);
    }

    /**
     * Gets the {@link Connection} from the underlying {@link SzReplicationProvider}
     * for use in accessing the data mart.  The caller must call {@link #close()} on
     * this {@link Connection} when done using it in order to make it available for
     * other use by other operations.
     * 
     * @param httpMethod The {@link SzHttpMethod} of the request.
     * @param uriInfo The {@link UriInfo} for the request.
     * @param timers The {@link Timers} for the request.
     * @param provider The {@link SzPocProvider} associated with the request.
     * 
     * @return The {@link SzReplicationProvider} that was obtained.
     * 
     * @throws SQLException If there is a failure in obtaining the {@link Connection}
     *                      from the underlying {@link SzReplicationProvider}.
     * 
     * @throws ServiceUnavailableException If the {@link SzReplicationProvider} is not yet
     *                                     ready to use after waiting {@link 
     *                                     #REPLICATION_READY_WAIT_TIME} milliseconds.
     * @throws InternalServerErrorException If the {@link SzReplicationProvider} indicates that
     *                                      it will never be ready to use.
     */
    default Connection getConnection(SzHttpMethod   httpMethod,
                                     UriInfo        uriInfo,
                                     Timers         timers,
                                     SzPocProvider  provider) 
        throws SQLException, ServiceUnavailableException, InternalServerErrorException
    {
        SzReplicationProvider repProvider 
            = this.getReplicationProvider(httpMethod, uriInfo, timers, provider);

        ConnectionProvider connProvider = repProvider.getConnectionProvider();

        return connProvider.getConnection();
    }

    /**
     * Gets the {@link SzReplicationProvider} from the specified {@link SzPocProvider}
     * and ensures it is ready to use before returning it.  This will wait at most
     * {@link #REPLICATOR_READY_WAIT_TIME} milliseconds for the replicator to become
     * ready to use.
     * 
     * @param httpMethod The {@link SzHttpMethod} of the request.
     * @param uriInfo The {@link UriInfo} for the request.
     * @param timers The {@link Timers} for the request.
     * @param provider The {@link SzPocProvider} associated with the request.
     * 
     * @return The {@link SzReplicationProvider} that was obtained.
     * 
     * @throws ServiceUnavailableException If the {@link SzReplicationProvider} is not yet
     *                                     ready to use after waiting {@link 
     *                                     #REPLICATION_READY_WAIT_TIME} milliseconds.
     * @throws InternalServerErrorException If the {@link SzReplicationProvider} indicates that
     *                                      it will never be ready to use.
     */
    default SzReplicationProvider getReplicationProvider(SzHttpMethod   httpMethod,
                                                         UriInfo        uriInfo,
                                                         Timers         timers,
                                                         SzPocProvider  provider) 
        throws ServiceUnavailableException, InternalServerErrorException
    {
        // get the replication provider
        SzReplicationProvider replicationProvider = provider.getReplicationProvider();

        Boolean ready = null;
        try {
            // ensure it is ready
            ready = replicationProvider.waitUntilReady(REPLICATOR_READY_WAIT_TIME);
        
        } catch (InterruptedException e) {
            throw this.newInternalServerErrorException(
                httpMethod, uriInfo, timers, e);
        }

        // check if it will never be ready
        if (ready == null) {
            throw this.newInternalServerErrorException(
                httpMethod, uriInfo, timers, "Data Mart Replicator cannot service requests.");
        }

        // check if it just is not yet ready
        if (Boolean.FALSE.equals(ready)) {
            throw this.newServiceUnavailableErrorException(
                httpMethod, uriInfo, timers, "Data Mart Replicator is not ready");
        }

        // return the provider
        return replicationProvider;
    }
}
