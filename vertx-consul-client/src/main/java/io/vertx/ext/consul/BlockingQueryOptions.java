package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Options used to perform blocking query that used to wait for a potential change using long polling.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/agent/http.html">Blocking Queries documentation</a>
 */
@DataObject
public class BlockingQueryOptions {

  private static final String INDEX_KEY = "index";
  private static final String WAIT_KEY = "wait";

  private long index;
  private String wait;

  /**
   * Default constructor
   */
  public BlockingQueryOptions() {}

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public BlockingQueryOptions(BlockingQueryOptions options) {
    this.index = options.index;
    this.wait = options.wait;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public BlockingQueryOptions(JsonObject options) {
    this.index = options.getLong(INDEX_KEY, 0L);
    this.wait = options.getString(WAIT_KEY);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (index > 0) {
      jsonObject.put(INDEX_KEY, index);
    }
    if (wait != null) {
      jsonObject.put(WAIT_KEY, wait);
    }
    return jsonObject;
  }

  /**
   * Get index
   *
   * @return the index
   */
  public long getIndex() {
    return index;
  }

  /**
   * Set index indicating that the client wishes to wait for any changes subsequent to that index.
   *
   * @param index the index
   * @return reference to this, for fluency
   */
  public BlockingQueryOptions setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Get wait period
   *
   * @return wait period
   */
  public String getWait() {
    return wait;
  }

  /**
   * Specifying a maximum duration for the blocking request. This is limited to 10 minutes.
   * If not set, the wait time defaults to 5 minutes. This value can be specified in the form of "10s" or "5m"
   * (i.e., 10 seconds or 5 minutes, respectively).
   *
   * @param wait wait period
   * @return reference to this, for fluency
   */
  public BlockingQueryOptions setWait(String wait) {
    this.wait = wait;
    return this;
  }
}
