package io.vertx.ext.consul.v1.kv;

import io.vertx.codegen.annotations.VertxGen;

import java.util.List;

@VertxGen
public interface QueryMetaWithList<ListType> {

  /**
   * @return query response meta
   */
  QueryMeta queryMeta();

  /**
   * @return attached object
   */
  List<ListType> list();

}
