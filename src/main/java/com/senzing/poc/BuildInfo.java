package com.senzing.poc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides build information for the POC server.
 */
public class BuildInfo {
  /**
   * The Maven build version of the POC Server.
   */
  public static final String MAVEN_VERSION;

  /**
   * The POC REST API specification version implemented by the API Server.
   */
  public static final String POC_REST_API_VERSION = "1.0.0";

  static {
    String resource = "/com/senzing/poc/build-info.properties";
    String version = "UNKNOWN";
    try (InputStream is = com.senzing.poc.BuildInfo.class.getResourceAsStream(resource))
    {
      Properties buildProps = new Properties();
      buildProps.load(is);
      version = buildProps.getProperty("Maven-Version");

    } catch (IOException e) {
      System.err.println("FAILED TO READ " + resource + " FILE");
      e.printStackTrace();

    } catch (Exception e) {
      e.printStackTrace();
      throw new ExceptionInInitializerError(e);

    } finally {
      MAVEN_VERSION = version;
    }
  }

  /**
   * Private default constructor.
   */
  private BuildInfo() {
    // do nothing
  }
}
