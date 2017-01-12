package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.DcCoordinates

fun DcCoordinates(
    datacenter: String? = null,
  servers: List<io.vertx.ext.consul.Coordinate>? = null): DcCoordinates = io.vertx.ext.consul.DcCoordinates().apply {

  if (datacenter != null) {
    this.datacenter = datacenter
  }

  if (servers != null) {
    this.servers = servers
  }

}

