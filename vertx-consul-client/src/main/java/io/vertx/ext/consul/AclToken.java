/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Holds properties of Acl token
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class AclToken {

  private static final String ID_KEY = "ID";
  private static final String NAME_KEY = "Name";
  private static final String TYPE_KEY = "Type";
  private static final String RULES_KEY = "Rules";

  private String id;
  private String name;
  private AclTokenType type;
  private String rules;

  /**
   * Default constructor
   */
  public AclToken() {
  }

  /**
   * Copy constructor
   *
   * @param token the one to copy
   */
  public AclToken(AclToken token) {
    this.id = token.id;
    this.name = token.name;
    this.type = token.type;
    this.rules = token.rules;
  }

  /**
   * Constructor from JSON
   *
   * @param token the JSON
   */
  public AclToken(JsonObject token) {
    this.id = token.getString(ID_KEY);
    this.name = token.getString(NAME_KEY);
    this.type = AclTokenType.of(token.getString(TYPE_KEY));
    this.rules = token.getString(RULES_KEY);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (id != null) {
      jsonObject.put(ID_KEY, id);
    }
    if (name != null) {
      jsonObject.put(NAME_KEY, name);
    }
    if (type != null) {
      jsonObject.put(TYPE_KEY, type.key);
    }
    if (rules != null) {
      jsonObject.put(RULES_KEY, rules);
    }
    return jsonObject;
  }

  /**
   * Get ID of token
   *
   * @return ID of token
   */
  public String getId() {
    return id;
  }

  /**
   * Set ID of token
   *
   * @param id ID of token
   * @return reference to this, for fluency
   */
  public AclToken setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get name of token
   *
   * @return name of token
   */
  public String getName() {
    return name;
  }

  /**
   * Set name of token
   *
   * @param name name of token
   * @return reference to this, for fluency
   */
  public AclToken setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get type of token
   *
   * @return type of token
   */
  public AclTokenType getType() {
    return type;
  }

  /**
   * Set type of token
   *
   * @param type type of token
   * @return reference to this, for fluency
   */
  public AclToken setType(AclTokenType type) {
    this.type = type;
    return this;
  }

  /**
   * Get rules for token
   *
   * @return rules for token
   */
  public String getRules() {
    return rules;
  }

  /**
   * Set rules for token
   *
   * @param rules rules for token
   * @return reference to this, for fluency
   */
  public AclToken setRules(String rules) {
    this.rules = rules;
    return this;
  }
}
