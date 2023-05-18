package io.vertx.ext.consul.token;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class NodeTokenApplyingOptions extends TokenApplyingOptions {
  private static final String NODE_NAME_KEY = "NodeName";

  @Override
  public JsonObject toJson() {
    JsonObject json =  super.toJson();
    if (name != null) {
      json.put(NODE_NAME_KEY, name);
    }
    return json;
  }
}
