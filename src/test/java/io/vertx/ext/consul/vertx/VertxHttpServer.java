package io.vertx.ext.consul.vertx;

import com.github.dockerjava.api.model.ContainerNetwork;
import io.vertx.ext.consul.CheckStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;

public class VertxHttpServer extends GenericContainer<VertxHttpServer> {
  private static final String IMAGE = System.getProperty("openet.vertx4.image.registry");
  private final String VERTICLE_HOME = "/usr/verticles";
  private final String STATUS_FILE = "health_status.txt";
  private final String HEALTH_HTTP_VERTICLE = "health_http_verticle.groovy";

  public VertxHttpServer() {
    super(IMAGE);
  }

  @Override
  protected void configure() {
    String version;
    if (isArm64()) {
      version = "4.4.1";
    } else version = "4.4.0";
    this.setDockerImageName(IMAGE + ":" + version);
    this.withEnv("VERTICLE_NAME", HEALTH_HTTP_VERTICLE);
    this.withEnv("VERTICLE_HOME", VERTICLE_HOME);
    this.withExposedPorts(8080);
    this.withWorkingDirectory(VERTICLE_HOME);
    this.withCopyFileToContainer(MountableFile.forClasspathResource(HEALTH_HTTP_VERTICLE), VERTICLE_HOME + "/");
    this.withCopyFileToContainer(MountableFile.forClasspathResource(STATUS_FILE), VERTICLE_HOME + "/");
    this.withCommand("vertx run " + HEALTH_HTTP_VERTICLE + " -cp " + VERTICLE_HOME + "/*");
    WaitStrategy wait = Wait.forHttp("/")
      .forStatusCode(200)
      .forPort(8080)
      .withStartupTimeout(Duration.ofSeconds(90));
    setWaitStrategy(wait);
  }

  public String address() {
    if (isMacOS()) return getContainerNetwork().getIpAddress();
    else return getContainerNetwork().getGateway();
  }

  public int port() {
    if (isMacOS()) return 8080;
    else return getMappedPort(8080);
  }

  private boolean isMacOS() {
    String osName = System.getProperty("os.name");
    return osName != null
      && (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("darwin"));
  }

  private boolean isArm64() {
    String osArch = System.getProperty("os.arch");
    return osArch != null && isMacOS() && osArch.equalsIgnoreCase("aarch64");
  }

  private ContainerNetwork getContainerNetwork() {
    return getContainerInfo()
      .getNetworkSettings()
      .getNetworks()
      .values()
      .stream()
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Container network is unknown"));
  }

  public void updateStatus(CheckStatus status) {
    int statusCode;
    switch (status) {
      case PASSING:
        statusCode = 200;
        break;
      case WARNING:
        statusCode = 429;
        break;
      default:
        statusCode = 500;
        break;
    }
    copyFileToContainer(Transferable.of(String.valueOf(statusCode)), VERTICLE_HOME + "/" + STATUS_FILE);
  }

}
