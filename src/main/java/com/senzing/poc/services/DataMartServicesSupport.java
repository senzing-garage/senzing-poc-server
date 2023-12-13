package com.senzing.poc.services;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.UriInfo;

import com.senzing.util.Timers;
import com.senzing.api.services.ServicesSupport;
import com.senzing.api.model.SzHttpMethod;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.poc.model.SzBoundType;
import com.senzing.poc.model.SzEntitiesPage;
import com.senzing.datamart.SzReplicationProvider;
import com.senzing.sql.ConnectionProvider;

import static com.senzing.poc.model.SzBoundType.*;
import static com.senzing.sql.SQLUtilities.*;

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

    /**
     * Retrieves a page of entity ID's for a specific report key with the
     * specified bound applied.
     * 
     * @param reportKey The report key identifying the report with which the 
     *                  entity ID's are associated.
     * @param entityIdBound The bounded value for the returned entity ID's.
     * @param boundType The {@link SzBoundType} describing how the entity ID
     *                  bound value is applied in retrieving the page.
     * @param pageSize The maximum number of entity ID's to return.
     * 
     * @throws BadRequestException If the specified page size is less than one (1).
     * @throws ServiceUnavailableException If the {@link SzReplicationProvider} is not yet
     *                                     ready to use after waiting {@link 
     *                                     #REPLICATION_READY_WAIT_TIME} milliseconds.
     * @throws InternalServerErrorException If the {@link SzReplicationProvider} indicates that
     *                                      it will never be ready to use.
     */
    default SzEntitiesPage retrieveEntitiesPage(SzHttpMethod    httpMethod,
                                                UriInfo         uriInfo,
                                                Timers          timers,
                                                SzPocProvider   provider,
                                                String          reportKey, 
                                                long            entityIdBound,
                                                SzBoundType     boundType, 
                                                int             pageSize) 
        throws BadRequestException,
               ServiceUnavailableException, 
               InternalServerErrorException                                              
    {
        // check the request parameters
        if (pageSize < 1) {
            throw this.newBadRequestException(httpMethod, uriInfo, timers, 
                "The specified page size must be a positive integer: " + pageSize);
        }

        // check if the bound type is null (this should not be the case)
        if (boundType == null) boundType = EXCLUSIVE_LOWER;

        // prepare the result object the rest of the page
        SzEntitiesPage page = SzEntitiesPage.FACTORY.create();
        page.setBound(entityIdBound);
        page.setBoundType(boundType);
        page.setPageSize(pageSize);

        // setup the JDBC variables
        Connection          conn    = null;
        PreparedStatement   ps      = null;
        ResultSet           rs      = null;
        try {
            // get the connection to the database
            conn = this.getConnection(httpMethod, uriInfo, timers, provider);

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT entity_id FROM sz_dm_report_detail "
                      + "WHERE report_key = ? AND related_id = 0 AND entity_id ");

            // handle the operator and order-by for the bound type
            switch (boundType) {
                case INCLUSIVE_LOWER:
                    sb.append(" >= ? ORDER BY entity_id ASC ");
                    break;
                case EXCLUSIVE_LOWER:
                    sb.append(" > ? ORDER BY entity_id ASC ");
                    break;
                case INCLUSIVE_UPPER:
                    sb.append(" <= ? ORDER BY entity_id DESC ");
                    break;
                case EXCLUSIVE_UPPER:
                    sb.append(" < ? ORDER BY entity_id DESC ");
                    break;
                default:
                    throw new IllegalStateException("Unhandled bound type: " + boundType);
            }

            // handle the page size
            sb.append("LIMIT ?");

            // prepare the statement
            ps = conn.prepareStatement(sb.toString());

            // bind the parameters
            ps.setString(1, reportKey);
            ps.setLong(2, entityIdBound);
            ps.setInt(3, pageSize);

            // execute the query
            rs = ps.executeQuery();

            // read the results
            while (rs.next()) {
                page.addEntityId(rs.getLong(1));
            }

            // release resources
            rs = close(rs);
            ps = close(ps);

            // now get the total entity ID count
            long totalCount = 0L;
            ps = conn.prepareStatement("SELECT COUNT(*) FROM sz_dm_report_detail WHERE report_key = ?");
            ps.setString(1, reportKey);
            rs = ps.executeQuery();
            rs.next();
            totalCount = rs.getLong(1);
            rs = close(rs);
            ps = close(ps);

            // now get the "before" and "after" entity ID count
            long beforeCount    = 0L;
            long afterCount     = 0L;
            List<Long> entityIds = page.getEntityIds();
            if (entityIds.size() > 0) {
                ps = conn.prepareStatement
                    ("SELECT COUNT(*) FROM sz_dm_report_detail "
                    + "WHERE report_key = ? AND entity_id < ?");
                
                ps.setString(1, reportKey);
                ps.setLong(2, entityIds.get(0));

                rs = ps.executeQuery();
                rs.next();
                
                beforeCount = rs.getLong(1);

                rs = close(rs);
                ps = close(ps);
            }
            // calculate the "after" count from total, page and before count
            afterCount = totalCount - entityIds.size() - beforeCount;

            // set the fields on the page
            page.setTotalEntityCount(totalCount);
            page.setBeforeEntityCount(beforeCount);
            page.setAfterEntityCount(afterCount);

            // return the page
            return page;

        } catch (WebApplicationException e) {
            throw e;

        } catch (Exception e) {
            throw this.newInternalServerErrorException(
                httpMethod, uriInfo, timers, e);

        } finally {
            rs = close(rs);
            ps = close(ps);
            conn = close(conn);
        }
    }
}
