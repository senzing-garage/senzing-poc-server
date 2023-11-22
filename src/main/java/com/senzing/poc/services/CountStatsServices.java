package com.senzing.poc.services;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.senzing.poc.model.SzCountStats;
import com.senzing.poc.model.SzSourceCountStats;
import com.senzing.poc.model.SzCountStatsResponse;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.api.services.ServicesSupport;
import com.senzing.util.AccessToken;
import com.senzing.util.Timers;
import com.senzing.api.model.SzHttpMethod;

import static com.senzing.sql.SQLUtilities.*;
import static com.senzing.api.model.SzHttpMethod.POST;
import static com.senzing.util.LoggingUtilities.logOnceAndThrow;
import static javax.ws.rs.core.MediaType.*;
import static com.senzing.api.model.SzHttpMethod.GET;

/**
 * Bulk Data REST services.
 */
@Path("/statistics/counts")
@Produces(APPLICATION_JSON)
public class CountStatsServices implements DataMartServicesSupport {
  /**
   * Gets all the count stats including record counts, entity counts and 
   * a breakdown by data source.
   *
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/")
  public SzCountStatsResponse getCountStatistics(@Context UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzCountStats stats = this.getStatistics(true, // include entity counts
                                                true, // include record counts
                                                GET, 
                                                uriInfo, 
                                                timers, 
                                                provider);

        return SzCountStatsResponse.FACTORY.create(this.newMeta(GET, 200, timers),
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
   * Gets all the entity count stats including with a break down
   * by data source.
   *
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/entities")
  public SzCountStatsResponse getEntityCountStatistics(@Context UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzCountStats stats = this.getStatistics(true,   // include entity counts
                                                false,  // exclude record counts
                                                GET, 
                                                uriInfo, 
                                                timers, 
                                                provider);

        return SzCountStatsResponse.FACTORY.create(this.newMeta(GET, 200, timers),
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
   * Gets all the record count stats including with a break down
   * by data source.
   *
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/records")
  public SzCountStatsResponse getRecordCountStatistics(@Context UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzCountStats stats = this.getStatistics(false,  // exclude entity counts
                                                true,   // include record counts
                                                GET, 
                                                uriInfo, 
                                                timers, 
                                                provider);

        return SzCountStatsResponse.FACTORY.create(this.newMeta(GET, 200, timers),
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
   * @param includeEntityCounts <code>true</code> if entity counts should be
   *                            included, otherwise <code>false</code>.
   * @param includeRecordCounts <code>true</code> if record counts should be
   *                            included, otherwise <code>false</code>.
   * @param httpMethod The {@link SzHttpMethod} of the request.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} associated with the request.
   * @param provider The {@link SzPocProvider} for the request context.
   * 
   * @return The {@link SzCountStats} describing the statistics.
   */
  protected SzCountStats getStatistics(boolean          includeEntityCounts,
                                       boolean          includeRecordCounts,
                                       SzHttpMethod     httpMethod,
                                       UriInfo          uriInfo,
                                       Timers           timers,
                                       SzPocProvider    provider)
      throws ServiceUnavailableException, InternalServerErrorException
  {
    // get the connection
    Connection    conn    = null;
    Statement     stmt    = null;
    ResultSet     rs      = null;
    SzCountStats  result  = SzCountStats.FACTORY.create();
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);
      stmt = conn.createStatement();
      
      // check if getting the entity counts
      if (includeEntityCounts) {
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
      }

      long totalRecordCount = 0L;

      this.queryingDatabase(timers, "selectCountsBySource");
      try {
        // now get the counts by data source
        rs = stmt.executeQuery(
          "SELECT data_source1, entity_count, record_count "
          + "FROM sz_dm_report WHERE report='DSS' AND statistic='ENTITY_COUNT'");

        while (rs.next()) {
          String  dataSource  = rs.getString(1);
          long    entityCount = rs.getLong(2);
          long    recordCount = rs.getLong(3);

          // increment the total record count
          totalRecordCount += recordCount;

          SzSourceCountStats stats = SzSourceCountStats.FACTORY.create(dataSource);
          if (includeEntityCounts) stats.setEntityCount(entityCount);
          if (includeRecordCounts) stats.setRecordCount(recordCount);

          result.addDataSourceCount(stats);
        }

      } finally {
        this.queriedDatabase(timers, "selectCountsBySource");
      }

      // release resources
      rs = close(rs);

      // optionally set the total record count
      if (includeRecordCounts) {
        result.setTotalRecordCount(totalRecordCount);
      }
      
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
}
