package io.vertx.ext.consul.policy;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds properties of Acl policies
 *
 * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/policies#parameters">Create a policy properties</a>
 */
@DataObject
public class AclPolicy {

  private static final String ID_KEY = "ID";
  private static final String NAME_KEY = "Name";
  private static final String DESCRIPTION_KEY = "Description";
  private static final String RULES_KEY = "Rules";
  private static final String DATACENTERS_KEY = "Datacenters";
  private static final String NAMESPACE_KEY = "Namespace";

  /**
   * Specifies the UUID of the ACL policy to read
   */
  private String id;

  /**
   * Specifies a name for the ACL policy
   */
  private String name;
  /**
   * Description of the policy
   */
  private String description;
  /**
   * Specifies rules for the ACL policy
   */
  private String rules;
  /**
   * Specifies the datacenters the policy is valid within
   */
  private List<String> datacenters;

  /**
   * Specifies the namespace to create the policy
   */
  private String namespace;

  public AclPolicy() {
  }

  public AclPolicy(JsonObject json) {
    this.id = json.getString(ID_KEY);
    this.name = json.getString(NAME_KEY);
    this.rules = json.getString(RULES_KEY);
    this.description = json.getString(DESCRIPTION_KEY);
    JsonArray datacenters = json.getJsonArray(DATACENTERS_KEY);
    if (datacenters != null && !datacenters.isEmpty()) {
      this.datacenters = new ArrayList<>();
      for (int i = 1; i < datacenters.size(); i++) {
        this.datacenters.add(datacenters.getString(i));
      }
    }
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getRules() {
    return rules;
  }

  public List<String> getDatacenters() {
    return datacenters;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getId() {
    return id;
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (name != null) {
      jsonObject.put(NAME_KEY, name);
    }
    if (description != null) {
      jsonObject.put(DESCRIPTION_KEY, description);
    }
    if (rules != null) {
      jsonObject.put(RULES_KEY, rules);
    }
    if (datacenters != null && !datacenters.isEmpty()) {
      JsonArray array = new JsonArray(datacenters);
      jsonObject.put(DATACENTERS_KEY, array);
    }
    if (namespace != null) {
      jsonObject.put(NAMESPACE_KEY, namespace);
    }
    return jsonObject;
  }

  /**
   * Sets a name. Must be unique
   *
   * @param name can contain alphanumeric characters, dashes -, and underscores _
   * @see #name
   */
  public AclPolicy setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets an optional free-form description that is human-readable.
   *
   * @param description
   * @see #description
   */
  public AclPolicy setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Sets a rules. The format of the Rules property is detailed in the ACL Rules documentation
   *
   * @param rules rules in specified format
   * @see #rules
   * @see <a href="https://developer.hashicorp.com/consul/docs/v1.11.x/security/acl/acl-rules">ACL Rules documentation</a>
   */
  public AclPolicy setRules(String rules) {
    this.rules = rules;
    return this;
  }

  /**
   * Sets an optional datacenters. By default, the policy is valid in all datacenters
   *
   * @param datacenters list of datacenters
   * @see #datacenters
   */
  public AclPolicy setDatacenters(List<String> datacenters) {
    this.datacenters = datacenters;
    return this;
  }

  /**
   * Adds a datacenter, like {@link #setDatacenters(List)}
   *
   * @see #datacenters
   */
  public AclPolicy addDatacenter(String datacenter) {
    if (this.datacenters == null) {
      this.datacenters = new ArrayList<>();
    }
    this.datacenters.add(datacenter);
    return this;
  }

  /**
   * Sets an optional namespace.
   * Default value is ns URL query parameter or in the X-Consul-Namespace header, or 'default' namespace.
   *
   * @param namespace
   * @see #namespace
   */
  public AclPolicy setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }
}
