package io.vertx.ext.consul.suite;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Utils;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class SecureClient extends ConsulTestBase {

  @Test
  public void withTrustAll(TestContext tc) {
    go(tc, true, null);
  }

  @Test
  public void withTrustOptions(TestContext tc) throws Exception {
    PemTrustOptions options = new PemTrustOptions()
      .addCertValue(Buffer.buffer(Utils.readResource("client-cert.pem")));
    go(tc, false, options);
  }

  private void go(TestContext tc, boolean trustAll, PemTrustOptions trustOptions) {
    ConsulClient secureClient = ctx.createSecureClient(trustAll, trustOptions);
    secureClient.putValue("foo/bars42", "value42", tc.asyncAssertSuccess(b -> {
      tc.assertTrue(b);
      secureClient.getValue("foo/bars42", tc.asyncAssertSuccess(pair -> {
        tc.assertEquals(pair.getValue(), "value42");
        ctx.closeClient(secureClient);
      }));
    }));
  }
}
