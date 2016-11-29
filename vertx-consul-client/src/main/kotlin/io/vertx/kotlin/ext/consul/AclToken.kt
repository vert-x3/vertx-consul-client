package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.AclToken
import io.vertx.ext.consul.AclTokenType

fun AclToken(
    id: String? = null,
  name: String? = null,
  rules: String? = null,
  type: AclTokenType? = null): AclToken = io.vertx.ext.consul.AclToken().apply {

  if (id != null) {
    this.id = id
  }

  if (name != null) {
    this.name = name
  }

  if (rules != null) {
    this.rules = rules
  }

  if (type != null) {
    this.type = type
  }

}

