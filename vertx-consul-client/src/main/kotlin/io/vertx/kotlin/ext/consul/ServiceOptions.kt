package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ServiceOptions

fun ServiceOptions(
    address: String? = null,
  checkOptions: io.vertx.ext.consul.CheckOptions? = null,
  id: String? = null,
  name: String? = null,
  port: Int? = null,
  tags: List<String>? = null): ServiceOptions = io.vertx.ext.consul.ServiceOptions().apply {

  if (address != null) {
    this.address = address
  }

  if (checkOptions != null) {
    this.checkOptions = checkOptions
  }

  if (id != null) {
    this.id = id
  }

  if (name != null) {
    this.name = name
  }

  if (port != null) {
    this.port = port
  }

  if (tags != null) {
    this.tags = tags
  }

}

