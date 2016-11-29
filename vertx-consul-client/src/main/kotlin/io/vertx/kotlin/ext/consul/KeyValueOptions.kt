package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.KeyValueOptions

fun KeyValueOptions(
    acquireSession: String? = null,
  casIndex: Long? = null,
  flags: Long? = null,
  releaseSession: String? = null): KeyValueOptions = io.vertx.ext.consul.KeyValueOptions().apply {

  if (acquireSession != null) {
    this.acquireSession = acquireSession
  }

  if (casIndex != null) {
    this.casIndex = casIndex
  }

  if (flags != null) {
    this.flags = flags
  }

  if (releaseSession != null) {
    this.releaseSession = releaseSession
  }

}

