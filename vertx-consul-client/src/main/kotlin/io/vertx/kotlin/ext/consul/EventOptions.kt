package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.EventOptions

fun EventOptions(
    node: String? = null,
  payload: String? = null,
  service: String? = null,
  tag: String? = null): EventOptions = io.vertx.ext.consul.EventOptions().apply {

  if (node != null) {
    this.node = node
  }

  if (payload != null) {
    this.payload = payload
  }

  if (service != null) {
    this.service = service
  }

  if (tag != null) {
    this.tag = tag
  }

}

