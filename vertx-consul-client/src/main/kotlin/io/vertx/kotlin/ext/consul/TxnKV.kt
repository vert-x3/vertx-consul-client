package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.TxnKV
import io.vertx.ext.consul.TxnKVType

/**
 * A function providing a DSL for building [io.vertx.ext.consul.TxnKV] objects.
 *
 * Holds operation to apply to the key/value store inside a transaction
 *
 * @param flags  Set the flags attached to this entry. Clients can choose to use this however makes sense for their application.
 * @param index  Set the index used for locking, unlocking, and check-and-set operations.
 * @param key  Set the key
 * @param session  Set the session used for locking, unlocking, and check-and-set operations.
 * @param type  Set the type of operation to perform
 * @param value  Set the value
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.ext.consul.TxnKV original] using Vert.x codegen.
 */
fun TxnKV(
  flags: Long? = null,
  index: Long? = null,
  key: String? = null,
  session: String? = null,
  type: TxnKVType? = null,
  value: String? = null): TxnKV = io.vertx.ext.consul.TxnKV().apply {

  if (flags != null) {
    this.setFlags(flags)
  }
  if (index != null) {
    this.setIndex(index)
  }
  if (key != null) {
    this.setKey(key)
  }
  if (session != null) {
    this.setSession(session)
  }
  if (type != null) {
    this.setType(type)
  }
  if (value != null) {
    this.setValue(value)
  }
}

