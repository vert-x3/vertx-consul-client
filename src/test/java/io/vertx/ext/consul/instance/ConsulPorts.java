package io.vertx.ext.consul.instance;

import java.util.HashMap;
import java.util.Map;

public class ConsulPorts {
  private final Map<ConsulPort, Integer> ports;

  private ConsulPorts(Map<ConsulPort, Integer> ports) {
    this.ports = ports;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Integer getValue(ConsulPort port) {
    return ports.get(port);
  }

  public Integer[] getValues() {
    return ports.values().toArray(new Integer[0]);
  }

  public static class Builder {
    private final Map<ConsulPort, Integer> ports = new HashMap<>();

    public Builder withPort(ConsulPort port, int value) {
      ports.put(port, value);
      return this;
    }

    public ConsulPorts build() {
      for (ConsulPort port : ConsulPort.values()) {
        ports.putIfAbsent(port, port.getDefaultValue());
      }
      return new ConsulPorts(ports);
    }
  }

  @Override
  public String toString() {
    return ports.entrySet().toString();
  }

  public enum ConsulPort {
    DNS(8600),
    HTTP(8500),
    HTTPS(8501),
    SERF_LAN(8301),
    SERF_WAN(8302),
    RPC(8300);

    private final int defaultValue;

    ConsulPort(int defaultValue) {
      this.defaultValue = defaultValue;
    }

    public int getDefaultValue() {
      return defaultValue;
    }
  }
}
