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

import com.senzing.poc.model.SzEntitySizeBreakdown;
import com.senzing.poc.model.SzEntitySizeCount;
import com.senzing.poc.model.SzEntitiesPage;
import com.senzing.poc.model.SzEntitySizeBreakdownResponse;
import com.senzing.poc.model.SzEntitySizeCountResponse;
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
 * Entity Size Breakdown REST services.
 */
@Path("/statistics/sizes")
@Produces(APPLICATION_JSON)
public class EntitySizeBreakdownServices
  implements DataMartServicesSupport 
{
  /**
   * Gets an {@link SzEntitySizeBreakdownResponse} describing the 
   * entity size breakdown which is the number of entities in the
   * entity repository having each distinct entity size that exists
   * in the entity repository.
   * 
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @GET
  @Path("/")
  public SzEntitySizeBreakdownResponse getEntitySizeBreakdown(
    @Context UriInfo uriInfo)
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();
    try {
        SzEntitySizeBreakdown breakdown = this.getBreakdown(GET, 
                                                            uriInfo, 
                                                            timers, 
                                                            provider);

        return SzEntitySizeBreakdownResponse.FACTORY.create(this.newMeta(GET, 200, timers),
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
  protected SzEntitySizeBreakdown getBreakdown(SzHttpMethod   httpMethod,
                                               UriInfo        uriInfo,
                                               Timers         timers,
                                               SzPocProvider  provider)
      throws ServiceUnavailableException, InternalServerErrorException
  {
    // get the connection
    Connection  conn  = null;
    Statement   stmt  = null;
    ResultSet   rs    = null;

    SzEntitySizeBreakdown result = SzEntitySizeBreakdown.FACTORY.create();
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);
      stmt = conn.createStatement();

      // get the total entity count
      this.queryingDatabase(timers, "selectEntitySizeBreakdown");
      try {
        // prepare the total entity count query
        rs = stmt.executeQuery(
          "SELECT statistic, entity_count FROM sz_dm_report WHERE report='ESB'");
        
        // read the results
        while (rs.next()) {
          SzEntitySizeCount sizeCount = SzEntitySizeCount.FACTORY.create();
          String  stat  = rs.getString(1);
          long    count = rs.getLong(2);
          int     size  = Integer.parseInt(stat);

          sizeCount.setEntitySize(size);
          sizeCount.setEntityCount(count);

          result.addEntitySizeCount(sizeCount);
        }


      } finally {
        this.queriedDatabase(timers, "selectEntitySizeBreakdown");
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
   * Gets the number of entities in the entity repository having the 
   * specified entity size.
   *
   * @param entitySize The entity size for which the entity count is being
   *                   requested.
   * @param uriInfo The {@link UriInfo} for the request.
   * 
   * @throws NotFoundException If the specified entity size is less than one.
   */
  @GET
  @Path("/{entitySize}")
  public SzEntitySizeCountResponse getEntitySizeCount(
    @PathParam("entitySize")  int     entitySize,
    @Context                  UriInfo uriInfo)
    throws NotFoundException
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    // check the entity size
    if (entitySize < 1) {
      throw this.newNotFoundException(GET, uriInfo, timers, 
        "The entity size cannot be less than one: " + entitySize);
    }
    
    try {
        SzEntitySizeCount sizeCount = this.doGetEntitySizeCount(entitySize,
                                                                GET, 
                                                                uriInfo, 
                                                                timers, 
                                                                provider);

        return SzEntitySizeCountResponse.FACTORY.create(
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
   * entity size.
   * 
   * @param entitySize The entity size for which the count is being requested.
   * @param httpMethod The {@link SzHttpMethod} of the request.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} associated with the request.
   * @param provider The {@link SzPocProvider} for the request context.
   * 
   * @return The {@link SzEntitySizeCount} describing the statistics.
   * 
   * @throws ServiceUnavailableException If the data mart is not yet ready to 
   *                                     service a request.
   * @throws InternalServerErrorException If an internal error occurs.
   */
  protected SzEntitySizeCount doGetEntitySizeCount(
    int               entitySize,
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
    SzEntitySizeCount   result  = SzEntitySizeCount.FACTORY.create();

    result.setEntitySize(entitySize);
    try {
      // get the connection to the data mart database
      conn = this.getConnection(httpMethod, uriInfo, timers, provider);

      this.queryingDatabase(timers, "selectCountForEntitySize");
      try {
        // prepare the statement
        ps = conn.prepareStatement(
          "SELECT entity_count FROM sz_dm_report "
          + "WHERE report='ESB' AND statistic=?");

        // bind the parameters
        ps.setString(1, String.valueOf(entitySize));

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
        this.queriedDatabase(timers, "selectCountForEntitySize");
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
   * specified entity size.
   *
   * @param entitySize The entity size for which the entity count is being
   *                   requested.
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
  @Path("/{entitySize}/entities")
  public SzEntitiesPageResponse getEntityIdsForEntitySize(
    @PathParam("entitySize")                                    int         entitySize,
    @QueryParam("bound")                                        String      entityIdBound,
    @QueryParam("boundType")  @DefaultValue("EXCLUSIVE_LOWER")  SzBoundType boundType,
    @QueryParam("pageSize")                                     Integer     pageSize,
    @QueryParam("sampleSize")                                   Integer     sampleSize,
    @Context                                                    UriInfo     uriInfo)
    throws NotFoundException
  {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    // check the entity size
    if (entitySize < 1) {
      throw this.newNotFoundException(GET, uriInfo, timers, 
        "The entity size cannot be less than one: " + entitySize);
    }

    try {
      String reportKey = "ESB:" + entitySize;

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
