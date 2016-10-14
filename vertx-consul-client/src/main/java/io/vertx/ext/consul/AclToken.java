package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class AclToken {

  public enum Type {
    CLIENT("client"),
    MANAGEMENT("management");

    public final String name;

    public static Type of(String name) {
      if (name == null) {
        return null;
      } else {
        return CLIENT.name.equals(name) ? CLIENT : MANAGEMENT;
      }
    }

    Type(String name) {
      this.name = name;
    }
  }

  private static final String ID_KEY = "ID";
  private static final String NAME_KEY = "Name";
  private static final String TYPE_KEY = "Type";
  private static final String RULES_KEY = "Rules";

  private String id;
  private String name;
  private Type type;
  private String rules;

  public AclToken() {
  }

  public AclToken(JsonObject json) {
    this.id = json.getString(ID_KEY);
    this.name = json.getString(NAME_KEY);
    this.type = Type.of(json.getString(TYPE_KEY));
    this.rules = json.getString(RULES_KEY);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  public String getRules() {
    return rules;
  }

  public AclToken setId(String id) {
    this.id = id;
    return this;
  }

  public AclToken setName(String name) {
    this.name = name;
    return this;
  }

  public AclToken setRules(String rules) {
    this.rules = rules;
    return this;
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (id != null) {
      jsonObject.put(ID_KEY, id);
    }
    if (name != null) {
      jsonObject.put(NAME_KEY, name);
    }
    if (type != null) {
      jsonObject.put(TYPE_KEY, type);
    }
    if (rules != null) {
      jsonObject.put(RULES_KEY, rules);
    }
    return jsonObject;
  }
}
