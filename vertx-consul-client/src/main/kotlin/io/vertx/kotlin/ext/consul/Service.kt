package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.Service

fun Service(
    address: String? = null,
  id: String? = null,
  name: String? = null,
  node: String? = null,
  nodeAddress: String? = null,
  port: Int? = null,
  tags: List<String>? = null): Service = io.vertx.ext.consul.Service().apply {

  if (address != null) {
    this.address = address
  }

  if (id != null) {
    this.id = id
  }

  if (name != null) {
    this.name = name
  }

  if (node != null) {
    this.node = node
  }

  if (nodeAddress != null) {
    this.nodeAddress = nodeAddress
  }

  if (port != null) {
    this.port = port
  }

  if (tags != null) {
    this.tags = tags
  }

}

