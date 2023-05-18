package io.vertx.ext.consul.token;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class ServiceTokenApplyingOptions extends TokenApplyingOptions {
  private static final String SERVICE_NAME_KEY = "ServiceName";

  @Override
  public JsonObject toJson() {
    JsonObject json = super.toJson();
    if (name != null) {
      json.put(SERVICE_NAME_KEY, name);
    }
    return json;
  }
}
