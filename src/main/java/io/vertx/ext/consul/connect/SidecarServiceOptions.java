package io.vertx.ext.consul.connect;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

@DataObject
public class SidecarServiceOptions {
  private static final String PROXY = "Proxy";
  private static final String PORT = "Port";
  private static final String CHECKS = "Checks";

  private ProxyOptions proxy;
  private int port;
  private List<JsonObject> checks;

  /**
   * Default constructor
   */
  public SidecarServiceOptions() {
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public SidecarServiceOptions(JsonObject options) {
    this.proxy = new ProxyOptions(options.getJsonObject(PROXY));
    this.port = options.getInteger(PORT);
    this.checks = options.getJsonArray(CHECKS).stream()
      .map(o -> (JsonObject) o)
      .collect(Collectors.toList());
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put(PORT, port);
    if (proxy != null) {
      jsonObject.put(PROXY, proxy.toJson());
    }
    if (checks != null) {
      jsonObject.put(CHECKS, checks);
    }
    return jsonObject;
  }

  public int getPort() {
    return port;
  }

  public SidecarServiceOptions setPort(int port) {
    this.port = port;
    return this;
  }

  public ProxyOptions getProxy() {
    return proxy;
  }

  public SidecarServiceOptions setProxy(ProxyOptions proxy) {
    this.proxy = proxy;
    return this;
  }

  public List<JsonObject> getChecks() {
    return checks;
  }

  public void setChecks(List<JsonObject> checks) {
    this.checks = checks;
  }
}
