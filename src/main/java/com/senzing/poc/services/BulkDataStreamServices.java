package com.senzing.poc.services;

import com.senzing.api.model.SzBulkLoadResponse;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.AccessToken;
import com.senzing.util.Timers;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import static com.senzing.api.model.SzHttpMethod.POST;
import static com.senzing.util.LoggingUtilities.logOnceAndThrow;
import static javax.ws.rs.core.MediaType.*;

/**
 * Bulk Data REST services.
 */
@Path("/load-queue/bulk-data")
@Produces(APPLICATION_JSON)
public class BulkDataStreamServices implements BulkDataStreamSupport {
  /**
   * Loads the bulk data records via form.
   *
   * @param dataSource The data source to assign to the loaded records unless
   *                   another data source mapping supercedes this default.
   * @param mapDataSources The JSON string mapping specific data sources to
   *                       alternate data source names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       data source specified.
   * @param mapDataSourceList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" data source
   *                          then the delimiter then the target data source.
   * @param entityType The entity type to assign to the loaded records unless
   *                   another entity type mapping supercedes this default.
   * @param mapEntityTypes The JSON string mapping specific entity types to
   *                       alternate entity type names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       entity type specified.
   * @param mapEntityTypeList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" entity type
   *                          then the delimiter then the target entity type.
   * @param loadId The optional load ID to use for loading the records.
   * @param maxBatchCount The maximum number of records to include in a
   *                      micro-batch, though less may be sent if the records
   *                      are large or if there is a delay in receiving more.
   * @param maxFailures The maximum number of failures or a negative number if
   *                    no maximum.
   * @param mediaType The media type for the content.
   * @param dataInputStream The input stream to read the uploaded data.
   * @param fileMetaData The form meta data for the uploaded file.
   * @param uriInfo The {@link UriInfo} for the request.
   */
  @POST
  @Path("/records")
  public SzBulkLoadResponse loadBulkRecordsViaForm(
      @QueryParam("dataSource") String dataSource,
      @QueryParam("mapDataSources") String mapDataSources,
      @QueryParam("mapDataSource") List<String> mapDataSourceList,
      @QueryParam("entityType") String entityType,
      @QueryParam("mapEntityTypes") String mapEntityTypes,
      @QueryParam("mapEntityType") List<String> mapEntityTypeList,
      @QueryParam("loadId") String loadId,
      @DefaultValue("10") @QueryParam("maxBatchCount") int maxBatchCount,
      @DefaultValue("0") @QueryParam("maxFailures") int maxFailures,
      @HeaderParam("Content-Type") MediaType mediaType,
      @FormDataParam("data") InputStream dataInputStream,
      @FormDataParam("data") FormDataContentDisposition fileMetaData,
      @Context UriInfo uriInfo)
  {
    SzPocProvider provider    = (SzPocProvider) this.getApiProvider();
    Timers        timers      = this.newTimers();
    AccessToken   accessToken = this.prepareStreamLoadOperation(provider,
                                                                uriInfo,
                                                                timers);
    try {
      return this.streamLoadBulkRecords(provider,
                                        timers,
                                        dataSource,
                                        mapDataSources,
                                        mapDataSourceList,
                                        entityType,
                                        mapEntityTypes,
                                        mapEntityTypeList,
                                        loadId,
                                        maxBatchCount,
                                        maxFailures,
                                        mediaType,
                                        dataInputStream,
                                        fileMetaData,
                                        uriInfo,
                                        null,
                                        null,
                                        null,
                                        null);

    } catch (ForbiddenException e) {
      throw e;

    } catch (RuntimeException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      throw logOnceAndThrow(new RuntimeException(e));

    } finally {
      provider.concludeProlongedOperation(accessToken);
    }
  }

  /**
   * Loads the bulk data records via direct upload.
   *
   * @param dataSource The data source to assign to the loaded records unless
   *                   another data source mapping supercedes this default.
   * @param mapDataSources The JSON string mapping specific data sources to
   *                       alternate data source names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       data source specified.
   * @param mapDataSourceList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" data source
   *                          then the delimiter then the target data source.
   * @param entityType The entity type to assign to the loaded records unless
   *                   another entity type mapping supercedes this default.
   * @param mapEntityTypes The JSON string mapping specific entity types to
   *                       alternate entity type names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       entity type specified.
   * @param mapEntityTypeList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" entity type
   *                          then the delimiter then the target entity type.
   * @param loadId The optional load ID to use for loading the records.
   * @param maxBatchCount The maximum number of records to include in a
   *                      micro-batch, though less may be sent if the records
   *                      are large or if there is a delay in receiving more.
   * @param maxFailures The maximum number of failures or a negative number if
   *                    no maximum.
   */
  @POST
  @Path("/records")
  @Consumes({ MediaType.APPLICATION_JSON,
      MediaType.TEXT_PLAIN,
      "text/csv",
      "application/x-jsonlines"})
  public SzBulkLoadResponse loadBulkRecordsDirect(
      @QueryParam("dataSource") String dataSource,
      @QueryParam("mapDataSources") String mapDataSources,
      @QueryParam("mapDataSource") List<String> mapDataSourceList,
      @QueryParam("entityType") String entityType,
      @QueryParam("mapEntityTypes") String mapEntityTypes,
      @QueryParam("mapEntityType") List<String> mapEntityTypeList,
      @QueryParam("loadId") String loadId,
      @DefaultValue("10") @QueryParam("maxBatchCount") int maxBatchCount,
      @DefaultValue("0") @QueryParam("maxFailures") int maxFailures,
      @HeaderParam("Content-Type") MediaType mediaType,
      InputStream dataInputStream,
      @Context UriInfo uriInfo)
  {
    SzPocProvider provider    = (SzPocProvider) this.getApiProvider();
    Timers        timers      = this.newTimers();
    AccessToken   accessToken = this.prepareStreamLoadOperation(provider,
                                                                uriInfo,
                                                                timers);

    if (accessToken == null) {
      throw this.newServiceUnavailableErrorException(
          POST, uriInfo, timers,
          "Too many prolonged operations running.  Try again later.");
    }
    try {
      return this.streamLoadBulkRecords(provider,
                                        timers,
                                        dataSource,
                                        mapDataSources,
                                        mapDataSourceList,
                                        entityType,
                                        mapEntityTypes,
                                        mapEntityTypeList,
                                        loadId,
                                        maxBatchCount,
                                        maxFailures,
                                        mediaType,
                                        dataInputStream,
                                        null,
                                        uriInfo,
                                        null,
                                        null,
                                        null,
                                        null);

    } catch (ForbiddenException e) {
      throw e;

    } catch (RuntimeException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      throw logOnceAndThrow(new RuntimeException(e));

    } finally {
      provider.concludeProlongedOperation(accessToken);
    }
  }

  /**
   * Loads bulk data records via form using SSE.
   *
   * @param dataSource The data source to assign to the loaded records unless
   *                   another data source mapping supercedes this default.
   * @param mapDataSources The JSON string mapping specific data sources to
   *                       alternate data source names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       data source specified.
   * @param mapDataSourceList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" data source
   *                          then the delimiter then the target data source.
   * @param entityType The entity type to assign to the loaded records unless
   *                   another entity type mapping supercedes this default.
   * @param mapEntityTypes The JSON string mapping specific entity types to
   *                       alternate entity type names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       entity type specified.
   * @param mapEntityTypeList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" entity type
   *                          then the delimiter then the target entity type.
   * @param loadId The optional load ID to use for loading the records.
   * @param maxBatchCount The maximum number of records to include in a
   *                      micro-batch, though less may be sent if the records
   *                      are large or if there is a delay in receiving more.
   * @param maxFailures The maximum number of failures or a negative number if
   *                    no maximum.
   * @param progressPeriod The suggested maximum time between SSE `progress`
   *                       events specified in milliseconds.  If not specified
   *                       then the default of `3000` milliseconds (i.e.: 3
   *                       seconds) is used.
   * @param mediaType The media type for the content.
   * @param dataInputStream The input stream to read the uploaded data.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param sseEventSink The {@link SseEventSink} for the SSE protocol.
   * @param sse The {@link Sse} instance for the SSE protocol.
   */
  @POST
  @Path("/records")
  @Produces(TEXT_EVENT_STREAM)
  public void loadBulkRecordsViaForm(
      @QueryParam("dataSource") String dataSource,
      @QueryParam("mapDataSources") String mapDataSources,
      @QueryParam("mapDataSource") List<String> mapDataSourceList,
      @QueryParam("entityType") String entityType,
      @QueryParam("mapEntityTypes") String mapEntityTypes,
      @QueryParam("mapEntityType") List<String> mapEntityTypeList,
      @QueryParam("loadId") String loadId,
      @DefaultValue("10") @QueryParam("maxBatchCount") int maxBatchCount,
      @DefaultValue("0") @QueryParam("maxFailures") int maxFailures,
      @HeaderParam("Content-Type") MediaType mediaType,
      @FormDataParam("data") InputStream dataInputStream,
      @FormDataParam("data") FormDataContentDisposition fileMetaData,
      @Context UriInfo uriInfo,
      @QueryParam("progressPeriod") @DefaultValue("3000") long progressPeriod,
      @Context SseEventSink sseEventSink,
      @Context Sse sse)

  {
    SzPocProvider provider    = (SzPocProvider) this.getApiProvider();
    Timers        timers      = this.newTimers();
    AccessToken   accessToken = this.prepareStreamLoadOperation(provider,
                                                                uriInfo,
                                                                timers);
    try {
      this.streamLoadBulkRecords(provider,
                                 timers,
                                 dataSource,
                                 mapDataSources,
                                 mapDataSourceList,
                                 entityType,
                                 mapEntityTypes,
                                 mapEntityTypeList,
                                 loadId,
                                 maxBatchCount,
                                 maxFailures,
                                 mediaType,
                                 dataInputStream,
                                 fileMetaData,
                                 uriInfo,
                                 progressPeriod,
                                 sseEventSink,
                                 sse,
                                 null);

    } catch (ForbiddenException e) {
      throw e;

    } catch (RuntimeException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      throw logOnceAndThrow(new RuntimeException(e));

    } finally {
      provider.concludeProlongedOperation(accessToken);
    }
  }

  /**
   * Loads the bulk data records via direct upload using SSE.
   *
   * @param dataSource The data source to assign to the loaded records unless
   *                   another data source mapping supercedes this default.
   * @param mapDataSources The JSON string mapping specific data sources to
   *                       alternate data source names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       data source specified.
   * @param mapDataSourceList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" data source
   *                          then the delimiter then the target data source.
   * @param entityType The entity type to assign to the loaded records unless
   *                   another entity type mapping supercedes this default.
   * @param mapEntityTypes The JSON string mapping specific entity types to
   *                       alternate entity type names.  A mapping from
   *                       empty-string is used for mapping records with no
   *                       entity type specified.
   * @param mapEntityTypeList The {@link List} of delimited strings that begin
   *                          the delimiter, followed by the "from" entity type
   *                          then the delimiter then the target entity type.
   * @param loadId The optional load ID to use for loading the records.
   * @param maxBatchCount The maximum number of records to include in a
   *                      micro-batch, though less may be sent if the records
   *                      are large or if there is a delay in receiving more.
   * @param maxFailures The maximum number of failures or a negative number if
   *                    no maximum.
   * @param progressPeriod The suggested maximum time between SSE `progress`
   *                       events specified in milliseconds.  If not specified
   *                       then the default of `3000` milliseconds (i.e.: 3
   *                       seconds) is used.
   * @param mediaType The media type for the content.
   * @param dataInputStream The input stream to read the uploaded data.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param sseEventSink The {@link SseEventSink} for the SSE protocol.
   * @param sse The {@link Sse} instance for the SSE protocol.
   */
  @POST
  @Path("/records")
  @Consumes({ APPLICATION_JSON,
      TEXT_PLAIN,
      TEXT_CSV,
      APPLICATION_JSONLINES })
  @Produces(TEXT_EVENT_STREAM)
  public void loadBulkRecordsDirect(
      @QueryParam("dataSource") String dataSource,
      @QueryParam("mapDataSources") String mapDataSources,
      @QueryParam("mapDataSource") List<String> mapDataSourceList,
      @QueryParam("entityType") String entityType,
      @QueryParam("mapEntityTypes") String mapEntityTypes,
      @QueryParam("mapEntityType") List<String> mapEntityTypeList,
      @QueryParam("loadId") String loadId,
      @DefaultValue("10") @QueryParam("maxBatchCount") int maxBatchCount,
      @DefaultValue("0") @QueryParam("maxFailures") int maxFailures,
      @HeaderParam("Content-Type") MediaType mediaType,
      InputStream dataInputStream,
      @Context UriInfo uriInfo,
      @QueryParam("progressPeriod") @DefaultValue("3000") long progressPeriod,
      @Context SseEventSink sseEventSink,
      @Context Sse sse)
  {
    SzPocProvider provider    = (SzPocProvider) this.getApiProvider();
    Timers        timers      = this.newTimers();
    AccessToken   accessToken = this.prepareStreamLoadOperation(provider,
                                                                uriInfo,
                                                                timers);
    try {
      this.streamLoadBulkRecords(provider,
                                 timers,
                                 dataSource,
                                 mapDataSources,
                                 mapDataSourceList,
                                 entityType,
                                 mapEntityTypes,
                                 mapEntityTypeList,
                                 loadId,
                                 maxBatchCount,
                                 maxFailures,
                                 mediaType,
                                 dataInputStream,
                                 null,
                                 uriInfo,
                                 progressPeriod,
                                 sseEventSink,
                                 sse,
                                 null);

    } catch (ForbiddenException e) {
      throw e;

    } catch (RuntimeException e) {
      throw logOnceAndThrow(e);

    } catch (Exception e) {
      throw logOnceAndThrow(new RuntimeException(e));

    } finally {
      provider.concludeProlongedOperation(accessToken);
    }
  }
}
