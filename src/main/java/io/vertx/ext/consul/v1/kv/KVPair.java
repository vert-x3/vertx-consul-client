package io.vertx.ext.consul.v1.kv;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.buffer.Buffer;

@VertxGen
public interface KVPair {

  // Key is the name of the key. It is also part of the URL path when accessed
  // via the API.
  String key();

  // CreateIndex holds the index corresponding the creation of this KVPair. This
  // is a read-only field.
  long createIndex();

  // ModifyIndex is used for the Check-And-Set operations and can also be fed
  // back into the WaitIndex of the QueryOptions in order to perform blocking
  // queries.
  long modifyIndex();

  // LockIndex holds the index corresponding to a lock on this key, if any. This
  // is a read-only field.
  long lockIndex();

  // Flags are any user-defined flags on the key. It is up to the implementer
  // to check these values, since Consul does not treat them specially.
  long flags();

  // Value is the value for the key. This can be any value, but it will be
  // base64 encoded upon transport.
  Buffer value();

  // Session is a string representing the ID of the session. Any other
  // interactions with this key over the same session must specify the same
  // session ID.
  String session();
}
