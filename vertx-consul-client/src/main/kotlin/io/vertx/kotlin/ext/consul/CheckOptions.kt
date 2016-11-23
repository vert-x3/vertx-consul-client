package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.CheckOptions

fun CheckOptions(
    http: String? = null,
  id: String? = null,
  interval: String? = null,
  name: String? = null,
  note: String? = null,
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

  if (note != null) {
    this.note = note
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

