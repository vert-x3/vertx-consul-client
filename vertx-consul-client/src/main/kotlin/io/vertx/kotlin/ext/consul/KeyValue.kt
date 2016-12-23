package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.KeyValue

fun KeyValue(
    createIndex: Long? = null,
  flags: Long? = null,
  key: String? = null,
  lockIndex: Long? = null,
  modifyIndex: Long? = null,
  session: String? = null,
  value: String? = null): KeyValue = io.vertx.ext.consul.KeyValue().apply {

  if (createIndex != null) {
    this.createIndex = createIndex
  }

  if (flags != null) {
    this.flags = flags
  }

  if (key != null) {
    this.key = key
  }

  if (lockIndex != null) {
    this.lockIndex = lockIndex
  }

  if (modifyIndex != null) {
    this.modifyIndex = modifyIndex
  }

  if (session != null) {
    this.session = session
  }

  if (value != null) {
    this.value = value
  }

}

