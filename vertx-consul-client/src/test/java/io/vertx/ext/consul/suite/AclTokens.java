package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.AclToken;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.handleResult;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class AclTokens extends ConsulTestBase {

    @Test
    public void test3() throws InterruptedException {
        masterClient.createAclToken(AclToken.empty(), h -> {
            String id = h.result();
            masterClient.infoAclToken(id, handleResult(info -> {
                assertEquals(id, info.getId());
                testComplete();
            }));
        });
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void test4() throws InterruptedException {
        masterClient.createAclToken(AclToken.empty(), h -> {
            String id = h.result();
            masterClient.destroyAclToken(id, handleResult(destroyed -> testComplete()));
        });
        await(1, TimeUnit.SECONDS);
    }

}
