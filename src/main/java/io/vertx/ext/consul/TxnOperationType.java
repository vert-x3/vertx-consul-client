package io.vertx.ext.consul;

import io.vertx.codegen.annotations.VertxGen;

/**
 * Represents the type of operation in a transaction. The available operation types are KV and Service
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/agent/http/kv.html#txn">/v1/txn</a> endpoint
 */
@VertxGen
public enum TxnOperationType {
  KV,
  SERVICE
}
