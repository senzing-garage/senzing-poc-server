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

import com.senzing.poc.model.SzEntityRelationsBreakdown;
import com.senzing.poc.model.SzEntityRelationsCount;
import com.senzing.poc.model.SzEntitiesPage;
import com.senzing.poc.model.SzEntityRelationsBreakdownResponse;
import com.senzing.poc.model.SzEntityRelationsCountResponse;
import com.senzing.poc.model.SzEntitiesPageResponse;
import com.senzing.poc.model.SzBoundType;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.api.services.ServicesSupport;
import com.senzing.util.AccessToken;
import com.senzing.util.Timers;
import com.senzing.api.model.SzHttpMethod;

import static com.senzing.sql.SQLUtilities.*;
import static com.senzing.api.model.SzHttpMethod.POST;
import static com.senzing.util.LoggingUtilities.*;
import static javax.ws.rs.core.MediaType.*;
import static com.senzing.api.model.SzHttpMethod.GET;

/**
 * Entity Relations Breakdown REST services.
 */
@Path("/statistics/relations")
@Produces(APPLICATION_JSON)
public class EntityRelationsBreakdownServices
  implements DataMartServicesSupport 
{
  /**
   * Gets an {@link SzEntityRelationsBreakdownResponse} describing the 
   * entity size breakdown which is the number of entities in the
   * entity repository having each distinct entity size that exists
   * in the entity repository.
   * 
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/")
  public SzEntityRelationsBreakdownResponse getEntityRelationsBreakdown(
    @Context UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzEntityRelationsBreakdown breakdown = this.getBreakdown(GET, 
                                                                 uriInfo, 
                                                                 timers, 
                                                                 provider);

        return SzEntityRelationsBreakdownResponse.FACTORY.create(this.newMeta(GET, 200, timers),
                                                                 this.newLinks(uriInfo),
                                                                 breakdown);
        
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
   * Internal method for obtaining the entity size breakdown statistics.
   * 
   * @param httpMethod The {@link SzHttpMethod} of the request.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} associated with the request.
   * @param provider The {@link SzPocProvider} for the request context.
   * 
   * @return The {@link SzCountStats} describing the statistics.
   * 
   * @throws ServiceUnavailableException If the data mart is not yet ready to 
   *                                     service a request.
   * @throws InternalServerErrorException If an internal error occurs.
   */
  protected SzEntityRelationsBreakdown getBreakdown(SzHttpMethod   httpMethod,
                                                    UriInfo        uriInfo,
                                                    Timers         timers,
                                                    SzPocProvider  provider)
      throws ServiceUnavailableException, InternalServerErrorException
  {
    // get the connection
    Connection  conn  = null;
    Statement   stmt  = null;
    ResultSet   rs    = null;

    SzEntityRelationsBreakdown result = SzEntityRelationsBreakdown.FACTORY.create();
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);
      stmt = conn.createStatement();

      // get the total entity count
      this.queryingDatabase(timers, "selectEntityRelationsBreakdown");
      try {
        // prepare the total entity count query
        rs = stmt.executeQuery(
          "SELECT statistic, entity_count FROM sz_dm_report WHERE report='ERB'");
        
        // read the results
        while (rs.next()) {
          SzEntityRelationsCount relationsCount = SzEntityRelationsCount.FACTORY.create();
          String  stat      = rs.getString(1);
          long    count     = rs.getLong(2);
          int     relations = Integer.parseInt(stat);

          relationsCount.setRelationsCount(relations);
          relationsCount.setEntityCount(count);

          result.addEntityRelationsCount(relationsCount);
        }


      } finally {
        this.queriedDatabase(timers, "selectEntityRelationsBreakdown");
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

  /**
   * Gets the number of entities in the entity respository having a specific
   * number of entity relations.
   *
   * @param relationsCount The number of entity relations that for which the
   *                       count is being requested.
   * @param uriInfo The {@link UriInfo} for the request.
   * 
   * @throws NotFoundException If the specified entity size is less than one.
   */
  @GET
  @Path("/{relationsCount}")
  public SzEntityRelationsCountResponse getEntityRelationsCount(
    @PathParam("relationsCount")  int     relationsCount,
    @Context                      UriInfo uriInfo)
    throws NotFoundException
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    // check the entity size
    if (relationsCount < 0) {
      throw this.newNotFoundException(GET, uriInfo, timers, 
        "The relations count cannot be less than zero: " + relationsCount);
    }
    
    try {
        SzEntityRelationsCount sizeCount = this.doGetEntityRelationsCount(relationsCount,
                                                                          GET, 
                                                                          uriInfo, 
                                                                          timers, 
                                                                          provider);

        return SzEntityRelationsCountResponse.FACTORY.create(
          this.newMeta(GET, 200, timers),
          this.newLinks(uriInfo),
          sizeCount);
        
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
   * number of entity relations.
   * 
   * @param relationsCount The number of entity relations that for which the
   *                       entity count is requested.
   * @param httpMethod The {@link SzHttpMethod} of the request.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} associated with the request.
   * @param provider The {@link SzPocProvider} for the request context.
   * 
   * @return The {@link SzEntityRelationsCount} describing the statistics.
   * 
   * @throws ServiceUnavailableException If the data mart is not yet ready to 
   *                                     service a request.
   * @throws InternalServerErrorException If an internal error occurs.
   */
  protected SzEntityRelationsCount doGetEntityRelationsCount(
    int               relationsCount,
    SzHttpMethod      httpMethod,
    UriInfo           uriInfo,
    Timers            timers,
    SzPocProvider     provider)
      throws ServiceUnavailableException,
             InternalServerErrorException
  {
    // get the connection
    Connection          conn    = null;
    PreparedStatement   ps      = null;
    ResultSet           rs      = null;
    SzEntityRelationsCount   result  = SzEntityRelationsCount.FACTORY.create();

    result.setRelationsCount(relationsCount);
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);

      this.queryingDatabase(timers, "selectCountForEntityRelations");
      try {
        // prepare the statement
        ps = conn.prepareStatement(
          "SELECT entity_count FROM sz_dm_report "
          + "WHERE report='ERB' AND statistic=?");

        // bind the parameters
        ps.setString(1, String.valueOf(relationsCount));

        // execute the query
        rs = ps.executeQuery();
        
        // check if we have a result
        if (rs.next()) {
          long entityCount = rs.getLong(1);

          result.setEntityCount(entityCount);
        } else {
          result.setEntityCount(0L);
        }

      } finally {
        this.queriedDatabase(timers, "selectCountForEntityRelations");
      }

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
   * Retrieves a page of entity ID's that identifies entities having the 
   * specified number of entity relations.
   *
   * @param relationsCount The number of entity relations for which the 
   *                       entity count is being requested.
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
  @Path("/{relationsCount}/entities")
  public SzEntitiesPageResponse getEntityIdsForEntitySize(
    @PathParam("relationsCount")                                int         relationsCount,
    @QueryParam("bound")      @DefaultValue("0")                long        entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    // check the entity size
    if (relationsCount < 1) {
      throw this.newNotFoundException(GET, uriInfo, timers, 
        "The relations count cannot be less than zero: " + relationsCount);
    }
    
    try {
      String reportKey = "ERB:" + relationsCount;

      SzEntitiesPage page = this.retrieveEntitiesPage(GET, 
                                                      uriInfo, 
                                                      timers, 
                                                      provider,
                                                      reportKey, 
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
