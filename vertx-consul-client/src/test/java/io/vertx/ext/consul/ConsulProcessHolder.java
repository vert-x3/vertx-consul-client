package io.vertx.ext.consul;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import com.pszymczyk.consul.LogLevel;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class ConsulProcessHolder {

    private static final String MASTER_TOKEN = "topSecret";
    private static final String DC = "test-dc";
    private static final ConsulProcessHolder INSTANCE = new ConsulProcessHolder();

    public static ConsulProcess consul() {
        return INSTANCE.consul;
    }

    static String dc() {
        return DC;
    }

    static String masterToken() {
        return MASTER_TOKEN;
    }

    static String testToken() {
        return INSTANCE.testToken;
    }

    private ConsulProcess consul;
    private String testToken;

    private ConsulProcessHolder() {
        try {
            create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ConsulProcess create() throws Exception {
        consul = ConsulStarterBuilder.consulStarter()
                .withLogLevel(LogLevel.ERR)
                .withCustomConfig(new JsonObject()
                        .put("datacenter", DC)
                        .put("acl_default_policy", "deny")
                        .put("acl_master_token", MASTER_TOKEN)
                        .put("acl_datacenter", DC).encode())
                .build()
                .start();
        HttpClientOptions httpClientOptions = new HttpClientOptions().setDefaultPort(consul.getHttpPort());
        HttpClient httpClient = Vertx.vertx().createHttpClient(httpClientOptions);
        CountDownLatch latch = new CountDownLatch(1);
        httpClient.put("/v1/acl/create?token=" + MASTER_TOKEN, h -> {
            if (h.statusCode() == 200) {
                h.bodyHandler(bh -> {
                    JsonObject responce = new JsonObject(bh.toString());
                    testToken = responce.getString("ID");
                    httpClient.close();
                    latch.countDown();
                });
            }
        }).end(new JsonObject().put("Rules", Utils.readResource("default_rules.hcl")).encode());
        latch.await(1, TimeUnit.SECONDS);
        if (testToken == null) {
            throw new RuntimeException("Starting consul fails");
        }
        return consul;
    }

}
