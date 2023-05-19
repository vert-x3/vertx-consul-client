package io.vertx.ext.consul.token;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

@DataObject
public class NodeTokenApplyingOptions extends TokenApplyingOptions {
  private static final String NODE_NAME_KEY = "NodeName";

  public NodeTokenApplyingOptions(JsonObject json) {
    this.name = json.getString(NODE_NAME_KEY);
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
      json.put(NODE_NAME_KEY, name);
    }
    return json;
  }
}
