package io.vertx.ext.consul.v1.kv;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @param <WithType> type of
 */
@VertxGen
public interface QueryMetaWith<WithType> {

  /**
   * @return query response meta
   */
  QueryMeta queryMeta();

  /**
   * @return attached object
   */
  WithType with();

}
