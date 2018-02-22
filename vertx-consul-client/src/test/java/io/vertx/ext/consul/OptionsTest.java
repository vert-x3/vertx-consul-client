package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class OptionsTest {

  @Test
  public void defaults() {
    ConsulClientOptions options = new ConsulClientOptions();
    assertEquals(options.getHost(), "localhost");
    assertEquals(options.getPort(), 8500);
    assertEquals(options.getTimeout(), 0);
    assertEquals(options.getAclToken(), null);
    assertEquals(options.getDc(), null);
  }

  @Test
  public void fromURI() {
    checkURI(URI.create("consul://host"), "host", 8500, null, null);
    checkURI(URI.create("scheme://host?acl=secret"), "host", 8500, null, "secret");
    checkURI(URI.create("will://host?aclToken=token"), "host", 8500, null, "token");
    checkURI(URI.create("be://google?dc=data"), "google", 8500, "data", null);
    checkURI(URI.create("ignored://example?dc=center&acl=000"), "example", 8500, "center", "000");
    checkURI(URI.create("full://1:2?dc=3&acl=4"), "1", 2, "3", "4");
  }

  private void checkURI(URI uri, String host, int port, String dc, String acl) {
    ConsulClientOptions options = new ConsulClientOptions(uri);
    assertEquals(options.getHost(), host);
    assertEquals(options.getPort(), port);
    assertEquals(options.getDc(), dc);
    assertEquals(options.getAclToken(), acl);
  }

  @Test
  public void fromEmptyJson() {
    ConsulClientOptions options = new ConsulClientOptions(new JsonObject());
    assertEquals(options.getHost(), "localhost");
    assertEquals(options.getPort(), 8500);
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
