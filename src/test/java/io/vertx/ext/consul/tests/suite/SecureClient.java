package io.vertx.ext.consul.tests.suite;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.tests.ConsulTestBase;
import io.vertx.ext.consul.tests.Utils;
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
      .addCertValue(Buffer.buffer(Utils.readResource("server-cert-ca-chain.pem")));
    go(tc, false, options);
  }

  private void go(TestContext tc, boolean trustAll, PemTrustOptions trustOptions) {
    ConsulClient secureClient = consul.createSecureClient(vertx, consul.dc().getMasterToken(), trustAll, trustOptions);
    secureClient.putValue("foo/bars42", "value42").onComplete(tc.asyncAssertSuccess(b -> {
      tc.assertTrue(b);
      secureClient.getValue("foo/bars42").onComplete(tc.asyncAssertSuccess(pair -> {
        tc.assertEquals(pair.getValue(), "value42");
        secureClient.close();
      }));
    }));
  }
}
