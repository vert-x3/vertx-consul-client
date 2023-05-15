package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * It is an object with an "ID" and/or "Name" field to specify a policy
 */
@DataObject
public class PolicyLink {
  private static final String ID_KEY = "ID";
  private static final String NAME_KEY = "Name";
  /**
   * Policy ID
   */
  private String id;
  /**
   * Policy name
   */
  private String name;

  public PolicyLink() {
  }

  public PolicyLink(JsonObject json) {
    this.id = json.getString(ID_KEY);
    this.name = json.getString(NAME_KEY);
  }

  JsonObject toJson() {
    JsonObject json = new JsonObject();
    if (id != null) {
      json.put(ID_KEY, id);
    }
    if (name != null) {
      json.put(NAME_KEY, name);
    }
    return json;
  }

  /**
   * Sets a policy id
   *
   * @param id uuid
   */
  public PolicyLink setId(String id) {
    PolicyLink.this.id = id;
    return this;
  }

  /**
   * Sets a policy name
   *
   * @param name
   */
  public PolicyLink setName(String name) {
    PolicyLink.this.name = name;
    return this;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
