package io.vertx.ext.consul.token;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class CloneAclTokenOptions {
  private static final String DESCRIPTION_KEY = "Description";
  private static final String NAMESPACE_KEY = "Namespace";
  /**
   * Description for the cloned token
   */
  private String description;
  private String namespace;

  public CloneAclTokenOptions() {
  }

  public CloneAclTokenOptions(JsonObject json){
    this.description = json.getString(DESCRIPTION_KEY);
    this.namespace = json.getString(NAMESPACE_KEY);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    if (description != null) {
      json.put(DESCRIPTION_KEY, description);
    }
    if (namespace != null) {
      json.put(NAMESPACE_KEY, namespace);
    }
    return json;
  }

  /**
   * Sets an optional description
   *
   * @param description Free form human-readable description
   */
  public CloneAclTokenOptions setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Sets an optional namespace
   * Default value is ns URL query parameter or in the X-Consul-Namespace header, or 'default' namespace.
   *
   * @param namespace
   */
  public CloneAclTokenOptions setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }
}
