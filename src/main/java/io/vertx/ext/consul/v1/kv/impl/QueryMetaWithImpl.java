package io.vertx.ext.consul.v1.kv.impl;

import io.vertx.ext.consul.v1.kv.QueryMeta;
import io.vertx.ext.consul.v1.kv.QueryMetaWith;

public class QueryMetaWithImpl<WithType> implements QueryMetaWith<WithType> {

  private final WithType withType;
  private final QueryMeta meta;

  public QueryMetaWithImpl(QueryMeta meta, WithType withType) {
    this.withType = withType;
    this.meta = meta;
  }

  @Override
  public QueryMeta queryMeta() {
    return meta;
  }

  @Override
  public WithType with() {
    return withType;
  }
}
