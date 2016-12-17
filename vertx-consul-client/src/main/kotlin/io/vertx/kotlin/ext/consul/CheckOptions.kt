package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.CheckOptions

fun CheckOptions(
    http: String? = null,
  id: String? = null,
  interval: String? = null,
  name: String? = null,
  notes: String? = null,
  script: String? = null,
  tcp: String? = null,
  ttl: String? = null): CheckOptions = io.vertx.ext.consul.CheckOptions().apply {

  if (http != null) {
    this.http = http
  }

  if (id != null) {
    this.id = id
  }

  if (interval != null) {
    this.interval = interval
  }

  if (name != null) {
    this.name = name
  }

  if (notes != null) {
    this.notes = notes
  }

  if (script != null) {
    this.script = script
  }

  if (tcp != null) {
    this.tcp = tcp
  }

  if (ttl != null) {
    this.ttl = ttl
  }

}

