/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul.dc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import com.pszymczyk.consul.LogLevel;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulAgent {

  private static final ObjectMapper mapper = new ObjectMapper();

  private final ConsulProcess process;
  private final String consulVersion;
  private final String masterToken;
  private final String host;
  private final String name;
  private final int httpsPort;

  ConsulAgent(ConsulDatacenter dc, ConsulAgentOptions options) {
    JsonObject cfg = new JsonObject()
      .put("server", true)
      .put("advertise_addr", options.getAddress())
      .put("client_addr", options.getAddress())
      .put("leave_on_terminate", true)
      .put("datacenter", dc.getName())
      .put("node_name", options.getNodeName())
      .put("node_id", options.getNodeId())
      .put("acl_default_policy", "deny")
      .put("acl_master_token", dc.getMasterToken())
      .put("acl_datacenter", dc.getName());
    if (options.getConsulVersion().compareTo("0.8.0") < 0) {
      cfg.put("ports", new JsonObject().put("rpc", getFreePort()));
    }
    if (options.getConsulVersion().compareTo("0.8.0") >= 0) {
      cfg.put("acl_enforce_version_8", false);
    }
    if (options.getConsulVersion().compareTo("0.9.0") >= 0) {
      cfg.put("enable_script_checks", true);
    }
    int sslCnt = 0;
    if (options.getKeyFile() != null) {
      cfg.put("key_file", options.getKeyFile());
      sslCnt++;
    }
    if (options.getCertFile() != null) {
      cfg.put("cert_file", options.getCertFile());
      sslCnt++;
    }
    if (options.getCaFile() != null) {
      cfg.put("ca_file", options.getCaFile());
      sslCnt++;
    }
    if (sslCnt == 3) {
      httpsPort = getFreePort();
      JsonObject ports = cfg.getJsonObject("ports", new JsonObject());
      ports.put("https", httpsPort);
      cfg.put("ports", ports).put("addresses", new JsonObject().put("https", "0.0.0.0"));
    } else {
      httpsPort = -1;
    }
    List<ConsulAgent> existingAgents = dc.getAgents();
    if (!existingAgents.isEmpty()) {
      cfg.put("start_join", existingAgents.stream()
        .map(agent -> "127.0.0.1:" + agent.getSerfLanPort())
        .collect(Collector.of(JsonArray::new, JsonArray::add, JsonArray::addAll)));
    }
    consulVersion = options.getConsulVersion();
    host = options.getAddress();
    name = options.getNodeName();
    masterToken = dc.getMasterToken();
    process = ConsulStarterBuilder.consulStarter()
      .withLogLevel(LogLevel.ERR)
      .withConsulVersion(options.getConsulVersion())
      .withCustomConfig(cfg.encode())
      .build()
      .start();
  }

  void stop() {
    process.close();
  }

  public String createAclToken(String rules) throws IOException {
    HttpClient client = new DefaultHttpClient();
    HttpPut put = new HttpPut("http://" + getHost() + ":" + getHttpPort() + "/v1/acl/create");
    put.addHeader("X-Consul-Token", masterToken);
    Map<String, String> tokenRequest = new HashMap<>();
    tokenRequest.put("Type", "client");
    tokenRequest.put("Rules", rules);
    put.setEntity(new StringEntity(mapper.writeValueAsString(tokenRequest)));
    HttpResponse response = client.execute(put);
    if (response.getStatusLine().getStatusCode() != 200) {
      throw new RuntimeException("Bad response");
    }
    Map<String, String> tokenResponse = mapper.readValue(EntityUtils.toString(response.getEntity()), new TypeReference<Map<String, String>>() {
    });
    return tokenResponse.get("ID");
  }

  public int getSerfLanPort() {
    return process.getSerfLanPort();
  }

  public int getHttpPort() {
    return process.getHttpPort();
  }

  public int getHttpsPort() {
    return httpsPort;
  }

  public String getHost() {
    return host;
  }

  public String getName() {
    return name;
  }

  public String getConsulVersion() {
    return consulVersion;
  }

  private static int getFreePort() {
    int port = -1;
    try {
      ServerSocket socket = new ServerSocket(0);
      port = socket.getLocalPort();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return port;
  }
}
