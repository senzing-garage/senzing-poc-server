package com.senzing.poc.services;

import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.senzing.poc.model.SzBoundType;
import com.senzing.poc.model.SzEntitiesPage;
import com.senzing.poc.model.SzEntitiesPageResponse;
import com.senzing.poc.model.SzRelationsPage;
import com.senzing.poc.model.SzRelationsPageResponse;
import com.senzing.poc.model.SzCrossSourceSummary;
import com.senzing.poc.model.SzCrossSourceSummaryResponse;
import com.senzing.poc.model.SzSourceSummary;
import com.senzing.poc.model.SzSourceSummaryResponse;
import com.senzing.poc.model.SzSummaryStats;
import com.senzing.poc.model.SzSummaryCounts;
import com.senzing.poc.model.SzSummaryStatsResponse;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.Timers;
import com.senzing.api.model.SzHttpMethod;
import com.senzing.datamart.model.SzReportCode;
import com.senzing.datamart.model.SzReportKey;
import com.senzing.datamart.model.SzReportStatistic;

import static com.senzing.sql.SQLUtilities.*;
import static com.senzing.util.LoggingUtilities.*;
import static javax.ws.rs.core.MediaType.*;
import static com.senzing.api.model.SzHttpMethod.GET;
import static com.senzing.datamart.model.SzReportStatistic.*;
import static com.senzing.datamart.model.SzReportCode.*;

/**
 * Count Statistics REST services.
 */
@Path("/statistics/summary")
@Produces(APPLICATION_JSON)
public class SummaryStatsServices implements DataMartServicesSupport {
  /**
   * Gets all the source summaries for all the configured data 
   * sources.
   * 
   * @param matchKey The optional match key for retrieving statistics
   *                 specific to a match key, or asterisk 
   *                 (<code>"*"</code>) for all match keys, or 
   *                 <code>null</code> for only retrieving statistics
   *                 that are not specific to a match key.
   * @param principle The optional principle for retrieving statistics
   *                  specific to a principle, or asterisk 
   *                  (<code>"*"</code>) for all principles, or 
   *                  <code>null</code> for only retrieving statistics
   *                  that are not specific to a principle.
   * @param onlyLoaded Set to <code>true</code> to only consider data sources
   *                   that have loaded record, otherwise set this to
   *                   <code>false<code> to consider all data sources.
   * @param uriInfo The {@link UriInfo} for the request.
   * 
   * @return The {@link SzSummaryStatsResponse} describing the response.
   */
  @GET
  @Path("/")
  public SzSummaryStatsResponse getSummaryStats(
    @QueryParam("matchKey")                                 String  matchKey,
    @QueryParam("principle")                                String  principle,
    @QueryParam("onlyLoadedSources")  @DefaultValue("true") boolean onlyLoaded,
    @Context                                                UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzSummaryStats stats = this.getSummaryStats(matchKey, 
                                                    principle,
                                                    onlyLoaded,
                                                    GET,
                                                    uriInfo, 
                                                    timers, 
                                                    provider);

        return SzSummaryStatsResponse.FACTORY.create(
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
   * Gets the data sources that have loaded records.
   * 
   * @param httpMethod The {@link SzHttpMethod} of the request.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} associated with the request.
   * @param provider The {@link SzPocProvider} for the request context.
   * 
   * @return The {@link Set} of data sources that have loaded records.
   * 
   * @throws SQLException If a JDBC failure occurs.
   */
  private SortedSet<String> getLoadedDataSources(SzHttpMethod   httpMethod,
                                                 UriInfo        uriInfo,
                                                 Timers         timers,
                                                 SzPocProvider  provider)
    throws SQLException
  {
    // initialize resources
    Connection        conn  = null;
    PreparedStatement ps    = null;
    ResultSet         rs    = null;
    
    try {
        // get the connection to the data mart database
        conn = this.getConnection(httpMethod, uriInfo, timers, provider);

        // prepare the statement
        this.queryingDatabase(timers, "selectLoadedSources");
        ps = conn.prepareStatement(
          "SELECT data_source1 FROM sz_dm_report "
              + "WHERE report=? AND statistic=? AND record_count > 0 "
              + "ORDER BY data_source1");
      
        // bind the parameters
        ps.setString(1, DATA_SOURCE_SUMMARY.getCode());
        ps.setString(2, ENTITY_COUNT.toString());

        // execute the query
        rs = ps.executeQuery();

        // read the results
        SortedSet<String> dataSources = new TreeSet<>();
        while(rs.next()) {
          dataSources.add(rs.getString(1));
        }

        // return the data sources
        return dataSources;

    } finally {
        this.queriedDatabase(timers, "selectLoadedSources");
        
        // release resources
        rs = close(rs);
        ps = close(ps);
        conn = close(conn);
    }
  }

  /**
   * Internal method for obtaining all the summary statistics.
   * 
   * @param matchKey The optional match key for retrieving statistics
   *                 specific to a match key, or asterisk 
   *                 (<code>"*"</code>) for all match keys, or 
   *                 <code>null</code> for only retrieving statistics
   *                 that are not specific to a match key.
   * @param principle The optional principle for retrieving statistics
   *                  specific to a principle, or asterisk 
   *                  (<code>"*"</code>) for all principles, or 
   *                  <code>null</code> for only retrieving statistics
   *                  that are not specific to a principle.
   * @param onlyLoaded Set to <code>true</code> to only consider data sources
   *                   that have loaded record, otherwise set this to
   *                   <code>false<code> to consider all data sources.
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
  protected SzSummaryStats getSummaryStats(String         matchKey,
                                           String         principle,
                                           boolean        onlyLoaded,
                                           SzHttpMethod   httpMethod,
                                           UriInfo        uriInfo,
                                           Timers         timers,
                                           SzPocProvider  provider)
      throws ServiceUnavailableException, InternalServerErrorException
  {

    // create the result
    SzSummaryStats result = SzSummaryStats.FACTORY.create();
    try {
      Set<String> dataSources = (onlyLoaded) 
        ? getLoadedDataSources(httpMethod, uriInfo, timers, provider) 
        : provider.getDataSources();
      
      // iterate over the data sources
      dataSources.forEach(dataSource -> {
        result.addSourceSummary(this.getSourceSummary(dataSource, 
                                                      matchKey,
                                                      principle,
                                                      onlyLoaded,
                                                      httpMethod,
                                                      uriInfo,
                                                      timers,
                                                      provider));
      });

      // return the result
      return result;

    } catch (ClientErrorException e) {
      throw e;

    } catch (WebApplicationException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      throw this.newInternalServerErrorException(
        httpMethod, uriInfo, timers, e);

    }
  }

  /**
   * Gets the source summary for a specific data source.
   *
   * @param dataSourceCode The data source code identifying the data source
   *                       for which the count statistics are being requested.
   * @param matchKey The optional match key for retrieving statistics
   *                 specific to a match key, or asterisk 
   *                 (<code>"*"</code>) for all match keys, or 
   *                 <code>null</code> for only retrieving statistics
   *                 that are not specific to a match key.
   * @param principle The optional principle for retrieving statistics
   *                  specific to a principle, or asterisk 
   *                  (<code>"*"</code>) for all principles, or 
   *                  <code>null</code> for only retrieving statistics
   *                  that are not specific to a principle.
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/data-sources/{dataSourceCode}")
  public SzSourceSummaryResponse getSourceSummary(
    @PathParam("dataSourceCode")                            String  dataSourceCode,
    @QueryParam("matchKey")                                 String  matchKey,
    @QueryParam("principle")                                String  principle,
    @QueryParam("onlyLoadedSources")  @DefaultValue("true") boolean onlyLoaded,
    @Context                                                UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzSourceSummary summary = this.getSourceSummary(dataSourceCode,
                                                        matchKey,
                                                        principle,
                                                        onlyLoaded,
                                                        GET, 
                                                        uriInfo, 
                                                        timers, 
                                                        provider);

        return SzSourceSummaryResponse.FACTORY.create(
          this.newMeta(GET, 200, timers),
          this.newLinks(uriInfo),
          summary);
        
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
   * Internal method for obtaining the source summary for a specific
   * data source.
   * 
   * @param dataSource The data source code for which the statistics
   *                   are being requested.
   * @param matchKey The optional match key for retrieving statistics
   *                 specific to a match key, or asterisk 
   *                 (<code>"*"</code>) for all match keys, or 
   *                 <code>null</code> for only retrieving statistics
   *                 that are not specific to a match key.
   * @param principle The optional principle for retrieving statistics
   *                  specific to a principle, or asterisk 
   *                  (<code>"*"</code>) for all principles, or 
   *                  <code>null</code> for only retrieving statistics
   *                  that are not specific to a principle.
   * @param onlyLoaded Set to <code>true</code> to only consider data sources
   *                   that have loaded record, otherwise set this to
   *                   <code>false<code> to consider all data sources.
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
  protected SzSourceSummary getSourceSummary(
    String            dataSource,
    String            matchKey,
    String            principle,
    boolean           onlyLoaded,
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
    SzSourceSummary     result  = SzSourceSummary.FACTORY.create(dataSource);
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);
            
      this.queryingDatabase(timers, "selectSourceSummary");
      try {
        // prepare the statement
        ps = conn.prepareStatement(
          "SELECT statistic, entity_count, record_count "
          + "FROM sz_dm_report WHERE report='DSS' "
          + "AND data_source1 = ? AND data_source2 = ? "
          + "AND statistic IN (?, ?)"
          + "ORDER BY statistic");

        // bind the parameters
        ps.setString(1, dataSource);
        ps.setString(2, dataSource);
        ps.setString(3, ENTITY_COUNT.toString());
        ps.setString(4, UNMATCHED_COUNT.toString());

        // execute the query
        rs = ps.executeQuery();
        
        // check if we have a result
        while (rs.next()) {
          String  encodedStat   = rs.getString(1);
          long    entityCount   = rs.getLong(2);
          long    recordCount   = rs.getLong(3);

          SzReportStatistic.Formatter formatter 
            = SzReportStatistic.Formatter.parse(encodedStat);
          
          SzReportStatistic statistic = formatter.getStatistic();
          switch (statistic) {
            case ENTITY_COUNT:
              result.setEntityCount(entityCount);
              result.setRecordCount(recordCount);
              break;
            case UNMATCHED_COUNT:
              result.setUnmatchedRecordCount(recordCount);
              break;
            default:
              throw new IllegalStateException(
                "Unexpected statistic value: " + statistic);
          }
        }

      } finally {
        this.queriedDatabase(timers, "selectSourceSummary");
      }

      // release resources
      rs = close(rs);
      ps = close(ps);
      conn = close(conn);

      // check if we should only consider loaded data sources
      if (onlyLoaded) {
        dataSources = getLoadedDataSources(httpMethod, uriInfo, timers, provider);
      }

      // get the cross summaries
      dataSources.forEach(vsDataSource -> {
        result.addCrossSourceSummary(this.getCrossSourceSummary(
          dataSource,
          vsDataSource,
          matchKey,
          principle,
          httpMethod,
          uriInfo,
          timers,
          provider));
      });

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
   * Gets cross-source summary statistics for a specific primary data source
   * and "versus" data source.
   *
   * @param dataSourceCode The data source code identifying the primary data
   *                       source for which the cross summary statistics are
   *                       being requested.
   * @param vsDataSourceCode The data source code identifying the "versus" data
   *                         source for which the cross summary statistics are 
   *                         being requested.
   * @param matchKey The optional match key for retrieving statistics
   *                 specific to a match key, or asterisk 
   *                 (<code>"*"</code>) for all match keys, or 
   *                 <code>null</code> for only retrieving statistics
   *                 that are not specific to a match key.
   * @param principle The optional principle for retrieving statistics
   *                  specific to a principle, or asterisk 
   *                  (<code>"*"</code>) for all principles, or 
   *                  <code>null</code> for only retrieving statistics
   *                  that are not specific to a principle.
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}")
  public SzCrossSourceSummaryResponse getCrossSourceSummary(
    @PathParam("dataSourceCode")    String  dataSourceCode,
    @PathParam("vsDataSourceCode")  String  vsDataSourceCode,
    @QueryParam("matchKey")         String  matchKey,
    @QueryParam("principle")        String  principle,
    @Context                        UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzCrossSourceSummary summary = this.getCrossSourceSummary(
          dataSourceCode,
          vsDataSourceCode,
          matchKey,
          principle,
          GET, 
          uriInfo, 
          timers, 
          provider);

        return SzCrossSourceSummaryResponse.FACTORY.create(
          this.newMeta(GET, 200, timers),
          this.newLinks(uriInfo),
          summary);
        
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
   * @param dataSource The primary data source code for which the
   *                   statistics are being requested.
   * @param vsDataSource The "versus" data source code for which the 
   *                     statistics are being requested.
   * @param requestedMatchKey The optional match key for retrieving
   *                          statistics specific to a match key, or
   *                          asterisk (<code>"*"</code>) for all
   *                          match keys, or <code>null</code> for
   *                          only retrieving statistics that are not
   *                          specific to a match key.
   * @param requestedPrinciple The optional principle for retrieving 
   *                           statistics specific to a principle, or
   *                           asterisk (<code>"*"</code>) for all
   *                           principles, or <code>null</code> for
   *                           only retrieving statistics that are not
   *                           specific to a principle.
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
  protected SzCrossSourceSummary getCrossSourceSummary(
    String            dataSource,
    String            vsDataSource,
    String            requestedMatchKey,
    String            requestedPrinciple,
    SzHttpMethod      httpMethod,
    UriInfo           uriInfo,
    Timers            timers,
    SzPocProvider     provider)
      throws ServiceUnavailableException, NotFoundException, 
             InternalServerErrorException
  {
    // check the data source
    Set<String> dataSources = provider.getDataSources(dataSource, vsDataSource);
    if (!dataSources.contains(dataSource)) {
      throw new NotFoundException("Unrecognized data source: " + dataSource);
    }
    if (!dataSources.contains(vsDataSource)) {
      throw new NotFoundException("Unrecognized data source: " + vsDataSource);
    }

    // normalize the match key and principle
    if (requestedMatchKey != null) {
        requestedMatchKey = requestedMatchKey.trim();
        if (requestedMatchKey.length() == 0) requestedMatchKey = null;
    }
    if (requestedPrinciple != null) {
        requestedPrinciple = requestedPrinciple.trim();
        if (requestedPrinciple.length() == 0) requestedPrinciple = null;
    }

    // get the connection
    Connection            conn    = null;
    PreparedStatement     ps      = null;
    ResultSet             rs      = null;
    SzCrossSourceSummary  result  = SzCrossSourceSummary.FACTORY.create(dataSource, 
                                                                        vsDataSource);

    Map<String, SzSummaryCounts> summaryCountsMap = new LinkedHashMap<>();

    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);
      
      this.queryingDatabase(timers, "selectCrossSourceSummary");
      try {
        // determine the report code
        String reportCode = (dataSource.equals(vsDataSource)) ? "DSS" : "CSS";

        // prepare the statement
        ps = conn.prepareStatement(
          "SELECT statistic, entity_count, record_count, relation_count "
          + "FROM sz_dm_report WHERE report=? AND data_source1 = ? "
          + "AND data_source2 = ? ORDER BY statistic");

        // bind the parameters
        ps.setString(1, reportCode);
        ps.setString(2, dataSource);
        ps.setString(3, vsDataSource);

        // execute the query
        rs = ps.executeQuery();
        
        // check if we have a result
        while (rs.next()) {
          String  encodedStat   = rs.getString(1);
          long    entityCount   = rs.getLong(2);
          long    recordCount   = rs.getLong(3);
          long    relationCount = rs.getLong(4);

          SzReportStatistic.Formatter formatter
            = SzReportStatistic.Formatter.parse(encodedStat);

          SzReportStatistic statistic = formatter.getStatistic();
          String            principle = formatter.getPrinciple();
          String            matchKey  = formatter.getMatchKey();

          // filter on match key and principle
          if (!Objects.equals(principle, requestedPrinciple) 
              && !"*".equals(requestedPrinciple)) {
                continue;
          }
          if (!Objects.equals(matchKey, requestedMatchKey)
            && !"*".equals(requestedMatchKey)) {
              continue;
          }

          // define a key for looking up the cross summary stats
          String countsKey = (principle == null ? "" : principle) 
            + ":" + (matchKey == null ? "" : matchKey);

          SzSummaryCounts summaryCounts = summaryCountsMap.get(countsKey);
          if (summaryCounts == null) {
            summaryCounts = SzSummaryCounts.FACTORY.create(matchKey, principle);
            summaryCountsMap.put(countsKey, summaryCounts);
          }
          
          switch (statistic) {
            case MATCHED_COUNT:
              summaryCounts.getMatches().setEntityCount(entityCount);
              summaryCounts.getMatches().setRecordCount(recordCount);
              break;
            case AMBIGUOUS_MATCH_COUNT:
              summaryCounts.getAmbiguousMatches().setEntityCount(entityCount);
              summaryCounts.getAmbiguousMatches().setRecordCount(recordCount);
              summaryCounts.getAmbiguousMatches().setRelationCount(relationCount);
              break;
            case POSSIBLE_MATCH_COUNT:
              summaryCounts.getPossibleMatches().setEntityCount(entityCount);
              summaryCounts.getPossibleMatches().setRecordCount(recordCount);
              summaryCounts.getPossibleMatches().setRelationCount(relationCount);
              break;
            case POSSIBLE_RELATION_COUNT:
              summaryCounts.getPossibleRelations().setEntityCount(entityCount);
              summaryCounts.getPossibleRelations().setRecordCount(recordCount);
              summaryCounts.getPossibleRelations().setRelationCount(relationCount);
              break;
            case DISCLOSED_RELATION_COUNT:
              summaryCounts.getDisclosedRelations().setEntityCount(entityCount);
              summaryCounts.getDisclosedRelations().setRecordCount(recordCount);
              summaryCounts.getDisclosedRelations().setRelationCount(relationCount);
              break;
            case ENTITY_COUNT:
            case UNMATCHED_COUNT:
              // ignore these stats
              break;
            default:
              throw new IllegalStateException(
                "Unexpected statistic encountered.  statistic=[ " + statistic 
                + " ], formattedStatistic=[ " + encodedStat + " ]");
          }
        }

        // set the summary counts
        result.setSummaryCounts(summaryCountsMap.values());

      } finally {
        this.queriedDatabase(timers, "selectCrossSourceSummary");
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
   * Retrieves a page of entity ID's for entities that have at least two
   * records from the associated data source that have matched.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/matched/entities")
  public SzEntitiesPageResponse getMatchedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             dataSource,
                             MATCHED_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the associated data source ambiguously matched against
   * another entity that has at least one record from the associated data
   * source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/ambiguously-matched/entities")
  public SzEntitiesPageResponse getAmbiguouslyMatchedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             dataSource,
                             AMBIGUOUS_MATCH_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the associated data source with a possible-match
   * relationship to another entity that has at least one record from
   * the associated data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/possibly-matched/entities")
  public SzEntitiesPageResponse getPossiblyMatchedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             dataSource,
                             POSSIBLE_MATCH_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the associated data source with a possible relation
   * to another entity that has at least one record from the associated
   * data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/possibly-related/entities")
  public SzEntitiesPageResponse getPossiblyRelatedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             dataSource,
                             POSSIBLE_RELATION_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the associated data source with a disclosed relationhip
   * to another entity that has at least one record from the "versus"
   * data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/disclosed-related/entities")
  public SzEntitiesPageResponse getDisclosedRelatedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             dataSource,
                             DISCLOSED_RELATION_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least
   * one record from the first data source and another record from
   * the second "versus" data source.
   *
   * @param dataSource The data source for which the entities are being
   *                   retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
   * @param entityIdBound The bound value for the entity ID's that will be
   *                      returned.
   * @param boundType The {@link SzBoundType} that describes how to apply the
   *                  specified entity ID bound.
   * @param pageSize The maximum number of entity ID's to return.
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/matched/entities")
  public SzEntitiesPageResponse getMatchedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             vsDataSource,
                             MATCHED_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the first data source ambiguously matched against
   * another entity that has at least one record from the "versus" data
   * source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/ambiguously-matched/entities")
  public SzEntitiesPageResponse getAmbiguouslyMatchedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             vsDataSource,
                             AMBIGUOUS_MATCH_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the first data source possibly matched against
   * another entity that has at least one record from the "versus" data
   * source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possibly-matched/entities")
  public SzEntitiesPageResponse getPossiblyMatchedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             vsDataSource,
                             POSSIBLE_MATCH_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the associated data source with a disclosed relationship
   * to another entity that has at least one record from the "versus"
   * data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possibly-related/entities")
  public SzEntitiesPageResponse getPossiblyRelatedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             vsDataSource,
                             POSSIBLE_RELATION_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have at least one
   * record from the associated data source ambiguously matched against
   * another entity that has at least one record from the associated data
   * source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/disclosed-related/entities")
  public SzEntitiesPageResponse getDisclosedRelatedEntityIds(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getEntityIds(dataSource,
                             vsDataSource,
                             DISCLOSED_RELATION_COUNT,
                             principle,
                             matchKey,
                             entityIdBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of entity ID's for entities that have the match type 
   * associated with the specific {@link SzReportStatistic} using the other
   * parameters for determining the page.
   *
   * @param dataSource The data source for which the entities are being 
   *                   retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param statistic The {@link SzReportStatistic} to use.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
   * @param entityIdBound The bound value for the entity ID's that will be
   *                      returned.
   * @param boundType The {@link SzBoundType} that describes how to apply the
   *                  specified entity ID bound.
   * @param pageSize The maximum number of entity ID's to return.
   * @param uriInfo The {@link UriInfo} for the request.
   * @return The {@link SzEntitiesPageResponse} describing the page of entities.
   * 
   * @throws NotFoundException If the specified entity size is less than one.
   */
  public SzEntitiesPageResponse getEntityIds(
      String            dataSource,
      String            vsDataSource,
      SzReportStatistic statistic,
      String            principle,
      String            matchKey,
      long              entityIdBound,
      SzBoundType       boundType,
      Integer           pageSize,
      Integer           sampleSize,
      UriInfo           uriInfo)
    throws NotFoundException
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    // check the data sources
    Set<String> dataSources = provider.getDataSources(dataSource, vsDataSource);
    if (!dataSources.contains(dataSource)) {
      throw new NotFoundException("Unrecognized data source: " + dataSource);
    }
    if (!dataSources.contains(vsDataSource)) {
      throw new NotFoundException("Unrecognized versus data source: " + vsDataSource);
    }

    try {
      String stat = statistic.principle(principle)
        .matchKey(matchKey).format();

      SzReportCode reportCode = (dataSource.equals(vsDataSource))
        ? DATA_SOURCE_SUMMARY : CROSS_SOURCE_SUMMARY;

      SzReportKey reportKey = new SzReportKey(reportCode,
                                              stat, 
                                              dataSource, 
                                              vsDataSource);

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

  /**
   * Retrieves a page of {@link SzRelation} instances describing the ambiguous match
   * relations between entities having at least one record from the first data source
   * ambiguously matched against another entity that has at least one record from the
   * "versus" data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/ambiguously-matched/relations")
  public SzRelationsPageResponse getAmbiguouslyMatchedRelations(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                String      relationBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getRelations(dataSource,
                             vsDataSource,
                             AMBIGUOUS_MATCH_COUNT,
                             principle,
                             matchKey,
                             relationBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of {@link SzRelation} instances describing the possible match
   * relations between entities having at least one record from the first data source
   * possibly matched against another entity that has at least one record from the
   * "versus" data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possibly-matched/relations")
  public SzRelationsPageResponse getPossiblyMatchedRelations(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                String      relationBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getRelations(dataSource,
                             vsDataSource,
                             POSSIBLE_MATCH_COUNT,
                             principle,
                             matchKey,
                             relationBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of {@link SzRelation} instances describing the possible relations
   * between entities having at least one record from the first data source possibly
   * related against another entity that has at least one record from the "versus"
   * data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/possibly-related/relations")
  public SzRelationsPageResponse getPossiblyRelatedRelations(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                String      relationBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getRelations(dataSource,
                             vsDataSource,
                             POSSIBLE_RELATION_COUNT,
                             principle,
                             matchKey,
                             relationBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of {@link SzRelation} instances describing the disclosed
   * relations between entities having at least one record from the first data
   * source having a disclosed relation to another entity that has at least
   * one record from the "versus" data source.
   *
   * @param dataSource The data source for which the entities are being retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
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
  @Path("/data-sources/{dataSourceCode}/vs/{vsDataSourceCode}/disclosed-related/relations")
  public SzRelationsPageResponse getDisclosedRelatedRelations(
    @PathParam("dataSourceCode")                                String      dataSource,
    @PathParam("vsDataSourceCode")                              String      vsDataSource,
    @QueryParam("principle")                                    String      principle,
    @QueryParam("matchKey")                                     String      matchKey,
    @QueryParam("bound")      @DefaultValue("0")                String      relationBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    return this.getRelations(dataSource,
                             vsDataSource,
                             DISCLOSED_RELATION_COUNT,
                             principle,
                             matchKey,
                             relationBound,
                             boundType,
                             pageSize,
                             sampleSize,
                             uriInfo);
  }

  /**
   * Retrieves a page of relations that have the match type associated with
   * the specific {@link SzReportStatistic} using the other parameters for
   * determining the page.
   *
   * @param dataSource The data source for which the entities are being 
   *                   retrieved.
   * @param vsDataSource The "versus" data source for which the entities
   *                     are being retrieved.
   * @param statistic The {@link SzReportStatistic} to use.
   * @param principle The optional principle to use.
   * @param matchKey The optional match key to use.
   * @param relationBound The bound value for the relation that is either a
   *                      single entity ID or a pair of entity ID's separated
   *                      by a colon.
   * @param boundType The {@link SzBoundType} that describes how to apply the
   *                  specified entity ID bound.
   * @param pageSize The maximum number of entity ID's to return.
   * @param uriInfo The {@link UriInfo} for the request.
   * 
   * @return The {@link SzRelationsPageResponse} describing the page of relations.
   * @throws NotFoundException If the specified entity size is less than one.
   */
  public SzRelationsPageResponse getRelations(
      String            dataSource,
      String            vsDataSource,
      SzReportStatistic statistic,
      String            principle,
      String            matchKey,
      String            relationBound,
      SzBoundType       boundType,
      Integer           pageSize,
      Integer           sampleSize,
      UriInfo           uriInfo)
    throws NotFoundException
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    // check the data sources
    Set<String> dataSources = provider.getDataSources(dataSource, vsDataSource);
    if (!dataSources.contains(dataSource)) {
      throw new NotFoundException("Unrecognized data source: " + dataSource);
    }
    if (!dataSources.contains(vsDataSource)) {
      throw new NotFoundException("Unrecognized versus data source: " + vsDataSource);
    }

    try {
      String stat = statistic.principle(principle)
        .matchKey(matchKey).format();

      SzReportCode reportCode = (dataSource.equals(vsDataSource))
        ? DATA_SOURCE_SUMMARY : CROSS_SOURCE_SUMMARY;

      SzReportKey reportKey = new SzReportKey(reportCode,
                                              stat, 
                                              dataSource, 
                                              vsDataSource);

      SzRelationsPage page = this.retrieveRelationsPage(GET, 
                                                        uriInfo, 
                                                        timers, 
                                                        provider, 
                                                        reportKey.toString(),
                                                        relationBound,
                                                        boundType, 
                                                        pageSize,
                                                        sampleSize);

      return SzRelationsPageResponse.FACTORY.create(
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
