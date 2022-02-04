package com.senzing.poc.services;

import com.senzing.api.model.SzBasicResponse;
import com.senzing.api.model.SzHttpMethod;
import com.senzing.api.model.SzLoadRecordResponse;
import com.senzing.api.services.*;
import com.senzing.poc.model.SzQueueInfo;
import com.senzing.poc.model.SzQueueInfoResponse;
import com.senzing.poc.model.impl.SzQueueInfoResponseImpl;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.JsonUtilities;
import com.senzing.util.Timers;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import java.util.Collections;
import java.util.Map;

import static com.senzing.api.model.SzHttpMethod.*;
import static javax.ws.rs.core.MediaType.*;

/**
 * Extends {@link EntityDataServices} to add stream-loading functionality.
 */
public class EntityStreamServices extends EntityDataServices
  implements StreamLoadSupport
{
  /**
   * The description to use for the load messaging queue.
   */
  public static final String LOAD_QUEUE_DESCRIPTION
      = "Load Message Queue";

  /**
   * Gets information on the load queue, providing the implementation of
   * <tt>"GET /load-queue"</tt>.
   *
   * @param uriInfo The {@link UriInfo} for the equest.
   * @return The {@link SzQueueInfoResponseImpl} describing the queue.
   */
  @GET
  @Path("load-queue")
  @Produces(APPLICATION_JSON)
  public SzQueueInfoResponse getLoadQueueInfo(@Context UriInfo uriInfo) {
    Timers timers = this.newTimers();

    try {
      // get the provider
      SzPocProvider provider = (SzPocProvider) this.getApiProvider();

      // check if there is a load sink configured
      if (!provider.hasLoadSink()) {
        throw newNotFoundException(GET, uriInfo, timers,
                                   "No load queue is configured");
      }

      // get the load sink
      SzMessageSink loadSink = provider.acquireLoadSink();
      try {
        return this.newQueueInfoResponse(
            GET, uriInfo, timers, LOAD_QUEUE_DESCRIPTION, loadSink);

      } finally {
        provider.releaseLoadSink(loadSink);
      }

    } catch (ServerErrorException e) {
      e.printStackTrace();
      throw e;

    } catch (WebApplicationException e) {
      throw e;

    } catch (Exception e) {
      e.printStackTrace();
      throw this.newInternalServerErrorException(POST, uriInfo, timers, e);
    }
  }

  /**
   * Creates a new instance of {@link SzQueueInfo}.
   *
   * @return A new instance of {@link SzQueueInfo}.
   */
  protected SzQueueInfo newQueueInfo(String description, SzMessageSink sink) {
    SzQueueInfo info = SzQueueInfo.FACTORY.create();
    info.setDescription(description);
    info.setProviderType(sink.getProviderType());
    info.setMessageCount(sink.getMessageCount());
    return info;
  }

  /**
   * Creates a new instance of {@link SzQueueInfoResponse}.
   *
   * @return A new instance of {@link SzQueueInfoResponse}.
   */
  protected SzQueueInfoResponse newQueueInfoResponse(SzHttpMethod   method,
                                                     UriInfo        uriInfo,
                                                     Timers         timers,
                                                     String         description,
                                                     SzMessageSink  sink)
  {
    return SzQueueInfoResponse.FACTORY.create(
        this.newMeta(method, 200, timers),
        this.newLinks(uriInfo),
        this.newQueueInfo(description, sink));
  }

  /**
   * Provides the implementation for
   * <tt>POST /load-queue/data-sources/{dataSourceCode}/records</tt>
   *
   * @param dataSourceCode The data source code from the URI path.
   * @param loadId The optional load ID query parameter for the record.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param recordJsonData The Senzing-format JSON text describing the record.
   * @return The {@link SzBasicResponse} describing the response.
   */
  @POST
  @Path("load-queue/data-sources/{dataSourceCode}/records")
  @Produces(APPLICATION_JSON)
  public SzBasicResponse postRecordToLoadQueue(
      @PathParam("dataSourceCode") String dataSourceCode,
      @QueryParam("loadId")        String loadId,
      @Context UriInfo                    uriInfo,
      String                              recordJsonData)
  {
    Timers timers = this.newTimers();

    try {
      SzPocProvider provider = (SzPocProvider) this.getApiProvider();
      if (!provider.hasLoadSink()) {
        throw newForbiddenException(
            POST, uriInfo, timers,
            "Cannot perform stream loading operations.  No load queue was "
            + "configured at startup.");
      }

      // normalize the data source code and the load ID
      dataSourceCode  = dataSourceCode.trim().toUpperCase();
      loadId          = this.normalizeString(loadId);

      // check if we are allowed to load records
      this.ensureLoadingIsAllowed(provider, POST, uriInfo, timers);

      // ensure/verify fields in the JSON text
      String recordText = this.ensureJsonFields(
          POST,
          uriInfo,
          timers,
          recordJsonData,
          Collections.singletonMap("DATA_SOURCE", dataSourceCode),
          Collections.singletonMap("ENTITY_TYPE", "GENERIC"));

      // cleanup the record ID and load ID in the JSON text
      JsonObject recordJson   = JsonUtilities.parseJsonObject(recordText);
      String     jsonRecordId = JsonUtilities.getString(recordJson, "RECORD_ID");
      if ((jsonRecordId != null && jsonRecordId.trim().length() == 0)
          || (loadId != null && loadId.trim().length() > 0))
      {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder(recordJson);

        // we have an empty record ID, we need to strip it from the JSON
        if (jsonRecordId != null && jsonRecordId.trim().length() == 0) {
          jsonBuilder.remove("RECORD_ID");
        }

        // if a load ID was provided then we need to add to the JSON
        if (loadId != null && loadId.trim().length() > 0) {
          jsonBuilder.remove("SOURCE_ID");
          jsonBuilder.add("SOURCE_ID", loadId);
        }

        // reconstitute the JSON text
        recordJson = jsonBuilder.build();
        recordText = JsonUtilities.toJsonText(recordJson);
      }

      // check that the data source code is valid
      this.checkDataSource(POST, uriInfo, timers, dataSourceCode, provider);

      // load the record asynchronously
      this.asyncLoadRecord(provider, null, timers, recordText);

      // construct the response
      SzBasicResponse response = SzBasicResponse.FACTORY.create(
          this.newMeta(POST, 200, timers),
          this.newLinks(uriInfo));

      // return the response
      return response;

    } catch (ServerErrorException e) {
      e.printStackTrace();
      throw e;

    } catch (WebApplicationException e) {
      throw e;

    } catch (Exception e) {
      e.printStackTrace();
      throw this.newInternalServerErrorException(POST, uriInfo, timers, e);
    }
  }

  /**
   * Provides the implementation for
   * <tt>PUT /load-queue/data-sources/{dataSourceCode}/records/{recordId}</tt>.
   *
   * @param dataSourceCode The data source code from the URI path.
   * @param recordId The record ID of the record being loaded.
   * @param loadId The optional load ID query parameter for the record.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param recordJsonData The Senzing-format JSON text describing the record.
   * @return The {@link SzLoadRecordResponse} describing the response.
   */
  @PUT
  @Path("load-queue/data-sources/{dataSourceCode}/records/{recordId}")
  public SzBasicResponse putRecordOnLoadQueue(
      @PathParam("dataSourceCode")  String  dataSourceCode,
      @PathParam("recordId")        String  recordId,
      @QueryParam("loadId")         String  loadId,
      @Context                      UriInfo uriInfo,
      String                                recordJsonData)
  {
    Timers timers = this.newTimers();
    try {
      SzPocProvider provider = (SzPocProvider) this.getApiProvider();
      if (!provider.hasLoadSink()) {
        throw newForbiddenException(
            POST, uriInfo, timers,
            "Cannot perform stream loading operations.  No load queue was "
                + "configured at startup.");
      }

      // ensure loading is allowed
      this.ensureLoadingIsAllowed(provider, PUT, uriInfo, timers);

      // normalize the data source code and load ID
      dataSourceCode  = dataSourceCode.trim().toUpperCase();
      loadId          = this.normalizeString(loadId);

      // setup the ensure map and default map
      Map<String,String> map = Map.of("DATA_SOURCE", dataSourceCode,
                                      "RECORD_ID", recordId);

      Map<String,String> defaultMap = Map.of("ENTITY_TYPE", "GENERIC");

      // validate and augment the JSON text
      String recordText = this.ensureJsonFields(PUT,
                                                uriInfo,
                                                timers,
                                                recordJsonData,
                                                map,
                                                defaultMap);

      // check if the load ID needs to be added on
      if (loadId != null && loadId.trim().length() > 0) {
        JsonObject        recordJson  = JsonUtilities.parseJsonObject(recordText);
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder(recordJson);

        jsonBuilder.remove("SOURCE_ID");
        jsonBuilder.add("SOURCE_ID", loadId);

        // reconstitute the JSON text
        recordJson = jsonBuilder.build();
        recordText = JsonUtilities.toJsonText(recordJson);
      }

      // check that the data source code is valid
      this.checkDataSource(PUT, uriInfo, timers, dataSourceCode, provider);

      // load the record asynchronously
      this.asyncLoadRecord(provider, null, timers, recordText);

      // construct the response
      SzBasicResponse response = SzBasicResponse.FACTORY.create(
          this.newMeta(PUT, 200, timers),
          this.newLinks(uriInfo));

      // return the response
      return response;

    } catch (ServerErrorException e) {
      e.printStackTrace();
      throw e;

    } catch (WebApplicationException e) {
      throw e;

    } catch (Exception e) {
      e.printStackTrace();
      throw this.newInternalServerErrorException(PUT, uriInfo, timers, e);
    }
  }
}
