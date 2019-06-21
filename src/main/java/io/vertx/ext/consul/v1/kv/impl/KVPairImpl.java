package io.vertx.ext.consul.v1.kv.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.consul.v1.kv.KVPair;

public class KVPairImpl implements KVPair {

  // Key is the name of the key. It is also part of the URL path when accessed
  // via the API.
  private String Key;

  // CreateIndex holds the index corresponding the creation of this KVPair. This
  // is a read-only field.
  private long CreateIndex;

  // ModifyIndex is used for the Check-And-Set operations and can also be fed
  // back into the WaitIndex of the QueryOptions in order to perform blocking
  // queries.
  private long ModifyIndex;

  // LockIndex holds the index corresponding to a lock on this key, if any. This
  // is a read-only field.
  private long LockIndex;

  // Flags are any user-defined flags on the key. It is up to the implementer
  // to check these values, since Consul does not treat them specially.
  private long Flags;

  // Value is the value for the key. This can be any value, but it will be
  // base64 encoded upon transport.
  private Buffer Value;

  // Session is a string representing the ID of the session. Any other
  // interactions with this key over the same session must specify the same
  // session ID.
  private String Session;

  @Override
  public String key() {
    return Key;
  }

  @Override
  public long createIndex() {
    return CreateIndex;
  }

  @Override
  public long modifyIndex() {
    return ModifyIndex;
  }

  @Override
  public long lockIndex() {
    return LockIndex;
  }

  @Override
  public long flags() {
    return Flags;
  }

  @Override
  public Buffer value() {
    return Value;
  }

  @Override
  public String session() {
    return Session;
  }

  public String getKey() {
    return Key;
  }

  public void setKey(String key) {
    Key = key;
  }

  public long getCreateIndex() {
    return CreateIndex;
  }

  public void setCreateIndex(long createIndex) {
    CreateIndex = createIndex;
  }

  public long getModifyIndex() {
    return ModifyIndex;
  }

  public void setModifyIndex(long modifyIndex) {
    ModifyIndex = modifyIndex;
  }

  public long getLockIndex() {
    return LockIndex;
  }

  public void setLockIndex(long lockIndex) {
    LockIndex = lockIndex;
  }

  public long getFlags() {
    return Flags;
  }

  public void setFlags(long flags) {
    Flags = flags;
  }

  public Buffer getValue() {
    return Value;
  }

  public void setValue(Buffer value) {
    Value = value;
  }

  public String getSession() {
    return Session;
  }

  public void setSession(String session) {
    Session = session;
  }
}
