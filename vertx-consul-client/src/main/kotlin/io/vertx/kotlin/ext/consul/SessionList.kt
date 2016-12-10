package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.SessionList

fun SessionList(
    index: Long? = null,
  list: List<io.vertx.ext.consul.Session>? = null): SessionList = io.vertx.ext.consul.SessionList().apply {

  if (index != null) {
    this.index = index
  }

  if (list != null) {
    this.list = list
  }

}

