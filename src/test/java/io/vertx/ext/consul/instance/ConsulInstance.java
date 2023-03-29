package io.vertx.ext.consul.instance;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.dc.ConsulDatacenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class ConsulInstance {

  private static final Logger logger = LoggerFactory.getLogger(ConsulInstance.class);

  public final ConsulContainer container;

  private ConsulInstance(ConsulContainer container) {
    this.container = container;
  }

  public static ConsulInstance start() {
    return builder().build();
  }

  public static ConsulInstance start(int mappedHttpPort) {
    return builder(mappedHttpPort).build();
  }

  public void shutdown() {
    if (container != null) {
      container.stop();
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(int mappedHttpPort) {
    return new Builder(mappedHttpPort);
  }

  public int httpPort() {
    Integer httpPort = container.exposedPorts().getValue(ConsulPorts.ConsulPort.HTTP);
    return container.getMappedPort(httpPort);
  }

  public int httpsPort() {
    Integer httpsPort = container.exposedPorts().getValue(ConsulPorts.ConsulPort.HTTPS);
    return container.getMappedPort(httpsPort);
  }

  public int serfLanPort() {
    Integer selfLanPort = container.exposedPorts().getValue(ConsulPorts.ConsulPort.SERF_LAN);
    return container.getMappedPort(selfLanPort);
  }

  public String address() {
    return container.gateway();
  }

  public String target() {
    return "consul://" + address() + ":" + httpPort();
  }

  public ConsulDatacenter dc() {
    return container.datacenter();
  }

  public void putValue(String key, String value) throws IOException, InterruptedException {
    container.execInContainer("/bin/sh", "-c", "consul kv put " + key + " " + value);
  }

  public String getValue(String key) throws IOException, InterruptedException {
    return container
      .execInContainer("/bin/sh", "-c", "consul kv get " + key)
      .getStdout();
  }

  public ConsulClient createClient(Vertx vertx, String token, boolean secure) {
    return ConsulClient.create(vertx, consulClientOptions(token, secure));
  }

  public ConsulClient createClient(Vertx vertx, ConsulClientOptions options) {
    return ConsulClient.create(vertx, options);
  }

  public ConsulClientOptions consulClientOptions(String token, boolean secure) {
    return new ConsulClientOptions()
      .setAclToken(token)
      .setDc(container.datacenter().getName())
      .setHost(address())
      .setPort(secure ? httpsPort() : httpPort())
      .setSsl(secure);
  }

  public ConsulClient createSecureClient(
    Vertx vertx, String token, boolean trustAll, PemTrustOptions trustOptions
  ) {
    ConsulClientOptions options = consulClientOptions(token, true)
      .setTrustAll(trustAll)
      .setPemTrustOptions(trustOptions);
    return ConsulClient.create(vertx, options);
  }


  public static class Builder {
    private static final String CONSUL_ENCRYPT_KEY = "CONSUL_ENCRYPT";
    private static final String CONSUL_LOCAL_CONFIG_ENV = "CONSUL_LOCAL_CONFIG";

    private static final String DEFAULT_IMAGE = "consul";
    private static final String DEFAULT_VERSION = "1.7.0";
    private static final String DEFAULT_ADDRESS = "127.0.0.1";

    private ConsulPorts exposedPorts;
    private String image;
    private String version;
    private ConsulDatacenter datacenter;
    private String encrypt;
    private boolean join;
    private String serverAddr;

    private String address;
    private int serverSerfLanPort;
    private int mappedHttpPort;

    private String nodeName;
    private String nodeId;

    private String keyFile;
    private String certFile;
    private String caFile;

    private static final Random random = new Random();

    private Builder() {
      this.address = DEFAULT_ADDRESS;
      this.version = DEFAULT_VERSION;
      this.image = DEFAULT_IMAGE;
      this.exposedPorts = ConsulPorts.builder().build();
      this.nodeName = "node-" + randomHex(16);
      this.nodeId = randomNodeId();
      this.datacenter = ConsulDatacenter.create();
    }

    private Builder(int mappedHttpPort) {
      this.address = DEFAULT_ADDRESS;
      this.version = DEFAULT_VERSION;
      this.image = DEFAULT_IMAGE;
      this.exposedPorts = ConsulPorts.builder().build();
      this.nodeName = "node-" + randomHex(16);
      this.nodeId = randomNodeId();
      this.datacenter = ConsulDatacenter.create();
      this.mappedHttpPort = mappedHttpPort;
    }

    private static String getProperty(String key) {
      String result = System.getProperty(key);
      if (result == null || result.isEmpty()) {
        result = System.getenv(key);
      }
      return result;
    }

    public ConsulInstance.Builder exposedPorts(ConsulPorts ports) {
      this.exposedPorts = Objects.requireNonNull(ports);
      return this;
    }

    public ConsulInstance.Builder consulImage(String image) {
      this.image = Objects.requireNonNull(image);
      return this;
    }

    public ConsulInstance.Builder consulVersion(String version) {
      this.version = Objects.requireNonNull(version);
      return this;
    }

    public ConsulInstance.Builder datacenter(ConsulDatacenter datacenter) {
      this.datacenter = Objects.requireNonNull(datacenter);
      return this;
    }

    public ConsulInstance.Builder encrypt() {
      return encrypt(getProperty(CONSUL_ENCRYPT_KEY));
    }

    public ConsulInstance.Builder encrypt(String encrypt) {
      Objects.requireNonNull(encrypt, "Encryption key is not defined");
      this.encrypt = encrypt;
      return this;
    }

    public ConsulInstance.Builder address(ConsulPorts ports) {
      this.exposedPorts = Objects.requireNonNull(ports);
      return this;
    }

    public ConsulInstance.Builder nodeName(String name) {
      this.nodeName = Objects.requireNonNull(name);
      return this;
    }

    public ConsulInstance.Builder nodeId(String id) {
      this.nodeId = Objects.requireNonNull(id);
      return this;
    }

    public ConsulInstance.Builder address(String address) {
      this.address = Objects.requireNonNull(address);
      return this;
    }

    public ConsulInstance.Builder join(ConsulInstance other) {
      Objects.requireNonNull(other);
      this.join = true;
      // todo macos
      //подключение по адерсу gateway и внешнему порту
      this.serverAddr = other.address();
      this.serverSerfLanPort = other.serfLanPort();
      return this;
    }

    public ConsulInstance.Builder keyFile(String file) {
      this.keyFile = Objects.requireNonNull(file);
      return this;
    }

    public ConsulInstance.Builder certFile(String file) {
      this.certFile = Objects.requireNonNull(file);
      return this;
    }

    public ConsulInstance.Builder caFile(String file) {
      this.caFile = Objects.requireNonNull(file);
      return this;
    }

    public ConsulInstance build() {
      logger.info("Building a Consul container (version: {}, exposed ports: {})", version, exposedPorts);
      ConsulContainer container = new ConsulContainer(
        image, version, exposedPorts, datacenter, nodeName
      );

      JsonObject cfg = new JsonObject();
      cfg.put("server", true);
      cfg.put("advertise_addr", address);
      cfg.put("client_addr", address);
      cfg.put("leave_on_terminate", true);
      cfg.put("node_name", nodeName);
      cfg.put("node_id", nodeId);
      cfg.put("datacenter", datacenter.getName());

      SemVer semVer = new SemVer(version);

      if (semVer.compareTo(new SemVer("1.4.0")) >= 0) {
        JsonObject master;
        if (semVer.compareTo(new SemVer("1.11.0")) >= 0) {
          master = new JsonObject().put("initial_management", datacenter.getMasterToken());
        } else master = new JsonObject().put("master", datacenter.getMasterToken());
        cfg.put("acl", new JsonObject()
          .put("enabled", true)
          .put("default_policy", "deny")
          .put("tokens", master)
        );
        cfg.put("primary_datacenter", datacenter.getName());
      } else {
        cfg.put("acl_default_policy", "deny");
        cfg.put("acl_master_token", datacenter.getMasterToken());
        cfg.put("acl_datacenter", datacenter.getName());
      }

      JsonObject ports = new JsonObject()
        .put("dns", exposedPorts.getValue(ConsulPorts.ConsulPort.DNS))
        .put("http", exposedPorts.getValue(ConsulPorts.ConsulPort.HTTP))
        .put("serf_lan", exposedPorts.getValue(ConsulPorts.ConsulPort.SERF_LAN))
        .put("serf_wan", exposedPorts.getValue(ConsulPorts.ConsulPort.SERF_WAN));
      if (semVer.compareTo(new SemVer("0.8.0")) < 0) {
        ports.put("rpc", exposedPorts.getValue(ConsulPorts.ConsulPort.RPC));
      } else {
        ports.put("server", exposedPorts.getValue(ConsulPorts.ConsulPort.RPC));
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
        ports.put("https", exposedPorts.getValue(ConsulPorts.ConsulPort.HTTPS));
      }
      cfg.put("ports", ports);

      if (mappedHttpPort != 0) {
        container.setPortBindings(Collections.singletonList(mappedHttpPort + ":" + exposedPorts.getValue(ConsulPorts.ConsulPort.HTTP)));
      }

      if (join) {
        logger.info("This agent will join an agent with address {} port {}", serverAddr, serverSerfLanPort);

        if (encrypt == null) {
          logger.warn("*** This agent will use plaintext to communicate with an agent {}. " +
            "If the connection is expected to be encrypted, " +
            "make sure to set {} environment variable ***", serverAddr, CONSUL_ENCRYPT_KEY);
        }
        JsonArray joinAddresses = new JsonArray().add(serverAddr + ":" + serverSerfLanPort);
        if (semVer.compareTo(new SemVer("1.15.0")) >= 0) {
          cfg.put("retry_join", joinAddresses);
          cfg.put("retry_max", 10);
        } else cfg.put("start_join", joinAddresses);
      }

      container
        .withEnv(CONSUL_LOCAL_CONFIG_ENV, cfg.encode())
        .withExposedPorts(exposedPorts.getValues());
      container.start();
      return new ConsulInstance(container);
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

