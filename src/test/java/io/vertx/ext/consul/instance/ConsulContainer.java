package io.vertx.ext.consul.instance;

import com.github.dockerjava.api.model.ContainerNetwork;
import io.vertx.ext.consul.dc.ConsulDatacenter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

public class ConsulContainer extends GenericContainer<ConsulContainer> {
  private static final String HEALTH_CHECK_PATH = "/v1/status/leader";

  private final ConsulPorts exposedPorts;
  private final ConsulDatacenter dc;

  private final String image;
  private final String version;
  private final String nodeName;

  public ConsulContainer(String image, String version, ConsulPorts exposedPorts, ConsulDatacenter dc, String nodeName) {
    super(image + ":" + version);
    this.exposedPorts = exposedPorts;
    this.dc = dc;
    this.image = image;
    this.version = version;
    this.nodeName = nodeName;
  }

  @Override
  protected void configure() {
    WaitStrategy wait = Wait.forHttp(HEALTH_CHECK_PATH)
      .forStatusCode(200)
      .forPort(ConsulPorts.ConsulPort.HTTP.getDefaultValue());
    setWaitStrategy(wait);
  }

  public String address() {
    return getContainerNetwork().getIpAddress();
  }

  public String gateway() {
    return getContainerNetwork().getGateway();
  }

  public ConsulPorts exposedPorts(){return exposedPorts;}

  public ConsulDatacenter datacenter(){return dc;}

  public String getVersion(){return version;}

  public String getNodeName(){return nodeName;}

  private ContainerNetwork getContainerNetwork() {
    return getContainerInfo()
      .getNetworkSettings()
      .getNetworks()
      .values()
      .stream()
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Container network is unknown"));
  }
}
