package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.Check
import io.vertx.ext.consul.CheckStatus

fun Check(
    id: String? = null,
  name: String? = null,
  nodeName: String? = null,
  notes: String? = null,
  output: String? = null,
  serviceId: String? = null,
  serviceName: String? = null,
  status: CheckStatus? = null): Check = io.vertx.ext.consul.Check().apply {

  if (id != null) {
    this.id = id
  }

  if (name != null) {
    this.name = name
  }

  if (nodeName != null) {
    this.nodeName = nodeName
  }

  if (notes != null) {
    this.notes = notes
  }

  if (output != null) {
    this.output = output
  }

  if (serviceId != null) {
    this.serviceId = serviceId
  }

  if (serviceName != null) {
    this.serviceName = serviceName
  }

  if (status != null) {
    this.status = status
  }

}

