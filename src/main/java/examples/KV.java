/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package examples;

import io.vertx.ext.consul.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class KV {

  public void put(ConsulClient consulClient) {

    consulClient.putValue("key", "value", res -> {
      if (res.succeeded()) {
        String opResult = res.result() ? "success" : "fail";
        System.out.println("result of the operation: " + opResult);
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void putWithOptions(ConsulClient consulClient, long modifyIndex) {

    KeyValueOptions opts = new KeyValueOptions()
      .setFlags(42)
      .setCasIndex(modifyIndex)
      .setAcquireSession("acquireSessionID")
      .setReleaseSession("releaseSessionID");

    consulClient.putValueWithOptions("key", "value", opts, res -> {
      if (res.succeeded()) {
        String opResult = res.result() ? "success" : "fail";
        System.out.println("result of the operation: " + opResult);
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void getValue(ConsulClient consulClient) {

    consulClient.getValue("key", res -> {
      if (res.succeeded()) {
        System.out.println("retrieved value: " + res.result().getValue());
        System.out.println("modify index: " + res.result().getModifyIndex());
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void getValues(ConsulClient consulClient) {

    consulClient.getValues("prefix", res -> {
      if (res.succeeded()) {
        System.out.println("modify index: " + res.result().getIndex());
        for (KeyValue kv : res.result().getList()) {
          System.out.println("retrieved value: " + kv.getValue());
        }
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void deleteValue(ConsulClient consulClient) {

    consulClient.deleteValue("key", res -> {
      if (res.succeeded()) {
        System.out.println("complete");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void deleteValues(ConsulClient consulClient) {

    consulClient.deleteValues("prefix", res -> {
      if (res.succeeded()) {
        System.out.println("complete");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void blocking(ConsulClient consulClient, long modifyIndex) {

    BlockingQueryOptions opts = new BlockingQueryOptions()
      .setIndex(modifyIndex)
      .setWait("1m");

    consulClient.getValueWithOptions("key", opts, res -> {
      if (res.succeeded()) {
        System.out.println("retrieved value: " + res.result().getValue());
        System.out.println("new modify index: " + res.result().getModifyIndex());
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void transaction(ConsulClient consulClient) {

    TxnRequest request = new TxnRequest()
      .addOperation(new TxnKVOperation().setKey("key1").setValue("value1").setType(TxnKVVerb.SET))
      .addOperation(new TxnKVOperation().setKey("key2").setValue("value2").setType(TxnKVVerb.SET));

    consulClient.transaction(request, res -> {
      if (res.succeeded()) {
        System.out.println("succeeded results: " + res.result().getResults().size());
        System.out.println("errors: " + res.result().getErrors().size());
      } else {
        res.cause().printStackTrace();
      }
    });
  }
}
