package io.vertx.ext.consul;

/**
 * Represents an check status.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/agent/checks.html">Consul checks documentation</a>
 */
public enum CheckStatus {

  PASSING("passing"),
  WARNING("warning"),
  CRITICAL("critical");

  public final String key;

  public static CheckStatus of(String key) {
    for (CheckStatus checkStatus : values()) {
      if (checkStatus.key.equals(key)) {
        return checkStatus;
      }
    }
    return null;
  }

  CheckStatus(String key) {
    this.key = key;
  }
}
