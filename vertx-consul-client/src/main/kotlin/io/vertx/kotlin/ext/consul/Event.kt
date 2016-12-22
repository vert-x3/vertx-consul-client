package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.Event

fun Event(
    id: String? = null,
  lTime: Int? = null,
  name: String? = null,
  node: String? = null,
  payload: String? = null,
  service: String? = null,
  tag: String? = null,
  version: Int? = null): Event = io.vertx.ext.consul.Event().apply {

  if (id != null) {
    this.id = id
  }

  if (lTime != null) {
    this.lTime = lTime
  }

  if (name != null) {
    this.name = name
  }

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

  if (version != null) {
    this.version = version
  }

}

