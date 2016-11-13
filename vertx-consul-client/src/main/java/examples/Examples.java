package examples;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.Service;

import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Examples {

  public void exampleCreateDefault(Vertx vertx) {

    ConsulClient client = ConsulClient.create(vertx);

  }

  public void exampleCreateWithOptions(Vertx vertx) {

    JsonObject config = new JsonObject().put("host", "consul.example.com");

    ConsulClient client = ConsulClient.create(vertx, config);

  }

  public void tcpHealth(ConsulClient consulClient, Vertx vertx) {

    Handler<HttpServerRequest> alwaysGood = h -> h.response()

      .setStatusCode(200)

      .end();

    // create HTTP server to responce health check

    vertx.createHttpServer()

      .requestHandler(alwaysGood)

      .listen(4848);

    // check health via TCP port every 1 sec

    CheckOptions opts = new CheckOptions().setTcp("localhost:4848").setInterval("1s");

    // register TCP check

    consulClient.registerCheck(opts, res -> {

      if (res.succeeded()) {

        System.out.println("check successfully registered");

      } else {

        res.cause().printStackTrace();

      }

    });
  }

  public void kv(ConsulClient consulClient) {

    // put Key/Value pair to Consul Store
    consulClient.putValue("foo", "bar", res -> {

      if (res.succeeded()) {

        System.out.println("result of the operation: " + (res.result() ? "success" : "fail"));

      } else {

        res.cause().printStackTrace();

      }

    });

    //  get Key/Value pair from Consul Store
    consulClient.getValue("foo", res -> {

      if (res.succeeded()) {

        System.out.println("retrieved value: " + res.result().getValue());

      } else {

        res.cause().printStackTrace();

      }

    });

  }

  public void services(ConsulClient consulClient) {

    consulClient.catalogServiceNodes("consul", res -> {

      if (res.succeeded()) {

        List<Service> serviceList = res.result();

        System.out.println("found " + serviceList.size() + " services");

        for (Service service : serviceList) {

          System.out.println("Service node: " + service.getNode());

          System.out.println("Service node address: " + service.getNodeAddress());

        }

      } else {

        res.cause().printStackTrace();

      }

    });

  }
  
}
