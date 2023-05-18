package io.vertx.ext.consul.token;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

@DataObject
public class AclToken {
  private static final String ACCESSOR_ID_KEY = "AccessorID";
  private static final String SECRET_ID_KEY = "SecretID";
  private static final String DESCRIPTION_KEY = "Description";
  private static final String POLICIES_KEY = "Policies";
  private static final String SERVICE_IDENTITIES_KEY = "ServiceIdentities";
  private static final String NODE_IDENTITIES_KEY = "NodeIdentities";
  private static final String LOCAL_KEY = "Local";
  private static final String EXPIRATION_TIME_KEY = "ExpirationTime";
  private static final String NAMESPACE_KEY = "Namespace";

  /**
   * Specifies a UUID to use as the token's Accessor ID
   */
  private String accessorId;

  /**
   * Specifies a UUID to use as the token's Secret ID
   */
  private String secretId;

  /**
   * Free form human-readable description of the token
   */
  private String description;

  /**
   * The list of policies that should be applied to the token
   */
  private List<PolicyLink> policies;

  /**
   * Indicates it is a replicated globally token(false) or local token in the current datacenter(true)
   */
  private Boolean local = false;

  /**
   * The list of service identities that should be applied to the token
   */
  private List<ServiceTokenApplyingOptions> serviceIdentities;

  /**
   * The list of node identities that should be applied to the token
   */
  private List<NodeTokenApplyingOptions> nodeIdentities;

  /**
   * Represents the point after which a token should be considered revoked and is eligible for destruction
   */
  private String expirationTime;

  /**
   * Specifies the namespace to create the policy
   */
  private String namespace;

  public AclToken() {
  }

  public AclToken(JsonObject json) {
    this.accessorId = json.getString(ACCESSOR_ID_KEY);
    this.secretId = json.getString(SECRET_ID_KEY);
    this.policies = new ArrayList<>();
    JsonArray policiesArray = json.getJsonArray(POLICIES_KEY);
    if (policiesArray != null) {
      for (int i = 0; i < policiesArray.size(); i++) {
        policies.add(new PolicyLink(policiesArray.getJsonObject(i)));
      }
    }
    this.local = json.getBoolean(LOCAL_KEY);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    if (description != null) {
      json.put(DESCRIPTION_KEY, description);
    }
    if (policies != null && !policies.isEmpty()) {
      JsonArray array = new JsonArray();
      for (PolicyLink p : policies) {
        array.add(p.toJson());
      }
      json.put(POLICIES_KEY, array);
    }
    if (serviceIdentities != null && !serviceIdentities.isEmpty()) {
      JsonArray array = new JsonArray();
      for (ServiceTokenApplyingOptions s : serviceIdentities) {
        array.add(s.toJson());
      }
      json.put(SERVICE_IDENTITIES_KEY, array);
    }
    if (nodeIdentities != null && !nodeIdentities.isEmpty()) {
      JsonArray array = new JsonArray();
      for (NodeTokenApplyingOptions n : nodeIdentities) {
        array.add(n.toJson());
      }
      json.put(NODE_IDENTITIES_KEY, array);
    }
    json.put(LOCAL_KEY, local);
    if (expirationTime != null) {
      json.put(EXPIRATION_TIME_KEY, expirationTime);
    }
    if (namespace != null) {
      json.put(NAMESPACE_KEY, namespace);
    }
    return json;
  }

  /**
   * Returns accessorId
   *
   * @see #accessorId
   */
  public String getAccessorId() {
    return accessorId;
  }

  /**
   * Returns secretId
   *
   * @see #secretId
   */
  public String getSecretId() {
    return secretId;
  }

  public String getDescription() {
    return description;
  }

  public List<PolicyLink> getPolicies() {
    return policies;
  }

  public Boolean getLocal() {
    return local;
  }

  /**
   * Sets an optional free-form description that is human-readable
   *
   * @param description
   * @see #description
   */
  public AclToken setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Sets policies
   *
   * @param policies
   * @see PolicyLink
   */
  public AclToken setPolicies(List<PolicyLink> policies) {
    this.policies = policies;
    return this;
  }

  /**
   * Adds a policy. Like {@link #setPolicies(List)}
   *
   * @param policyLink
   */
  public AclToken addPolicy(PolicyLink policyLink) {
    if (this.policies == null) {
      this.policies = new ArrayList<>();
    }
    this.policies.add(policyLink);
    return this;
  }

  /**
   * Indicates that it is a local token
   *
   * @see AclToken#local
   */
  public AclToken local() {
    this.local = true;
    return this;
  }

  /**
   * Sets the expiration time. Optional, by default NO expiration.
   *
   * @param expirationTime must be between 1 minute and 24 hours in the future
   * @see AclToken#expirationTime
   */
  public AclToken setExpirationTime(String expirationTime) {
    this.expirationTime = expirationTime;
    return this;
  }

  /**
   * Sets an optional namespace.
   * Default value is ns URL query parameter or in the X-Consul-Namespace header, or 'default' namespace.
   *
   * @param namespace
   * @see #namespace
   */
  public AclToken setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets a list of nodes
   *
   * @see NodeTokenApplyingOptions
   */
  public AclToken setNodeIdentities(List<NodeTokenApplyingOptions> nodeIdentities) {
    this.nodeIdentities = nodeIdentities;
    return this;
  }

  /**
   * Sets a list of services
   *
   * @see ServiceTokenApplyingOptions
   */
  public AclToken setServiceIdentities(List<ServiceTokenApplyingOptions> serviceIdentities) {
    this.serviceIdentities = serviceIdentities;
    return this;
  }
}
