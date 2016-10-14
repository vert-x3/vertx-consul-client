package examples;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.ConsulClient;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Examples {

  public void exampleCreateClient(Vertx vertx, JsonObject config) {

    ConsulClient client = ConsulClient.create(vertx, config);

  }

}
