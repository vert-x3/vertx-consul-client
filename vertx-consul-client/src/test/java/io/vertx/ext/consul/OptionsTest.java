package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class OptionsTest extends VertxTestBase {

  @Test
  public void defaults() {
    ConsulClientOptions options = new ConsulClientOptions();
    assertEquals(options.getHost(), "localhost");
    assertEquals(options.getPort(), 8500);
    assertEquals(options.getTimeout(), 0);
    assertEquals(options.getAclToken(), null);
    assertEquals(options.getDc(), null);
  }

  private void checkJson(ConsulClientOptions options, JsonObject json) {
    assertEquals(options.getHost(), json.getString("host"));
    assertEquals(options.getPort(), (int) json.getInteger("port"));
    assertEquals(options.getTimeout(), (long) json.getLong("timeout"));
    assertEquals(options.getUserAgent(), json.getString("userAgent"));
    assertEquals(options.getAclToken(), json.getString("aclToken"));
    assertEquals(options.getDc(), json.getString("dc"));
  }

  @Test
  public void toJson() {
    ConsulClientOptions options = new ConsulClientOptions()
      .setHost("host")
      .setPort(42)
      .setTimeout(33)
      .setUserAgent("ag")
      .setAclToken("tok")
      .setDc("d");
    JsonObject json = options.toJson();
    checkJson(options, json);
  }

  @Test
  public void fromJson() {
    JsonObject json = new JsonObject()
      .put("host", "host")
      .put("port", 42)
      .put("timeout", 33)
      .put("userAgent", "agent")
      .put("aclToken", "top-secret")
      .put("dc", "far-away");
    ConsulClientOptions options = new ConsulClientOptions(json);
    checkJson(options, json);
  }

  @Test
  public void copy() {
    ConsulClientOptions options = new ConsulClientOptions()
      .setHost("host")
      .setPort(42)
      .setTimeout(33)
      .setUserAgent("ag")
      .setAclToken("tok")
      .setDc("d");
    ConsulClientOptions copy = new ConsulClientOptions(options);
    assertEquals(options.getHost(), copy.getHost());
    assertEquals(options.getPort(), copy.getPort());
    assertEquals(options.getTimeout(), copy.getTimeout());
    assertEquals(options.getUserAgent(), copy.getUserAgent());
    assertEquals(options.getAclToken(), copy.getAclToken());
    assertEquals(options.getDc(), copy.getDc());
  }

}
