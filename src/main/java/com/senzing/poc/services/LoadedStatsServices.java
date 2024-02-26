package com.senzing.poc.services;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.senzing.poc.model.SzBoundType;
import com.senzing.poc.model.SzEntitiesPage;
import com.senzing.poc.model.SzEntitiesPageResponse;
import com.senzing.poc.model.SzLoadedStats;
import com.senzing.poc.model.SzSourceLoadedStats;
import com.senzing.poc.model.SzLoadedStatsResponse;
import com.senzing.poc.model.SzSourceLoadedStatsResponse;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.Timers;
import com.senzing.api.model.SzHttpMethod;
import com.senzing.datamart.model.SzReportKey;

import static com.senzing.sql.SQLUtilities.*;
import static com.senzing.util.LoggingUtilities.*;
import static javax.ws.rs.core.MediaType.*;
import static com.senzing.api.model.SzHttpMethod.GET;
import static com.senzing.datamart.model.SzReportStatistic.*;
import static com.senzing.datamart.model.SzReportCode.*;

/**
 * Count Statistics REST services.
 */
@Path("/statistics/loaded")
@Produces(APPLICATION_JSON)
public class LoadedStatsServices implements DataMartServicesSupport {
  /**
   * Gets all the count stats including total record count, total entity
   * count and total unmatched record count along with a breakdown of 
   * record count, entity count and unmatched record count by data source.
   *
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/")
  public SzLoadedStatsResponse getLoadedStatistics(@Context UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzLoadedStats stats = this.getStatistics(GET, 
                                                uriInfo, 
                                                timers, 
                                                provider);

        return SzLoadedStatsResponse.FACTORY.create(this.newMeta(GET, 200, timers),
                                                   this.newLinks(uriInfo),
                                                   stats);
        
    } catch (ClientErrorException e) {
      throw e;

    } catch (WebApplicationException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      e.printStackTrace();
      throw this.newInternalServerErrorException(GET, uriInfo, timers, e);
    }
  }

  /**
   * Internal method for obtaining the count statistics.
   * 
   * @param httpMethod The {@link SzHttpMethod} of the request.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} associated with the request.
   * @param provider The {@link SzPocProvider} for the request context.
   * 
   * @return The {@link SzLoadedStats} describing the statistics.
   * 
   * @throws ServiceUnavailableException If the data mart is not yet ready to 
   *                                     service a request.
   * @throws InternalServerErrorException If an internal error occurs.
   */
  protected SzLoadedStats getStatistics(SzHttpMethod     httpMethod,
                                       UriInfo          uriInfo,
                                       Timers           timers,
                                       SzPocProvider    provider)
      throws ServiceUnavailableException, InternalServerErrorException
  {
    // get the connection
    Connection    conn    = null;
    Statement     stmt    = null;
    ResultSet     rs      = null;
    SzLoadedStats  result  = SzLoadedStats.FACTORY.create();
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);
      stmt = conn.createStatement();

      // get the total entity count
      this.queryingDatabase(timers, "selectEntityCount");
      try {
        // prepare the total entity count query
        rs = stmt.executeQuery(
          "SELECT SUM(entity_count) FROM sz_dm_report WHERE report='ESB'");
          
        // read the results
        rs.next();
        result.setTotalEntityCount(rs.getLong(1));

      } finally {
        this.queriedDatabase(timers, "selectEntityCount");
        // release resources
        rs = close(rs);
      }

      // create a map to keep track of the source stats
      Map<String, SzSourceLoadedStats> sourceStatsMap = new LinkedHashMap<>();

      // now get the source entity and record counts
      long totalRecordCount = 0L;

      this.queryingDatabase(timers, "selectCountsBySource");
      try {
        // now get the counts by data source
        rs = stmt.executeQuery(
          "SELECT data_source1, entity_count, record_count "
          + "FROM sz_dm_report WHERE report='DSS' AND statistic='ENTITY_COUNT' "
          + "ORDER BY data_source1");

        while (rs.next()) {
          String  dataSource  = rs.getString(1);
          long    entityCount = rs.getLong(2);
          long    recordCount = rs.getLong(3);

          // increment the total record count
          totalRecordCount += recordCount;

          SzSourceLoadedStats stats = SzSourceLoadedStats.FACTORY.create(dataSource);
          stats.setEntityCount(entityCount);
          stats.setRecordCount(recordCount);

          sourceStatsMap.put(dataSource, stats);
        }

      } finally {
        this.queriedDatabase(timers, "selectCountsBySource");
      }

      // release resources
      rs = close(rs);

      // set the total record count
      result.setTotalRecordCount(totalRecordCount);

      long totalUnmatchedRecordCount = 0L;

      this.queryingDatabase(timers, "selectUnmatchedCountsBySource");
      try {
        // now get the counts by data source
        rs = stmt.executeQuery(
          "SELECT data_source1, entity_count "
          + "FROM sz_dm_report WHERE report='DSS' AND statistic='UNMATCHED_COUNT' "
          + "ORDER BY data_source1");

        while (rs.next()) {
          String  dataSource      = rs.getString(1);
          long    unmatchedCount  = rs.getLong(2);

          // increment the total record count
          totalUnmatchedRecordCount += unmatchedCount;

          SzSourceLoadedStats stats = sourceStatsMap.remove(dataSource);
          if (stats == null) {
            stats = SzSourceLoadedStats.FACTORY.create(dataSource);
            logWarning("Missing entity and record count stats for data source, "
                       + "but got unmatched record count stats: " + dataSource);
          }

          // set the unmatched record count
          stats.setUnmatchedRecordCount(unmatchedCount);

          // add the source stats to the result
          result.addDataSourceCount(stats);          
        }

      } finally {
        this.queriedDatabase(timers, "selectCountsBySource");
      }

      // release resources
      rs = close(rs);

      // set the total unmatched record count
      result.setTotalUnmatchedRecordCount(totalUnmatchedRecordCount);

      // add the source stats to the result
      sourceStatsMap.values().forEach(stats -> {
        logWarning("Missing unmatched record count stats for data source, but "
                   + "got entity and record count stats: " + stats.getDataSource());
        result.addDataSourceCount(stats);
      });

      // return the result
      return result;

    } catch (SQLException e) {
      throw this.newInternalServerErrorException(
        httpMethod, uriInfo, timers, e);
    } finally {
      rs   = close(rs);
      stmt = close(stmt);
      conn = close(conn);
    }
  }

  /**
   * Gets all the count stats for a specific data source including record 
   * counts, entity counts and unmatched record counts for that data source.
   *
   * @param dataSourceCode The data source code identifying the data source
   *                       for which the count statistics are being requested.
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/data-sources/{dataSourceCode}")
  public SzSourceLoadedStatsResponse getSourceLoadedStatistics(
    @PathParam("dataSourceCode") String dataSourceCode,
    @Context UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzSourceLoadedStats stats = this.getSourceStatistics(dataSourceCode, 
                                                             GET, 
                                                             uriInfo, 
                                                             timers, 
                                                             provider);

        return SzSourceLoadedStatsResponse.FACTORY.create(
          this.newMeta(GET, 200, timers),
          this.newLinks(uriInfo),
          stats);
        
    } catch (ClientErrorException e) {
      throw e;

    } catch (WebApplicationException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      e.printStackTrace();
      throw this.newInternalServerErrorException(GET, uriInfo, timers, e);
    }
  }

  /**
   * Internal method for obtaining the count statistics for a specific
   * data source.
   * 
   * @param dataSource The data source code for which the statistics
   *                   are being requested.
   * @param httpMethod The {@link SzHttpMethod} of the request.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} associated with the request.
   * @param provider The {@link SzPocProvider} for the request context.
   * 
   * @return The {@link SzLoadedStats} describing the statistics.
   * 
   * @throws NotFoundException If the specified data source is not recognized.
   * @throws ServiceUnavailableException If the data mart is not yet ready to 
   *                                     service a request.
   * @throws InternalServerErrorException If an internal error occurs.
   */
  protected SzSourceLoadedStats getSourceStatistics(
    String            dataSource,
    SzHttpMethod      httpMethod,
    UriInfo           uriInfo,
    Timers            timers,
    SzPocProvider     provider)
      throws ServiceUnavailableException, NotFoundException, 
             InternalServerErrorException
  {
    // check the data source
    Set<String> dataSources = provider.getDataSources(dataSource);
    if (!dataSources.contains(dataSource)) {
      throw new NotFoundException("Unrecognized data source: " + dataSource);
    }

    // get the connection
    Connection          conn    = null;
    PreparedStatement   ps      = null;
    ResultSet           rs      = null;
    SzSourceLoadedStats  result  = SzSourceLoadedStats.FACTORY.create(dataSource);
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);
      
      this.queryingDatabase(timers, "selectCountsForSource");
      try {
        // prepare the statement
        ps = conn.prepareStatement(
          "SELECT entity_count, record_count "
          + "FROM sz_dm_report WHERE report='DSS' AND statistic='ENTITY_COUNT' "
          + "AND data_source1 = ?");

        // bind the parameters
        ps.setString(1, dataSource);

        // execute the query
        rs = ps.executeQuery();
        
        // check if we have a result
        if (rs.next()) {
          long    entityCount = rs.getLong(1);
          long    recordCount = rs.getLong(2);

          result.setEntityCount(entityCount);
          result.setRecordCount(recordCount);

        } else {
          logWarning("Failed to find entity and record count stats "
                     + "for data source: " + dataSource);
        }

      } finally {
        this.queriedDatabase(timers, "selectCountsForSource");
      }

      // release resources
      rs = close(rs);
      ps = close(ps);

      boolean foundUnmatched = false;
      this.queryingDatabase(timers, "selectUnmatchedCountForSource");
      try {
        // now get the counts by data source
        ps = conn.prepareStatement(
          "SELECT entity_count "
          + "FROM sz_dm_report WHERE report='DSS' AND statistic='UNMATCHED_COUNT' "
          + "AND data_source1 = ?");

        // bind the parameters
        ps.setString(1, dataSource);

        // execute the query
        rs = ps.executeQuery();
        if (rs.next()) {
          foundUnmatched = true;
          long unmatchedCount = rs.getLong(1);

          // set the unmatched record count
          result.setUnmatchedRecordCount(unmatchedCount);

        } else {
          logWarning("Failed to find unmatched record count stats "
                     + "for data source: " + dataSource);
        }

      } finally {
        this.queriedDatabase(timers, "selectUnmatchedCountForSource");
      }

      // release resources
      rs = close(rs);
      ps = close(ps);

      // return the result
      return result;

    } catch (SQLException e) {
      throw this.newInternalServerErrorException(
        httpMethod, uriInfo, timers, e);

    } finally {
      rs   = close(rs);
      ps   = close(ps);
      conn = close(conn);
    }
  }

  /**
   * Retrieves a page of entity ID's for entities that have records loaded
   * from the specified data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param entityIdBound The bound value for the entity ID's that will be
   *                      returned.
   * @param boundType The {@link SzBoundType} that describes how to apply the
   *                  specified entity ID bound.
   * @param pageSize The maximum number of entity ID's to return.
   * @param uriInfo The {@link UriInfo} for the request.
   * 
   * @throws NotFoundException If the specified entity size is less than one.
   */
  @GET
  @Path("/data-sources/{dataSourceCode}/entities")
  public SzEntitiesPageResponse getEntityIdsForDataSource(
    @PathParam("dataSourceCode")                                String      dataSource,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    // check the data source
    Set<String> dataSources = provider.getDataSources(dataSource);
    if (!dataSources.contains(dataSource)) {
      throw new NotFoundException("Unrecognized data source: " + dataSource);
    }

    try {
      SzReportKey reportKey = new SzReportKey(DATA_SOURCE_SUMMARY,
                                              ENTITY_COUNT, 
                                              dataSource, 
                                              dataSource);

      SzEntitiesPage page = this.retrieveEntitiesPage(GET, 
                                                      uriInfo, 
                                                      timers, 
                                                      provider, 
                                                      reportKey.toString(), 
                                                      entityIdBound, 
                                                      boundType, 
                                                      pageSize,
                                                      sampleSize);

      return SzEntitiesPageResponse.FACTORY.create(
        this.newMeta(GET, 200, timers),
        this.newLinks(uriInfo),
        page);
        
    } catch (ClientErrorException e) {
      throw e;

    } catch (WebApplicationException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      e.printStackTrace();
      throw this.newInternalServerErrorException(GET, uriInfo, timers, e);
    }
  }
}
