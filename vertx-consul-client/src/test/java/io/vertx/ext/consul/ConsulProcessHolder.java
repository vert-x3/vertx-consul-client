package io.vertx.ext.consul;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import com.pszymczyk.consul.LogLevel;
import io.vertx.core.Handler;
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

    private static ConsulProcessHolder instance;

    private static ConsulProcessHolder instance() {
        if (instance == null) {
            synchronized (ConsulProcessHolder.class) {
                if (instance == null) {
                    instance = new ConsulProcessHolder();
                }
            }
        }
        return instance;
    }

    static ConsulProcess consul() {
        return instance().consul;
    }

    static String dc() {
        return DC;
    }

    static String masterToken() {
        return MASTER_TOKEN;
    }

    static String writeToken() {
        return instance().writeToken;
    }

    static String readToken() {
        return instance().readToken;
    }

    private ConsulProcess consul;
    private String writeToken;
    private String readToken;

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
                        .put("acl_datacenter", DC)
                        .encode())
                .build()
                .start();

        CountDownLatch latch = new CountDownLatch(2);
        Vertx vertx = Vertx.vertx();
        createToken(vertx, "write_rules.hcl", token -> {
            writeToken = token;
            latch.countDown();
        });
        createToken(vertx, "read_rules.hcl", token -> {
            readToken = token;
            latch.countDown();
        });
        latch.await(1, TimeUnit.SECONDS);
        vertx.close();

        if (writeToken == null || readToken == null) {
            throw new RuntimeException("Starting consul fails " + writeToken + "/" + readToken);
        }
        return consul;
    }

    private void createToken(Vertx vertx, String rules, Handler<String> tokenHandler) {
        HttpClientOptions httpClientOptions = new HttpClientOptions().setDefaultPort(consul.getHttpPort());
        HttpClient httpClient = vertx.createHttpClient(httpClientOptions);
        String rulesBody;
        try {
            rulesBody = Utils.readResource(rules);
        } catch (Exception e) {
            tokenHandler.handle(null);
            return;
        }
        String request = new JsonObject()
                .put("Rules", rulesBody)
                .encode();
        httpClient.put("/v1/acl/create?token=" + MASTER_TOKEN, h -> {
            if (h.statusCode() == 200) {
                h.bodyHandler(bh -> {
                    JsonObject responce = new JsonObject(bh.toString());
                    httpClient.close();
                    tokenHandler.handle(responce.getString("ID"));
                });
            } else {
                tokenHandler.handle(null);
            }
        }).end(request);
    }
}
