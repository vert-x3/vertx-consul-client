package io.vertx.ext.consul.connect;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class ExposePathOptions {
  private static final String PATH = "Path";
  private static final String PROTOCOL = "Protocol";
  private static final String LOC_PATH = "LocalPathPort";
  private static final String LIS_PORT = "ListenerPort";

  private String path;
  private String protocol;
  private Integer localPathPort;
  private Integer listenerPort;

  /**
   * Default constructor
   */
  public ExposePathOptions() {
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public ExposePathOptions(JsonObject options) {
    this.path = options.getString(PATH);
    this.protocol = options.getString(PROTOCOL);
    this.localPathPort = options.getInteger(LOC_PATH);
    this.listenerPort = options.getInteger(LIS_PORT);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put(PATH, path);
    jsonObject.put(PROTOCOL, protocol);
    jsonObject.put(LOC_PATH, localPathPort);
    jsonObject.put(LIS_PORT, listenerPort);
    return jsonObject;
  }

  public String getPath() {
    return path;
  }

  public ExposePathOptions setPath(String path) {
    this.path = path;
    return this;
  }

  public String getProtocol() {
    return protocol;
  }

  public ExposePathOptions setProtocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  public Integer getLocalPathPort() {
    return localPathPort;
  }

  public ExposePathOptions setLocalPathPort(Integer localPathPort) {
    this.localPathPort = localPathPort;
    return this;
  }

  public Integer getListenerPort() {
    return listenerPort;
  }

  public ExposePathOptions setListenerPort(Integer listenerPort) {
    this.listenerPort = listenerPort;
    return this;
  }
}
