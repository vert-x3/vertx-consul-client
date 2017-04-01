package io.vertx.ext.consul.suite;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.Utils;
import org.junit.Test;

import static io.vertx.ext.consul.Utils.getAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class SecureClient extends ConsulTestBase {

  @Test
  public void withTrustAll() {
    go(true, null);
  }

  @Test
  public void withTrustOptions() throws Exception {
    PemTrustOptions options = new PemTrustOptions()
      .addCertValue(Buffer.buffer(Utils.readResource("client-cert.pem")));
    go(false, options);
  }

  private void go(boolean trustAll, PemTrustOptions trustOptions) {
    ConsulClient secureClient = ctx.createSecureClient(trustAll, trustOptions);
    assertTrue(getAsync(h -> secureClient.putValue("foo/bars42", "value42", h)));
    KeyValue pair = getAsync(h -> secureClient.getValue("foo/bars42", h));
    assertEquals(pair.getValue(), "value42");
    ctx.closeClient(secureClient);
  }
}
