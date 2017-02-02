package io.vertx.ext.consul;

/**
 * Represents the type of operation in a transaction. KV is the only available operation type,
 * though other types of operations may be added in future versions of Consul to be mixed with key/value operations
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/agent/http/kv.html#txn">/v1/txn</a> endpoint
 */
public enum TxnOperationType {
  KV
}
