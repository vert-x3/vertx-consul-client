package io.vertx.ext.consul.connect;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

@DataObject
public class ProxyOptions {
  private static final String CONFIG = "Config";
  private static final String UPSTREAMS = "Upstreams";
  private static final String EXPOSE = "Expose";

  private JsonObject config;
  private List<UpstreamOptions> upstreams;
  private ExposeOptions expose;

  /**
   * Default constructor
   */
  public ProxyOptions() {
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public ProxyOptions(JsonObject options) {
    this.config = options.getJsonObject(CONFIG);
    this.upstreams = options.getJsonArray(UPSTREAMS).stream()
      .map(o -> new UpstreamOptions((JsonObject) o))
      .collect(Collectors.toList());
    this.expose = new ExposeOptions(options.getJsonObject(EXPOSE));
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (upstreams != null) {
      jsonObject.put(UPSTREAMS, upstreams.stream().map(UpstreamOptions::toJson).collect(Collectors.toList()));
    }
    if (config != null) {
      jsonObject.put(CONFIG, config);
    }
    if (expose != null) {
      jsonObject.put(EXPOSE, expose.toJson());
    }
    return jsonObject;
  }

  public JsonObject getConfig() {
    return config;
  }

  public ProxyOptions setConfig(JsonObject config) {
    this.config = config;
    return this;
  }

  public List<UpstreamOptions> getUpstreams() {
    return upstreams;
  }

  public ProxyOptions setUpstreams(List<UpstreamOptions> upstreams) {
    this.upstreams = upstreams;
    return this;
  }

  public ExposeOptions getExpose() {
    return expose;
  }

  public ProxyOptions setExpose(ExposeOptions expose) {
    this.expose = expose;
    return this;
  }
}
