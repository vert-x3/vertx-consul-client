package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.KeyValueList

fun KeyValueList(
    index: Long? = null,
  list: List<io.vertx.ext.consul.KeyValue>? = null): KeyValueList = io.vertx.ext.consul.KeyValueList().apply {

  if (index != null) {
    this.index = index
  }

  if (list != null) {
    this.list = list
  }

}

