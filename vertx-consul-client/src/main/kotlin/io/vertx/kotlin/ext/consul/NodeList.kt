package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.NodeList

fun NodeList(
    index: Long? = null,
  list: List<io.vertx.ext.consul.Node>? = null): NodeList = io.vertx.ext.consul.NodeList().apply {

  if (index != null) {
    this.index = index
  }

  if (list != null) {
    this.list = list
  }

}

