package io.vertx.ext.consul.vertx;

import com.github.dockerjava.api.model.ContainerNetwork;
import io.vertx.ext.consul.CheckStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.MountableFile;

public class VertxHttpServer extends GenericContainer<VertxHttpServer> {
  private static String IMAGE = "vertx/vertx3";
  private final String VERTICLE_HOME = "/usr/verticles";
  private final String STATUS_FILE = "health_status.txt";
  private final String HEALTH_HTTP_VERTICLE = "health_http_verticale.groovy";

  public VertxHttpServer() {
    super(IMAGE);
  }

  @Override
  protected void configure() {
    addEnv("VERTICLE_NAME", HEALTH_HTTP_VERTICLE);
    addEnv("VERTICLE_HOME", VERTICLE_HOME);
    addExposedPort(8080);
    this.withWorkingDirectory(VERTICLE_HOME);
    this.withCopyFileToContainer(MountableFile.forClasspathResource(HEALTH_HTTP_VERTICLE), VERTICLE_HOME + "/");
    this.withCopyFileToContainer(MountableFile.forClasspathResource(STATUS_FILE), VERTICLE_HOME + "/");
    this.withCommand("vertx run " + HEALTH_HTTP_VERTICLE + " -cp " + VERTICLE_HOME + "/*");
    WaitStrategy wait = Wait.forHttp("/")
      .forStatusCode(200)
      .forPort(8080);
    setWaitStrategy(wait);
  }

  public String address() {
    return getContainerNetwork().getIpAddress();
  }

  public String gateway() {
    return getContainerNetwork().getGateway();
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
