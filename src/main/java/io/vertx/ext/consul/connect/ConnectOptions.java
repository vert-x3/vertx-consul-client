package io.vertx.ext.consul.connect;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class ConnectOptions {
  private static final String SIDECAR = "SidecarService";

  private SidecarServiceOptions sidecarService;

  /**
   * Default constructor
   */
  public ConnectOptions() {
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public ConnectOptions(JsonObject options) {
    this.sidecarService = new SidecarServiceOptions(options.getJsonObject(SIDECAR));
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (sidecarService != null) {
      jsonObject.put(SIDECAR, sidecarService.toJson());
    }
    return jsonObject;
  }

  public SidecarServiceOptions getSidecarService() {
    return sidecarService;
  }

  public ConnectOptions setSidecarService(SidecarServiceOptions sidecarService) {
    this.sidecarService = sidecarService;
    return this;
  }
}
