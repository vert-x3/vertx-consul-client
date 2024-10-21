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
package io.vertx.ext.consul.tests.suite;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.Service;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.ext.consul.TxnError;
import io.vertx.ext.consul.TxnKVOperation;
import io.vertx.ext.consul.TxnKVVerb;
import io.vertx.ext.consul.TxnOperationType;
import io.vertx.ext.consul.TxnRequest;
import io.vertx.ext.consul.TxnResponse;
import io.vertx.ext.consul.TxnServiceOperation;
import io.vertx.ext.consul.TxnServiceVerb;
import io.vertx.ext.consul.tests.ConsulTestBase;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class Transactions extends ConsulTestBase {

  private final String SERVICE_NAME = "service1";

  @Test
  public void kvSet(TestContext tc) {
    TxnRequest request = new TxnRequest()
      .addOperation(new TxnKVOperation().setKey("foo/bar/t1").setValue("val1").setType(TxnKVVerb.SET))
      .addOperation(new TxnKVOperation().setKey("foo/bar/t2").setValue("val2").setType(TxnKVVerb.SET));
    writeClient.transaction(request).onComplete(tc.asyncAssertSuccess(response -> {
      tc.assertEquals(0, response.getErrorsSize());
      tc.assertEquals(2, response.getResultsSize());
      List<String> keys = getKeys(response);
      tc.assertTrue(keys.contains("foo/bar/t1"));
      tc.assertTrue(keys.contains("foo/bar/t2"));
      getEntries(tc, "foo/bar/t", entries -> {
        tc.assertTrue(entries.contains("foo/bar/t1/val1"));
        tc.assertTrue(entries.contains("foo/bar/t2/val2"));
        writeClient.deleteValues("foo/bar/t").onComplete(tc.asyncAssertSuccess());
      });
    }));
  }

  @Test
  public void kvCas(TestContext tc) {
    createKV(tc, "foo/bar1", "value1", idx1 -> {
      createKV(tc, "foo/bar2", "value2", idx2 -> {
        TxnRequest req1 = new TxnRequest()
          .addOperation(new TxnKVOperation().setKey("foo/bar1").setValue("newVal1").setIndex(idx1).setType(TxnKVVerb.CAS))
          .addOperation(new TxnKVOperation().setKey("foo/bar2").setValue("newVal2").setIndex(idx2 - 1).setType(TxnKVVerb.CAS));
        writeClient.transaction(req1).onComplete(tc.asyncAssertSuccess(resp1 -> {
          tc.assertEquals(1, resp1.getErrorsSize());
          tc.assertEquals(1, resp1.getErrors().get(0).getOpIndex());
          tc.assertEquals(0, resp1.getResultsSize());
          TxnRequest req2 = new TxnRequest()
            .addOperation(new TxnKVOperation().setKey("foo/bar1").setValue("newVal1").setIndex(idx1).setType(TxnKVVerb.CAS))
            .addOperation(new TxnKVOperation().setKey("foo/bar2").setValue("newVal2").setIndex(idx2).setType(TxnKVVerb.CAS));
          writeClient.transaction(req2).onComplete(tc.asyncAssertSuccess(resp2 -> {
            tc.assertEquals(0, resp2.getErrorsSize());
            tc.assertEquals(2, resp2.getResultsSize());
            List<String> keys = getKeys(resp2);
            tc.assertTrue(keys.contains("foo/bar1"));
            tc.assertTrue(keys.contains("foo/bar2"));
            getEntries(tc, "foo/bar", entries -> {
              tc.assertTrue(entries.contains("foo/bar1/newVal1"));
              tc.assertTrue(entries.contains("foo/bar2/newVal2"));
              writeClient.deleteValues("foo/bar").onComplete(tc.asyncAssertSuccess());
            });
          }));
        }));
      });
    });
  }

  @Test
  public void serviceSet(TestContext tc) {
    ServiceOptions serviceOptions1 = createServiceOptions("id1", SERVICE_NAME, "10.10.10.10", 8080);
    ServiceOptions serviceOptions2 = createServiceOptions("id2", SERVICE_NAME, "10.10.10.10", 8081);
    TxnRequest request = new TxnRequest()
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions1).setType(TxnServiceVerb.SET))
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions2).setType(TxnServiceVerb.SET));
    writeClient.transaction(request).onComplete(tc.asyncAssertSuccess(response -> {
      tc.assertEquals(0, response.getErrorsSize());
      tc.assertEquals(2, response.getResultsSize());
      List<Service> services = getServices(response);
      checkService(tc, services, serviceOptions1);
      checkService(tc, services, serviceOptions2);
      getRegistrations(tc, SERVICE_NAME, registrations -> {
        checkService(tc, registrations, serviceOptions1);
        checkService(tc, registrations, serviceOptions2);
        writeClient.deregisterService(serviceOptions1.getId()).onComplete(tc.asyncAssertSuccess());
        writeClient.deregisterService(serviceOptions2.getId()).onComplete(tc.asyncAssertSuccess());
      });
    }));
  }

  @Test
  public void serviceGet(TestContext tc) {
    ServiceOptions serviceOptions1 = createServiceOptions("id1", SERVICE_NAME, "10.10.10.11", 8080);
    ServiceOptions serviceOptions2 = createServiceOptions("id2", SERVICE_NAME, "10.10.10.11", 8081);
    TxnRequest getRequest = new TxnRequest()
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions1).setType(TxnServiceVerb.GET))
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions2).setType(TxnServiceVerb.GET));
    writeClient.transaction(getRequest).onComplete(tc.asyncAssertSuccess(resp1 -> {
      tc.assertEquals(2, resp1.getErrorsSize());
      tc.assertEquals(0, resp1.getResultsSize());
      registerServiceAndGet(tc, serviceOptions1, getRequest, resp2 -> {
        tc.assertEquals(1, resp2.getErrorsSize());
        tc.assertEquals(0, resp2.getResultsSize());
        registerServiceAndGet(tc, serviceOptions2, getRequest, resp3 -> {
          tc.assertEquals(0, resp3.getErrorsSize());
          tc.assertEquals(2, resp3.getResultsSize());
          List<Service> services = getServices(resp3);
          checkService(tc, services, serviceOptions1);
          checkService(tc, services, serviceOptions2);
          writeClient.deregisterCatalogService(getNodeName(), serviceOptions1.getId()).onComplete(tc.asyncAssertSuccess());
          writeClient.deregisterCatalogService(getNodeName(), serviceOptions2.getId()).onComplete(tc.asyncAssertSuccess());
        });
      });
    }));
  }

  @Test
  public void serviceDelete(TestContext tc) {
    ServiceOptions serviceOptions1 = createServiceOptions("id1", SERVICE_NAME, "10.10.10.10", 8080);
    ServiceOptions serviceOptions2 = createServiceOptions("id2", SERVICE_NAME, "10.10.10.10", 8081);
    TxnRequest deleteRequest = new TxnRequest()
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions1).setType(TxnServiceVerb.DELETE))
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions2).setType(TxnServiceVerb.DELETE));
    writeClient.transaction(deleteRequest).onComplete(tc.asyncAssertSuccess(resp1 -> {
      tc.assertEquals(0, resp1.getErrorsSize());
      tc.assertEquals(0, resp1.getResultsSize());
      writeClient.registerService(serviceOptions1).onComplete(tc.asyncAssertSuccess(resp2 -> {
        getRegistrations(tc, SERVICE_NAME, registrations1 -> {
          checkService(tc, registrations1, serviceOptions1);
          writeClient.transaction(deleteRequest).onComplete(tc.asyncAssertSuccess(resp3 -> {
            tc.assertEquals(0, resp1.getErrorsSize());
            tc.assertEquals(0, resp1.getResultsSize());
            getRegistrations(tc, SERVICE_NAME, registrations2 -> {
              tc.assertEquals(0, registrations2.size());
            });
          }));
        });
      }));
    }));
  }

  @Test
  public void serviceCas(TestContext tc) {
    ServiceOptions serviceOptions1 = createServiceOptions("id1", SERVICE_NAME, "10.10.10.10", 8080);
    ServiceOptions serviceOptions2 = createServiceOptions("id2", SERVICE_NAME, "10.10.10.10", 8081);
    Map<String, String> meta = new HashMap<>();
    meta.put("test1", "value2");
    writeClient.registerService(serviceOptions1).onComplete(tc.asyncAssertSuccess(resp1 -> {
      writeClient.registerService(serviceOptions2).onComplete(tc.asyncAssertSuccess(resp2 -> {
        getRegistrations(tc, SERVICE_NAME, registrations1 -> {
          long idx1 = getModifyIndex(tc, registrations1, serviceOptions1);
          long idx2 = getModifyIndex(tc, registrations1, serviceOptions2);
          TxnRequest requestWithStaleIndex = new TxnRequest()
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setType(TxnServiceVerb.CAS).setServiceOptions(
              serviceOptions1.setMeta(meta).setModifyIndex(idx1 - 1)))
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setType(TxnServiceVerb.CAS).setServiceOptions(
              serviceOptions2.setMeta(meta).setModifyIndex(idx2)));
          writeClient.transaction(requestWithStaleIndex).onComplete(tc.asyncAssertSuccess(resp3 -> {
            tc.assertEquals(1, resp3.getErrorsSize());
            tc.assertEquals(0, resp3.getResultsSize());
            TxnRequest request = new TxnRequest()
              .addOperation(new TxnServiceOperation().setNode(getNodeName()).setType(TxnServiceVerb.CAS).setServiceOptions(
                serviceOptions1.setMeta(meta).setModifyIndex(idx1)))
              .addOperation(new TxnServiceOperation().setNode(getNodeName()).setType(TxnServiceVerb.CAS).setServiceOptions(
                serviceOptions2.setMeta(meta).setModifyIndex(idx2)));
            writeClient.transaction(request).onComplete(tc.asyncAssertSuccess(resp4 -> {
              tc.assertEquals(0, resp4.getErrorsSize());
              tc.assertEquals(2, resp4.getResultsSize());
              getRegistrations(tc, SERVICE_NAME, registrations2 -> {
                checkService(tc, registrations2, serviceOptions1);
                checkService(tc, registrations2, serviceOptions2);
                writeClient.deregisterCatalogService(getNodeName(), serviceOptions1.getId()).onComplete(tc.asyncAssertSuccess());
                writeClient.deregisterCatalogService(getNodeName(), serviceOptions2.getId()).onComplete(tc.asyncAssertSuccess());
              });
            }));
          }));
        });
      }));
    }));
  }

  @Test
  public void serviceMultiOperationSuccess(TestContext tc) {
    ServiceOptions serviceOptions1 = createServiceOptions("id1", SERVICE_NAME, "10.10.10.10", 8080);
    ServiceOptions serviceOptions2 = createServiceOptions("id2", SERVICE_NAME, "10.10.10.10", 8081);
    ServiceOptions serviceOptions3 = createServiceOptions("id3", SERVICE_NAME, "10.10.10.10", 8082);
    writeClient.registerService(serviceOptions1).onComplete(tc.asyncAssertSuccess(resp1 -> {
      writeClient.registerService(serviceOptions2).onComplete(tc.asyncAssertSuccess(resp2 -> {
        getRegistrations(tc, SERVICE_NAME, registrations1 -> {
          tc.assertEquals(2, registrations1.size());
          Map<String, String> meta = new HashMap<>();
          meta.put("test2", "value2");
          serviceOptions2.setMeta(meta);
          TxnRequest multiOpRequest = new TxnRequest()
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions1).setType(TxnServiceVerb.DELETE))
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions2).setType(TxnServiceVerb.SET))
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions3).setType(TxnServiceVerb.SET));
          writeClient.transaction(multiOpRequest).onComplete(tc.asyncAssertSuccess(resp3 -> {
            tc.assertEquals(0, resp3.getErrorsSize());
            tc.assertEquals(2, resp3.getResultsSize());
            getRegistrations(tc, SERVICE_NAME, registrations2 -> {
              tc.assertEquals(2, registrations2.size());
              checkService(tc, registrations2, serviceOptions2);
              checkService(tc, registrations2, serviceOptions3);
              writeClient.deregisterCatalogService(getNodeName(), serviceOptions2.getId()).onComplete(tc.asyncAssertSuccess());
              writeClient.deregisterCatalogService(getNodeName(), serviceOptions3.getId()).onComplete(tc.asyncAssertSuccess());
            });
          }));
        });
      }));
    }));
  }

  @Test
  public void serviceMultiOperationError(TestContext tc) {
    ServiceOptions serviceOptions1 = createServiceOptions("id1", SERVICE_NAME, "10.10.12.10", 8080);
    ServiceOptions serviceOptions2 = createServiceOptions("id2", SERVICE_NAME, "10.10.12.10", 8081);
    ServiceOptions serviceOptions3 = createServiceOptions("id3", "", "10.10.12.10", 8082);
    writeClient.registerService(serviceOptions1).onComplete(tc.asyncAssertSuccess(resp1 -> {
      writeClient.registerService(serviceOptions2).onComplete(tc.asyncAssertSuccess(resp2 -> {
        getRegistrations(tc, SERVICE_NAME, registrations1 -> {
          checkService(tc, registrations1, serviceOptions1);
          checkService(tc, registrations1, serviceOptions2);
          TxnRequest multiOpRequest = new TxnRequest()
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions1).setType(TxnServiceVerb.DELETE))
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions2).setType(TxnServiceVerb.GET))
            .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions3).setType(TxnServiceVerb.SET));
          writeClient.transaction(multiOpRequest).onComplete(tc.asyncAssertSuccess(resp3 -> {
            tc.assertEquals(1, resp3.getErrorsSize());
            tc.assertEquals(2, resp3.getError(0).getOpIndex());
            tc.assertEquals(0, resp3.getResultsSize());
            getRegistrations(tc, SERVICE_NAME, registrations2 -> {
              tc.assertEquals(2, registrations2.size());
              checkService(tc, registrations2, serviceOptions1);
              checkService(tc, registrations2, serviceOptions2);
              writeClient.deregisterCatalogService(getNodeName(), serviceOptions1.getId()).onComplete(tc.asyncAssertSuccess());
              writeClient.deregisterCatalogService(getNodeName(), serviceOptions2.getId()).onComplete(tc.asyncAssertSuccess());
            });
          }));
        });
      }));
    }));
  }

  @Test
  public void kvAndServiceSet(TestContext tc) {
    ServiceOptions serviceOptions = createServiceOptions("id1", SERVICE_NAME, "10.10.10.10", 8080);
    TxnRequest request = new TxnRequest()
      .addOperation(new TxnKVOperation().setKey("foo/bar/t1").setValue("val1").setType(TxnKVVerb.SET))
      .addOperation(new TxnKVOperation().setKey("foo/bar/t2").setValue("val2").setType(TxnKVVerb.SET))
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions).setType(TxnServiceVerb.SET));
    writeClient.transaction(request).onComplete(tc.asyncAssertSuccess(response -> {
      tc.assertEquals(0, response.getErrorsSize());
      tc.assertEquals(3, response.getResultsSize());
      List<String> keys = getKeys(response);
      tc.assertTrue(keys.contains("foo/bar/t1"));
      tc.assertTrue(keys.contains("foo/bar/t2"));
      List<Service> services = getServices(response);
      checkService(tc, services, serviceOptions);
      getEntries(tc, "foo/bar/t", entries -> {
        tc.assertTrue(entries.contains("foo/bar/t1/val1"));
        tc.assertTrue(entries.contains("foo/bar/t2/val2"));
        writeClient.deleteValues("foo/bar/t").onComplete(tc.asyncAssertSuccess());
        getRegistrations(tc, SERVICE_NAME, registrations2 -> {
          checkService(tc, registrations2, serviceOptions);;
          writeClient.deregisterCatalogService(getNodeName(), serviceOptions.getId()).onComplete(tc.asyncAssertSuccess());
        });
      });
    }));
  }

  @Test
  public void kvAndServiceError(TestContext tc) {
    ServiceOptions serviceOptions = createServiceOptions("id1", SERVICE_NAME, "10.10.10.10", 8080);
    TxnRequest request = new TxnRequest()
      .addOperation(new TxnKVOperation().setKey("foo/bar/t1").setValue("val1").setType(TxnKVVerb.SET))
      .addOperation(new TxnKVOperation().setKey("foo/bar/t2").setValue("val2").setType(TxnKVVerb.SET))
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions).setType(TxnServiceVerb.GET));
    writeClient.transaction(request).onComplete(tc.asyncAssertSuccess(response -> {
      tc.assertEquals(1, response.getErrorsSize());
      tc.assertEquals(2, response.getError(0).getOpIndex());
      tc.assertEquals(0, response.getResultsSize());
      List<String> keys = getKeys(response);
      tc.assertTrue(keys.isEmpty());
      List<Service> services = getServices(response);
      tc.assertTrue(services.isEmpty());
      readClient.getValues("foo/bar/t").onComplete(tc.asyncAssertSuccess(entries -> {
        tc.assertTrue(entries.getList() == null);
      }));
    }));
  }

  @Test
  public void testJsonToTxnRequest(TestContext tc) {
    ServiceOptions serviceOptions = createServiceOptions("id1", SERVICE_NAME, "10.10.10.10", 8080);
    Map<String, String> meta = new HashMap<>();
    meta.put("test1", "value2");
    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    serviceOptions.setMeta(meta);
    serviceOptions.setTags(tags);
    TxnRequest txnRequest = new TxnRequest()
      .addOperation(new TxnKVOperation().setKey("foo/bar/t1").setValue("val1").setType(TxnKVVerb.SET))
      .addOperation(new TxnServiceOperation().setNode(getNodeName()).setServiceOptions(serviceOptions).setType(TxnServiceVerb.SET));
    JsonObject jsonRequest = txnRequest.toJson();
    List<JsonObject> jsonOperations = jsonRequest.getJsonArray("operations").getList();
    tc.assertEquals(serviceOptions.getName(), jsonOperations.get(1).getJsonObject("Service").getJsonObject("Service").getString("Service"));
    TxnRequest deserializedTxnRequest = new TxnRequest(jsonRequest);
    tc.assertTrue(deserializedTxnRequest.getOperations().get(0) instanceof TxnKVOperation);
    TxnKVOperation deserializedTxnKVOperation = (TxnKVOperation) deserializedTxnRequest.getOperations().get(0);
    tc.assertEquals("foo/bar/t1", deserializedTxnKVOperation.getKey());
    tc.assertEquals("val1", deserializedTxnKVOperation.getValue());
    tc.assertEquals(TxnKVVerb.SET, deserializedTxnKVOperation.getType());
    tc.assertEquals(TxnOperationType.KV, deserializedTxnKVOperation.getOperationType());
    tc.assertTrue(deserializedTxnRequest.getOperations().get(1) instanceof TxnServiceOperation);
    TxnServiceOperation deserializedTxnServiceOperation = (TxnServiceOperation) deserializedTxnRequest.getOperations().get(1);
    tc.assertEquals(getNodeName(), deserializedTxnServiceOperation.getNode());
    tc.assertEquals(TxnServiceVerb.SET, deserializedTxnServiceOperation.getType());
    tc.assertEquals(TxnOperationType.SERVICE, deserializedTxnServiceOperation.getOperationType());
    ServiceOptions deserializedServiceOptions = deserializedTxnServiceOperation.getServiceOptions();
    tc.assertEquals(serviceOptions.getId(), deserializedServiceOptions.getId());
    tc.assertEquals(serviceOptions.getName(), deserializedServiceOptions.getName());
    tc.assertEquals(serviceOptions.getAddress(), deserializedServiceOptions.getAddress());
    tc.assertEquals(serviceOptions.getPort(), deserializedServiceOptions.getPort());
    tc.assertEquals(serviceOptions.getTags(), deserializedServiceOptions.getTags());
    tc.assertEquals(serviceOptions.getMeta(), deserializedServiceOptions.getMeta());
    tc.assertEquals(serviceOptions.getCreateIndex(), deserializedServiceOptions.getCreateIndex());
    tc.assertEquals(serviceOptions.getModifyIndex(), deserializedServiceOptions.getModifyIndex());
  }

  @Test
  public void testJsonToTxnResponse(TestContext tc) {
    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    Map<String, String> meta = new HashMap<>();
    meta.put("test1", "value2");
    Service service = new Service()
      .setId("id1")
      .setName(SERVICE_NAME)
      .setAddress("10.10.10.10")
      .setPort(8080)
      .setMeta(meta)
      .setTags(tags);
    KeyValue keyValue = new KeyValue()
      .setKey("foo/bar/t1")
      .setValue("val1");
    TxnError txnError = new TxnError()
      .setOpIndex(0)
      .setWhat("Missing node registration");
    TxnResponse txnResponse = new TxnResponse()
      .addResult(keyValue)
      .addResult(service)
      .addError(txnError);
    JsonObject jsonObject = txnResponse.toJson();
    List<JsonObject> jsonResults = jsonObject.getJsonArray("Results").getList();
    tc.assertEquals(service.getName(), jsonResults.get(1).getJsonObject("Service").getString("Service"));
    TxnResponse deserializedTxnResponse = new TxnResponse(jsonObject);
    tc.assertEquals(deserializedTxnResponse.getResultsSize(), 2);
    tc.assertTrue(deserializedTxnResponse.getResults().get(0) instanceof KeyValue);
    KeyValue deserializedKeyValue = (KeyValue) deserializedTxnResponse.getResults().get(0);
    tc.assertEquals(keyValue.getKey(), deserializedKeyValue.getKey());
    tc.assertEquals(keyValue.getValue(), deserializedKeyValue.getValue());
    tc.assertTrue(deserializedTxnResponse.getResults().get(1) instanceof Service);
    Service deserializedService = (Service) deserializedTxnResponse.getResults().get(1);
    tc.assertEquals(service.getId(), deserializedService.getId());
    tc.assertEquals(service.getName(), deserializedService.getName());
    tc.assertEquals(service.getAddress(), deserializedService.getAddress());
    tc.assertEquals(service.getPort(), deserializedService.getPort());
    tc.assertEquals(service.getTags(), deserializedService.getTags());
    tc.assertEquals(service.getMeta(), deserializedService.getMeta());
    tc.assertEquals(service.getCreateIndex(), deserializedService.getCreateIndex());
    tc.assertEquals(service.getModifyIndex(), deserializedService.getModifyIndex());
    tc.assertEquals(deserializedTxnResponse.getErrorsSize(), 1);
    tc.assertTrue(deserializedTxnResponse.getErrors().get(0) instanceof TxnError);
    TxnError deserializedTxnError = deserializedTxnResponse.getErrors().get(0);
    tc.assertEquals(txnError.getOpIndex(), deserializedTxnError.getOpIndex());
    tc.assertEquals(txnError.getWhat(), deserializedTxnError.getWhat());
  }

  @Test
  public void testEmptyJsonToTxnResponse(TestContext tc) {
    TxnResponse txnResponse = new TxnResponse();
    JsonObject jsonObject = txnResponse.toJson();
    TxnResponse deserializedTxnResponse = new TxnResponse(jsonObject);
    tc.assertEquals(deserializedTxnResponse.getResultsSize(), 0);
    tc.assertEquals(deserializedTxnResponse.getErrorsSize(), 0);
  }

  private void getEntries(TestContext tc, String prefix, Handler<List<String>> resultHandler) {
    readClient.getValues(prefix).onComplete(tc.asyncAssertSuccess(list -> {
      resultHandler.handle(list.getList().stream()
        .map(kv -> kv.getKey() + "/" + kv.getValue()).collect(Collectors.toList()));
    }));
  }

  private List<String> getKeys(TxnResponse resp) {
    return resp.getResults().stream()
      .filter(o -> o instanceof KeyValue)
      .map(kv -> ((KeyValue) kv).getKey()).collect(Collectors.toList());
  }

  private void createKV(TestContext tc, String key, String value, Handler<Long> resultHandler) {
    writeClient.putValue(key, value).onComplete(tc.asyncAssertSuccess(b -> {
      tc.assertTrue(b);
      readClient.getValue(key).onComplete(tc.asyncAssertSuccess(pair -> {
        resultHandler.handle(pair.getModifyIndex());
      }));
    }));
  }

  private void getRegistrations(TestContext tc, String serviceName, Handler<List<Service>> resultHandler) {
    readClient.catalogServiceNodes(serviceName).onComplete(tc.asyncAssertSuccess(list -> {
      resultHandler.handle(list.getList());
    }));
  }

  private List<Service> getServices(TxnResponse resp) {
    return resp.getResults().stream()
      .filter(o -> o instanceof Service)
      .map(s -> ((Service) s)).collect(Collectors.toList());
  }

  private ServiceOptions createServiceOptions(String id, String name, String address, int port) {
    Map<String, String> meta = new HashMap<>();
    meta.put("test1", "value1");
    List<String> tags = new ArrayList<>();
    tags.add("TAG_1");
    tags.add("TAG_2");
    return new ServiceOptions()
      .setId(id)
      .setName(name)
      .setAddress(address)
      .setPort(port)
      .setMeta(meta)
      .setTags(tags);
  }

  private static void checkService(TestContext tc, List<Service> list, ServiceOptions options) {
    Service s = list.stream()
      .filter(i -> i.getId().equals(options.getId()))
      .findFirst()
      .orElseThrow(NoSuchElementException::new);
    tc.assertEquals(s.getName(), options.getName());
    tc.assertEquals(s.getTags(), options.getTags());
    tc.assertEquals(s.getAddress(), options.getAddress());
    tc.assertEquals(s.getPort(), options.getPort());
    for (Map.Entry<String, String> entry : options.getMeta().entrySet()) {
      tc.assertEquals(s.getMeta().get(entry.getKey()), entry.getValue());
    }
  }

  private long getModifyIndex(TestContext tc, List<Service> list, ServiceOptions options) {
    Service s = list.stream()
      .filter(i -> i.getId().equals(options.getId()))
      .findFirst()
      .orElseThrow(NoSuchElementException::new);
    return s.getModifyIndex();
  }

  private void registerServiceAndGet(TestContext tc, ServiceOptions serviceOptions, TxnRequest txnRequest, Handler<TxnResponse> resultsHandler) {
    writeClient.registerService(serviceOptions).onComplete(tc.asyncAssertSuccess(resp1 -> {
      writeClient.transaction(txnRequest).onComplete(tc.asyncAssertSuccess(resp2 -> {
        resultsHandler.handle(resp2);
      }));
    }));
  }

}
