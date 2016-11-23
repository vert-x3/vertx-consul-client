package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.MaintenanceOptions

fun MaintenanceOptions(
    enable: Boolean? = null,
  id: String? = null,
  reason: String? = null): MaintenanceOptions = io.vertx.ext.consul.MaintenanceOptions().apply {

  if (enable != null) {
    this.isEnable = enable
  }

  if (id != null) {
    this.id = id
  }

  if (reason != null) {
    this.reason = reason
  }

}

