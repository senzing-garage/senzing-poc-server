package com.senzing.poc.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
import com.senzing.poc.model.SzRelationsPage;
import com.senzing.poc.model.SzRelation;
import com.senzing.poc.model.SzMatchType;
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
     * The default page size if no sample size is specified.
     */
    final int DEFAULT_PAGE_SIZE = 100;

    /**
     * The multiplier to calculate the default page size from the sample size when 
     * the sample size is specified, but the page size is not specified.
     */
    final int SAMPLE_SIZE_MULTIPLIER = 20;

    /**
     * The number of milliseconds to wait for the data mart replicator
     * to become ready before throwing a {@link ServiceUnavailableException}.
     */
    final long REPLICATOR_READY_WAIT_TIME = 10000L;

    /**
     * The standardized {@link Timers} key used for SQL queries. 
     */
    final String DATABASE_QUERY_TIMING = "sqlQuery";

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
     * @param httpMethod The {@link SzHttpMethod} being invoked.
     * @param uriInfo The {@link UriInfo} for the REST invocation.
     * @param timers The {@link Timers} used for instrumeting the request.
     * @param provider The {@link SzPocProvider} for the request context.
     * @param reportKey The report key identifying the report with which the 
     *                  entity ID's are associated.
     * @param entityIdBound The bounded value for the returned entity ID's.
     * @param boundType The {@link SzBoundType} describing how the entity ID
     *                  bound value is applied in retrieving the page.
     * @param pageSize The optional maximum number of entity ID's to return.
     * @param sampleSize The optional number of results to randomly sample from the
     *                   page, which, if specified, must be strictly less-than the
     *                   page size.
     * 
     * @return The {@link SzEntitiesPage} describing the entities on the page.
     * 
     * @throws BadRequestException If the specified page size or sample size is less
     *                             than one (1), or if the sample size is specified
     *                             and is greater-than or equal to the sample size.
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
                                                Integer         pageSize,
                                                Integer         sampleSize)
        throws BadRequestException,
               ServiceUnavailableException, 
               InternalServerErrorException                                              
    {
        // check the request parameters
        if (pageSize != null && pageSize < 1) {
            throw this.newBadRequestException(httpMethod, uriInfo, timers, 
                "If specified, the page size must be a positive integer: " + pageSize);
        }
        if (sampleSize != null && sampleSize < 1) {
            throw this.newBadRequestException(httpMethod, uriInfo, timers, 
                "If specified, the sample size must be a positive integer: " + sampleSize);
        }
        if (pageSize != null && sampleSize != null && sampleSize >= pageSize) {
            throw this.newBadRequestException(httpMethod, uriInfo, timers, 
                "If both the page size and sample size are specified then the sample size ("
                + sampleSize + ") must be stritly less-than the page size (" + pageSize + ")");
        }

        // default the page size if not specified
        if (pageSize == null) {
            pageSize = (sampleSize == null) 
                ? DEFAULT_PAGE_SIZE : SAMPLE_SIZE_MULTIPLIER * sampleSize;
        }

        // create
        List<Long> pageResults = new ArrayList<>(pageSize);

        // check if the bound type is null (this should not be the case)
        if (boundType == null) boundType = EXCLUSIVE_LOWER;

        // prepare the result object the rest of the page
        SzEntitiesPage page = SzEntitiesPage.FACTORY.create();
        page.setBound(entityIdBound);
        page.setBoundType(boundType);
        page.setPageSize(pageSize);
        page.setSampleSize(sampleSize);

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
                pageResults.add(rs.getLong(1));
            }

            // release resources
            rs = close(rs);
            ps = close(ps);
            
            // get the actual count of results on the page
            int resultCount = pageResults.size();

            // track the minimum and maximum page entity ID encountered
            long minEntityId = -1L;
            long maxEntityId = -1L;
            for (int index = 0; index < resultCount; index++) {
                long entityId = pageResults.get(index);
                if (minEntityId < 0L || entityId < minEntityId) {
                    minEntityId = entityId;
                }
                if (maxEntityId < 0L || entityId > maxEntityId) {
                    maxEntityId = entityId;
                }
            }

            // now filter the results if we have a sample size
            if (sampleSize != null && pageResults.size() > sampleSize) {
                // set the page minumum and maximum value
                page.setPageMinimumValue(minEntityId);
                page.setPageMaximumValue(maxEntityId);

                // create a list of indices
                List<Integer> indices = new ArrayList<>();
                for (int index = 0; index < resultCount; index++) {
                    indices.add(index);
                }

                // shuffle the list of indices
                Collections.shuffle(indices);

                // determine how many results to filter out
                int filterCount = resultCount - sampleSize;

                // now eliminiate the filtered results using first N indices
                for (int index = 0; index < filterCount; index++) {
                    int resultIndex = indices.get(index);
                    pageResults.set(resultIndex, null);
                }
            }

            // add the non-filtered results to the page
            pageResults.forEach(entityId -> {
                // check if the result was filtered out from random sampling
                if (entityId != null) {
                    // the result was not filtered so add it to the page
                    page.addEntityId(entityId);
                }
            });

            // now get the total entity ID count
            long totalCount = 0L;
            ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM sz_dm_report_detail "
                    + "WHERE report_key = ? AND related_id = 0");
            ps.setString(1, reportKey);
            rs = ps.executeQuery();
            rs.next();
            totalCount = rs.getLong(1);
            rs = close(rs);
            ps = close(ps);

            // now get the "before" and "after" entity ID count
            long beforeCount    = 0L;
            long afterCount     = 0L;
            if (resultCount > 0) {
                ps = conn.prepareStatement("SELECT COUNT(*) FROM sz_dm_report_detail "
                    + "WHERE report_key = ? AND entity_id < ? AND related_id = 0");
                
                ps.setString(1, reportKey);
                ps.setLong(2, minEntityId);
                
                rs = ps.executeQuery();
                rs.next();
                
                beforeCount = rs.getLong(1);

                rs = close(rs);
                ps = close(ps);
            }
            // calculate the "after" count from total, page and before count
            afterCount = totalCount - resultCount - beforeCount;

            // set the fields on the page
            page.setTotalEntityCount(totalCount);
            page.setBeforePageCount(beforeCount);
            page.setAfterPageCount(afterCount);

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


    /**
     * Retrieves a page of relations for a specific report key with the
     * specified bound applied.
     * 
     * @param httpMethod The {@link SzHttpMethod} being invoked.
     * @param uriInfo The {@link UriInfo} for the REST invocation.
     * @param timers The {@link Timers} used for instrumeting the request.
     * @param provider The {@link SzPocProvider} for the request context.
     * @param reportKey The report key identifying the report with which the 
     *                  entity ID's are associated.
     * @param relationBound The bounded value for the returned relations.
     * @param boundType The {@link SzBoundType} describing how the entity ID
     *                  bound value is applied in retrieving the page.
     * @param pageSize The optional maximum number of entity ID's to return.
     * @param sampleSize The optional number of results to randomly sample from the
     *                   page, which, if specified, must be strictly less-than the
     *                   page size.
     * @return The {@link SzRelationsPage} describing the relations on the page.
     * 
     * @throws BadRequestException If the specified page size or sample size is less
     *                             than one (1), or if the sample size is specified
     *                             and is greater-than or equal to the sample size.
     * @throws ServiceUnavailableException If the {@link SzReplicationProvider} is not yet
     *                                     ready to use after waiting {@link 
     *                                     #REPLICATION_READY_WAIT_TIME} milliseconds.
     * @throws InternalServerErrorException If the {@link SzReplicationProvider} indicates that
     *                                      it will never be ready to use.
     */
    default SzRelationsPage retrieveRelationsPage(SzHttpMethod  httpMethod,
                                                  UriInfo       uriInfo,
                                                  Timers        timers,
                                                  SzPocProvider provider,
                                                  String        reportKey, 
                                                  String        relationBound,
                                                  SzBoundType   boundType, 
                                                  Integer       pageSize,
                                                  Integer       sampleSize)
        throws BadRequestException,
               ServiceUnavailableException, 
               InternalServerErrorException                                              
    {
        // check the request parameters
        if (pageSize != null && pageSize < 1) {
            throw this.newBadRequestException(httpMethod, uriInfo, timers, 
                "If specified, the page size must be a positive integer: " + pageSize);
        }
        if (sampleSize != null && sampleSize < 1) {
            throw this.newBadRequestException(httpMethod, uriInfo, timers, 
                "If specified, the sample size must be a positive integer: " + sampleSize);
        }
        if (pageSize != null && sampleSize != null && sampleSize >= pageSize) {
            throw this.newBadRequestException(httpMethod, uriInfo, timers, 
                "If both the page size and sample size are specified then the sample size ("
                + sampleSize + ") must be stritly less-than the page size (" + pageSize + ")");
        }

        // parse the relation bound
        long entityIdBound  = 0L;
        long relatedIdBound = 0L;
        if (relationBound != null) {
            relationBound = relationBound.trim();
            if (relationBound.length() > 0) {
                try {
                    int index = relationBound.indexOf(":");
                    if (index < 0) {
                        entityIdBound = Long.parseLong(relationBound);
                    } else if (index == 0) {
                        throw new IllegalArgumentException();                        
                    } else if (index > 0) {
                        entityIdBound = Long.parseLong(
                            relationBound.substring(0, index).trim());
                        String text = (index == relationBound.length() - 1)
                            ? "" : relationBound.substring(index + 1).trim();

                        relatedIdBound = (text.length() == 0) 
                            ? 0L : Long.parseLong(text);
                    }
                } catch (IllegalArgumentException e) {
                    throw this.newBadRequestException(httpMethod, uriInfo, timers,
                        "The specified relation bound is not properly formatted: " 
                        + relationBound);
                }          
            }
        }

        // default the page size if not specified
        if (pageSize == null) {
            pageSize = (sampleSize == null) 
                ? DEFAULT_PAGE_SIZE : SAMPLE_SIZE_MULTIPLIER * sampleSize;
        }

        // create
        List<SzRelation> pageResults = new ArrayList<>(pageSize);

        // check if the bound type is null (this should not be the case)
        if (boundType == null) boundType = EXCLUSIVE_LOWER;

        // prepare the result object the rest of the page
        SzRelationsPage page = SzRelationsPage.FACTORY.create();
        page.setBound(relationBound);
        page.setBoundType(boundType);
        page.setPageSize(pageSize);
        page.setSampleSize(sampleSize);

        // setup the JDBC variables
        Connection          conn    = null;
        PreparedStatement   ps      = null;
        ResultSet           rs      = null;
        try {
            // get the connection to the database
            conn = this.getConnection(httpMethod, uriInfo, timers, provider);

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT t1.entity_id, t1.related_id,"
                      + " t2.match_type, t2.match_key, t2.errule_code "
                      + "FROM sz_dm_report_detail AS t1 "
                      + "INNER JOIN sz_dm_relation AS t2 "
                      + "ON t2.entity_id = LEAST(t1.entity_id, t1.related_id) "
                      + "AND t2.related_id = GREATEST(t1.entity_id, t2.related_id) "
                      + "WHERE t1.report_key = ? AND t1.related_id <> 0 AND ");

            // handle the operator and order-by for the bound type
            switch (boundType) {
                case INCLUSIVE_LOWER:
                    sb.append("((t1.entity_id = ? AND t1.related_id >= ?)");
                    sb.append(" OR (t1.entity_id > ?)) ");
                    sb.append("ORDER BY t1.entity_id, t1.related_id ASC ");
                    break;
                case EXCLUSIVE_LOWER:
                    sb.append("((t1.entity_id = ? AND t1.related_id > ?)");
                    sb.append(" OR (t1.entity_id > ?)) ");
                    sb.append("ORDER BY t1.entity_id, t1.related_id ASC ");
                    break;
                case INCLUSIVE_UPPER:
                    sb.append("((t1.entity_id = ? AND t1.related_id <= ?)");
                    sb.append(" OR (t1.entity_id < ?)) ");
                    sb.append("ORDER BY t1.entity_id, t1.related_id DESC ");
                    break;
                case EXCLUSIVE_UPPER:
                    sb.append("((t1.entity_id = ? AND t1.related_id < ?)");
                    sb.append(" OR (t1.entity_id < ?)) ");
                    sb.append("ORDER BY t1.entity_id, t1.related_id DESC ");
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
            ps.setLong(3, relatedIdBound);
            ps.setLong(4, entityIdBound);
            ps.setInt(5, pageSize);

            // execute the query
            rs = ps.executeQuery();

            // read the results
            while (rs.next()) {
                long        entityId    = rs.getLong(1);
                long        relatedId   = rs.getLong(2);
                SzMatchType matchType   = SzMatchType.valueOf(rs.getString(3));
                String      matchKey    = rs.getString(4);
                String      principle   = rs.getString(5);

                SzRelation relation = SzRelation.FACTORY.create(
                    entityId, relatedId, matchType, matchKey, principle);

                pageResults.add(relation);
            }

            // release resources
            rs = close(rs);
            ps = close(ps);
            
            // get the actual count of results on the page
            int resultCount = pageResults.size();

            // track the min/max entity ID and related ID encountered
            long minEntityId    = -1L;
            long minRelatedId   = -1L;
            long maxEntityId    = -1L;
            long maxRelatedId   = -1L;
            for (int index = 0; index < resultCount; index++) {
                SzRelation relation = pageResults.get(index);
                long entityId   = relation.getEntityId();
                long relatedId  = relation.getRelatedId();

                if (minEntityId < 0L || entityId < minEntityId) {
                    minEntityId     = entityId;
                    minRelatedId    = relatedId;

                } else if (entityId == minEntityId && relatedId < minRelatedId) {
                    minRelatedId = relatedId;
                }

                if (maxEntityId < 0L || entityId > maxEntityId) {
                    maxEntityId     = entityId;
                    maxRelatedId    = relatedId;
                } else if (entityId == maxEntityId && relatedId > maxRelatedId) {
                    maxRelatedId = relatedId;
                }
            }

            // now filter the results if we have a sample size
            if (sampleSize != null && pageResults.size() > sampleSize) {
                // set the page minimum and maximum values
                page.setPageMinimumValue(minEntityId + ":" + minRelatedId);
                page.setPageMaximumValue(maxEntityId + ":" + maxRelatedId);
                
                // create a list of indices
                List<Integer> indices = new ArrayList<>();
                for (int index = 0; index < resultCount; index++) {
                    indices.add(index);
                }

                // shuffle the list of indices
                Collections.shuffle(indices);

                // determine how many results to filter out
                int filterCount = resultCount - sampleSize;

                // now eliminiate the filtered results using first N indices
                for (int index = 0; index < filterCount; index++) {
                    int resultIndex = indices.get(index);
                    pageResults.set(resultIndex, null);
                }
            }

            // add the non-filtered results to the page
            pageResults.forEach(relation -> {
                // check if the result was filtered out from random sampling
                if (relation != null) {
                    // the result was not filtered so add it to the page
                    page.addRelation(relation);
                }
            });

            // now get the total relation count
            long totalCount = 0L;
            ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM sz_dm_report_detail "
                    + "WHERE report_key = ? AND related_id <> 0");
            ps.setString(1, reportKey);
            rs = ps.executeQuery();
            rs.next();
            totalCount = rs.getLong(1);
            rs = close(rs);
            ps = close(ps);

            // now get the "before" and "after" relation count
            long beforeCount    = 0L;
            long afterCount     = 0L;
            if (resultCount > 0) {
                ps = conn.prepareStatement
                    ("SELECT COUNT(*) FROM sz_dm_report_detail "
                    + "WHERE report_key = ? AND related_id <> 0 AND "
                    + "((entity_id = ? AND related_id < ?) OR (entity_id < ?))");
                
                ps.setString(1, reportKey);
                ps.setLong(2, minEntityId);
                ps.setLong(3, minRelatedId);
                ps.setLong(4, minEntityId);
                
                rs = ps.executeQuery();
                rs.next();
                
                beforeCount = rs.getLong(1);

                rs = close(rs);
                ps = close(ps);
            }
            // calculate the "after" count from total, page and before count
            afterCount = totalCount - resultCount - beforeCount;

            // set the fields on the page
            page.setTotalRelationCount(totalCount);
            page.setBeforePageCount(beforeCount);
            page.setAfterPageCount(afterCount);

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
