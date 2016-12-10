package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.Session

fun Session(
    checks: List<String>? = null,
  createIndex: Long? = null,
  id: String? = null,
  index: Long? = null,
  lockDelay: Long? = null,
  node: String? = null): Session = io.vertx.ext.consul.Session().apply {

  if (checks != null) {
    this.checks = checks
  }

  if (createIndex != null) {
    this.createIndex = createIndex
  }

  if (id != null) {
    this.id = id
  }

  if (index != null) {
    this.index = index
  }

  if (lockDelay != null) {
    this.lockDelay = lockDelay
  }

  if (node != null) {
    this.node = node
  }

}

