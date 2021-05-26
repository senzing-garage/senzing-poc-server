package com.senzing.poc.services;

import com.senzing.api.model.SzServerInfo;
import com.senzing.api.services.AdminServices;
import com.senzing.api.services.SzApiProvider;
import com.senzing.poc.model.SzPocServerInfo;
import com.senzing.poc.server.SzPocProvider;

/**
 * Extends {@link AdminServices} to augment the <tt>GET /server-info</tt>
 * endpoint to add information regarding the load queue.
 */
public class PocAdminServices extends AdminServices {
  /**
   * Overridden to add the load queue information to the server info.
   *
   * {@inheritDoc}
   */
  @Override
  protected SzServerInfo newServerInfo(SzApiProvider  provider,
                                       Long           activeConfigId)
  {
    SzPocProvider pocProvider = (SzPocProvider) provider;
    SzPocServerInfo serverInfo = (SzPocServerInfo)
        super.newServerInfo(provider, activeConfigId);
    serverInfo.setLoadQueueConfigured(pocProvider.hasLoadSink());
    return serverInfo;
  }
}
