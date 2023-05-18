package io.vertx.ext.consul.token;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public abstract class TokenApplyingOptions {
  private static final String DATACENTERS_KEY = "Datacenters";

  /**
   * The name of the service/node
   */
  protected String name;
  /**
   * Specifies the datacenters the policy is valid within
   */
  protected List<String> datacenters;

  public String getName() {
    return name;
  }

  /**
   * Sets a name
   *
   * @param name - must be no longer than 256 characters, must start and end with a lowercase alphanumeric character,
   *             and can only contain lowercase alphanumeric characters as well as '-' and '_'.
   */
  public TokenApplyingOptions setName(String name) {
    this.name = name;
    return this;
  }

  public List<String> getDatacenters() {
    return datacenters;
  }

  /**
   * Sets an optional datacenters. By default, the policy is valid in all datacenters
   *
   * @param datacenters list of datacenters
   * @see #datacenters
   */
  public TokenApplyingOptions setDatacenters(List<String> datacenters) {
    this.datacenters = datacenters;
    return this;
  }

  /**
   * Adds a datacenter, like {@link #setDatacenters(List)}
   *
   * @see #datacenters
   */
  public TokenApplyingOptions addDatacenter(String datacenter) {
    if (datacenters == null) {
      datacenters = new ArrayList<>();
    }
    datacenters.add(datacenter);
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    if (datacenters != null && !datacenters.isEmpty()) {
      JsonArray array = new JsonArray(datacenters);
      json.put(DATACENTERS_KEY, array);
    }
    return json;
  }
}
