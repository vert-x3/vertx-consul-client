package io.vertx.ext.consul.tests.instance;

import com.github.dockerjava.api.model.ContainerNetwork;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.tests.dc.ConsulDatacenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.consul.ConsulContainer;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class ConsulInstance extends ConsulContainer {
  private static final Logger logger = LoggerFactory.getLogger(ConsulInstance.class);

  private final ConsulDatacenter dc;

  private JsonObject configuration = new JsonObject();

  private ConsulInstance(String image, String version, ConsulDatacenter dc) {
    super(image + ":" + version);
    this.dc = dc;
  }

  public static ConsulInstance.Builder builder() {
    return new Builder();
  }

  public static ConsulInstance.Builder defaultConsulBuilder(ConsulDatacenter dc) {
    return ConsulInstance.builder()
      .datacenter(dc)
      .keyFile("server-key.pem")
      .certFile("server-cert.pem")
      .caFile("server-cert-ca-chain.pem");
  }

  @Override
  public void stop() {
    try {
      execInContainer("consul", "leave");
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    super.stop();
  }

  public String getConfig(String name) {
    return configuration.getString(name, "");
  }

  public void setConfig(JsonObject config) {
    this.configuration = config;
  }

  public String address() {
    if (isMacOS()) return getHost();
    else return getContainerNetwork().getGateway();
  }

  public ConsulDatacenter dc() {
    return dc;
  }


  public ConsulClient createClient(Vertx vertx, String token) {
    return ConsulClient.create(vertx, consulClientOptions(token));
  }

  public ConsulClient createClient(Vertx vertx, ConsulClientOptions options) {
    return ConsulClient.create(vertx, options);
  }

  public ConsulClientOptions consulClientOptions(String token) {
    return new ConsulClientOptions()
      .setAclToken(token)
      .setDc(dc.getName())
      .setHost(address())
      .setPort(getMappedPort(Builder.HTTP_PORT));
  }

  public ConsulClient createSecureClient(
    Vertx vertx, String token, boolean trustAll, PemTrustOptions trustOptions
  ) {
    ConsulClientOptions options = consulClientOptions(token)
      .setPort(getMappedPort(Builder.HTTPS_PORT))
      .setTrustAll(trustAll)
      .setTrustOptions(trustOptions)
      .setVerifyHost(false)
      .setSsl(true);
    return ConsulClient.create(vertx, options);
  }

  public ContainerNetwork getContainerNetwork() {
    return getContainerInfo()
      .getNetworkSettings()
      .getNetworks()
      .values()
      .stream()
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Container network is unknown"));
  }

  private static boolean isMacOS() {
    String osName = System.getProperty("os.name");
    return osName != null
      && (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("darwin"));
  }

  public static class Builder {
    private static final String CONSUL_LOCAL_CONFIG_ENV = "CONSUL_LOCAL_CONFIG";
    private static final String DEFAULT_IMAGE = "consul";
    private static final String DEFAULT_VERSION = "1.13.7";
    private static final int DNS_PORT = 8600;
    private static final int HTTP_PORT = 8500;
    private static final int HTTPS_PORT = 8501;
    private static final int SERF_LAN_PORT = 8301;
    private static final int SERF_WAN_PORT = 8302;
    private static final int RPC_PORT = 8300;

    private String image;
    private String version;
    private ConsulDatacenter datacenter;
    private boolean join;
    private String serverAddr;

    private int serverSerfLanPort;

    private String nodeName;
    private String keyFile;
    private String certFile;
    private String caFile;

    private static final Random random = new Random();

    private Builder() {
      this.image = DEFAULT_IMAGE;
      this.version = DEFAULT_VERSION;
      this.nodeName = "node-" + randomHex(16);
      this.datacenter = ConsulDatacenter.create();
    }

    public Builder consulImage(String image) {
      this.image = Objects.requireNonNull(image);
      return this;
    }

    public Builder consulVersion(String version) {
      this.version = Objects.requireNonNull(version);
      return this;
    }

    public Builder datacenter(ConsulDatacenter datacenter) {
      this.datacenter = Objects.requireNonNull(datacenter);
      return this;
    }

    public Builder nodeName(String name) {
      this.nodeName = Objects.requireNonNull(name);
      return this;
    }

    public Builder join(ConsulInstance other) {
      Objects.requireNonNull(other);
      this.join = true;
      if (isMacOS()) {
        this.serverAddr = other.getContainerNetwork().getIpAddress();
        this.serverSerfLanPort = Builder.SERF_LAN_PORT;
      } else {
        //connection by gateway address and external port
        this.serverAddr = other.address();
        this.serverSerfLanPort = other.getMappedPort(Builder.SERF_LAN_PORT);
      }
      return this;
    }

    public Builder keyFile(String file) {
      this.keyFile = Objects.requireNonNull(file);
      return this;
    }

    public Builder certFile(String file) {
      this.certFile = Objects.requireNonNull(file);
      return this;
    }

    public Builder caFile(String file) {
      this.caFile = Objects.requireNonNull(file);
      return this;
    }

    public ConsulInstance build() {
      logger.info("Building a Consul container (version: {}))", version);
      ConsulInstance container = new ConsulInstance(image, version, datacenter);

      JsonObject cfg = new JsonObject();
      cfg.put("node_name", nodeName);
      cfg.put("node_id", randomNodeId());
      cfg.put("datacenter", datacenter.getName());
      cfg.put("bind_addr", "0.0.0.0");

      SemVer semVer = new SemVer(version);

      if (semVer.compareTo(new SemVer("1.4.0")) >= 0) {
        JsonObject master;
        if (semVer.compareTo(new SemVer("1.11.0")) >= 0) {
          master = new JsonObject().put("initial_management", datacenter.getMasterToken());
        } else master = new JsonObject().put("master", datacenter.getMasterToken());
        cfg.put("acl", new JsonObject()
          .put("enabled", true)
          .put("tokens", master)
        );
        cfg.put("primary_datacenter", datacenter.getName());
      } else {
        cfg.put("acl_master_token", datacenter.getMasterToken());
        cfg.put("acl_datacenter", datacenter.getName());
      }

      JsonObject ports = new JsonObject()
        .put("dns", DNS_PORT)
        .put("http", HTTP_PORT)
        .put("serf_lan", SERF_LAN_PORT)
        .put("serf_wan", SERF_WAN_PORT);
      if (semVer.compareTo(new SemVer("0.8.0")) < 0) {
        ports.put("rpc", RPC_PORT);
      } else {
        ports.put("server", RPC_PORT);
      }

      if (semVer.compareTo(new SemVer("0.8.0")) >= 0) {
        cfg.put("acl_enforce_version_8", false);
      }

      if (semVer.compareTo(new SemVer("0.9.0")) >= 0) {
        cfg.put("enable_script_checks", true);
      }

      int sslCnt = 0;
      JsonObject tls = new JsonObject();
      if (keyFile != null) {
        String path = "/tmp/" + keyFile;
        tls.put("key_file", path);
        container.withCopyFileToContainer(MountableFile.forClasspathResource(keyFile), path);
        sslCnt++;
      }
      if (certFile != null) {
        String path = "/tmp/" + certFile;
        tls.put("cert_file", path);
        container.withCopyFileToContainer(MountableFile.forClasspathResource(certFile), path);
        sslCnt++;
      }
      if (caFile != null) {
        String path = "/tmp/" + caFile;
        tls.put("ca_file", path);
        container.withCopyFileToContainer(MountableFile.forClasspathResource(caFile), path);
        sslCnt++;
      }
      if (semVer.compareTo(new SemVer("1.12.0")) >= 0) {
        cfg.put("tls", new JsonObject().put("defaults", tls));
      } else cfg.mergeIn(tls);

      if (sslCnt == 3) {
        ports.put("https", HTTPS_PORT);
      }
      cfg.put("ports", ports);

      if (join) {
        logger.info("This agent will join an agent with address {} port {}", serverAddr, serverSerfLanPort);

        cfg.put("server", false);
        cfg.put("leave_on_terminate", true);

        JsonArray joinAddresses = new JsonArray().add(serverAddr + ":" + serverSerfLanPort);
        if (semVer.compareTo(new SemVer("1.15.0")) >= 0) {
          cfg.put("retry_join", joinAddresses);
          cfg.put("retry_max", 10);
        } else cfg.put("start_join", joinAddresses);
      }
      container
        .withEnv(CONSUL_LOCAL_CONFIG_ENV, cfg.encode())
        .withExposedPorts(DNS_PORT, HTTP_PORT, HTTPS_PORT, RPC_PORT, SERF_LAN_PORT, SERF_WAN_PORT);
      container.setConfig(cfg);
      container.start();
      return container;
    }

    private static String randomNodeId() {
      return randomHex(8) + "-" + randomHex(4) + "-" + randomHex(4) + "-" + randomHex(4) + "-" + randomHex(12);
    }

    private static String randomHex(int len) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < len; i++) {
        sb.append(Long.toHexString(random.nextInt(16)));
      }
      return sb.toString();
    }
  }
}
