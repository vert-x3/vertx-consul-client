package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class ServiceIdentity extends Identity {
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
