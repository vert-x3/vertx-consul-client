package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.SessionOptions
import io.vertx.ext.consul.SessionBehavior

fun SessionOptions(
    behavior: SessionBehavior? = null,
  checks: List<String>? = null,
  lockDelay: Long? = null,
  name: String? = null,
  node: String? = null,
  ttl: Long? = null): SessionOptions = io.vertx.ext.consul.SessionOptions().apply {

  if (behavior != null) {
    this.behavior = behavior
  }

  if (checks != null) {
    this.checks = checks
  }

  if (lockDelay != null) {
    this.lockDelay = lockDelay
  }

  if (name != null) {
    this.name = name
  }

  if (node != null) {
    this.node = node
  }

  if (ttl != null) {
    this.ttl = ttl
  }

}

