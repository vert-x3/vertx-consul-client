package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ServiceEntry

fun ServiceEntry(
    checks: List<io.vertx.ext.consul.Check>? = null,
  node: io.vertx.ext.consul.Node? = null,
  service: io.vertx.ext.consul.Service? = null): ServiceEntry = io.vertx.ext.consul.ServiceEntry().apply {

  if (checks != null) {
    this.checks = checks
  }

  if (node != null) {
    this.node = node
  }

  if (service != null) {
    this.service = service
  }

}

