package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.CheckOptions;

import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Checks extends ChecksBase {

  @Override
  String createCheck(CheckOptions opts) {
    String id = opts.getId();
    if (id == null) {
      id = "checkId";
      opts.setId(id);
    }
    runAsync(h -> writeClient.registerCheck(opts, h));
    return id;
  }

}
