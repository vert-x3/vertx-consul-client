package io.vertx.ext.consul.token;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

@DataObject
public class ServiceTokenApplyingOptions extends TokenApplyingOptions {
  private static final String SERVICE_NAME_KEY = "ServiceName";

  public ServiceTokenApplyingOptions(JsonObject json) {
    this.name = json.getString(SERVICE_NAME_KEY);
    JsonArray datacenters = json.getJsonArray(DATACENTERS_KEY);
    if (datacenters != null && !datacenters.isEmpty()) {
      this.datacenters = new ArrayList<>();
      for (int i = 1; i < datacenters.size(); i++) {
        this.datacenters.add(datacenters.getString(i));
      }
    }
  }

  @Override
  public JsonObject toJson() {
    JsonObject json = super.toJson();
    if (name != null) {
      json.put(SERVICE_NAME_KEY, name);
    }
    return json;
  }
}
