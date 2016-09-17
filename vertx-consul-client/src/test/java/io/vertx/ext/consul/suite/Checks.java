package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.CheckInfo;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.handleResult;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Checks extends ConsulTestBase {

    @Test
    public void testCheck1() {
        CheckOptions check = CheckOptions.ttl("10s").setId("checkId").setName("checkName");
        testClient.registerCheck(check, handleResult(h1 -> {
            testClient.localChecks(handleResult(h2 -> {
                CheckInfo c = h2.stream().filter(i -> "checkName".equals(i.getName())).findFirst().get();
                testClient.updateCheck(c.setOutput("outputMessage"), handleResult(h3 -> {
                    testClient.localChecks(handleResult(h4 -> {
                        CheckInfo c2 = h4.stream().filter(i -> "checkName".equals(i.getName())).findFirst().get();
                        assertEquals(c2.getOutput(), "outputMessage");
                        testClient.deregisterCheck(c2.getId(), handleResult(h5 -> testComplete()));
                    }));
                }));
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

}
