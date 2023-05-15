package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class CloneAclToken {
  private static final String DESCRIPTION_KEY = "Description";
  private static final String NAMESPACE_KEY = "Namespace";
  /**
   * Description for the cloned token
   */
  private String description;
  private String namespace;

  public CloneAclToken() {
  }

  public CloneAclToken(JsonObject json){
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
  public CloneAclToken setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Sets an optional namespace
   * Default value is ns URL query parameter or in the X-Consul-Namespace header, or 'default' namespace.
   *
   * @param namespace
   */
  public CloneAclToken setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }
}
