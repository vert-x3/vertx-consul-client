package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Options used to register checks in Consul.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class CheckOptions {

  private static final String ID_KEY = "ID";
  private static final String NAME_KEY = "Name";
  private static final String NOTE_KEY = "Note";
  private static final String SCRIPT_KEY = "Script";
  private static final String HTTP_KEY = "HTTP";
  private static final String INTERVAL_KEY = "Interval";
  private static final String TTL_KEY = "TTL";
  private static final String TCP_KEY = "TCP";

  /**
   * Create check option for Script+Interval check
   *
   * @param script path to script
   * @param interval checking interval in Go's time format which is sequence of decimal numbers,
   *                 each with optional fraction and a unit suffix, such as "300ms", "-1.5h" or "2h45m".
   *                 Valid time units are "ns", "us" (or "µs"), "ms", "s", "m", "h"
   * @return a new CheckOption object
   */
  public static CheckOptions script(String script, String interval) {
    CheckOptions checkOptions = new CheckOptions();
    checkOptions.script = script;
    checkOptions.interval = interval;
    return checkOptions;
  }

  /**
   * Create check option for HTTP+Interval check
   *
   * @param http check HTTP address
   * @param interval checking interval in Go's time format which is sequence of decimal numbers,
   *                 each with optional fraction and a unit suffix, such as "300ms", "-1.5h" or "2h45m".
   *                 Valid time units are "ns", "us" (or "µs"), "ms", "s", "m", "h"
   * @return a new CheckOption object
   */
  public static CheckOptions http(String http, String interval) {
    CheckOptions checkOptions = new CheckOptions();
    checkOptions.http = http;
    checkOptions.interval = interval;
    return checkOptions;
  }

  /**
   * Create check option for Time to Live (TTL) check
   *
   * @param ttl TTL interval in Go's time format which is sequence of decimal numbers,
   *                 each with optional fraction and a unit suffix, such as "300ms", "-1.5h" or "2h45m".
   *                 Valid time units are "ns", "us" (or "µs"), "ms", "s", "m", "h"
   * @return a new CheckOption object
   */
  public static CheckOptions ttl(String ttl) {
    CheckOptions checkOptions = new CheckOptions();
    checkOptions.ttl = ttl;
    return checkOptions;
  }

  /**
   * Create check option for TCP+Interval check
   *
   * @param tcp check TCP address
   * @param interval checking interval in Go's time format which is sequence of decimal numbers,
   *                 each with optional fraction and a unit suffix, such as "300ms", "-1.5h" or "2h45m".
   *                 Valid time units are "ns", "us" (or "µs"), "ms", "s", "m", "h"
   * @return a new CheckOption object
   */
  public static CheckOptions tcp(String tcp, String interval) {
    CheckOptions checkOptions = new CheckOptions();
    checkOptions.tcp = tcp;
    checkOptions.interval = interval;
    return checkOptions;
  }

  private String id;
  private String name;
  private String script;
  private String http;
  private String ttl;
  private String tcp;
  private String interval;
  private String note;

  private CheckOptions() {}

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public CheckOptions(CheckOptions options) {
    this.id = options.id;
    this.name = options.name;
    this.script = options.script;
    this.http = options.http;
    this.ttl = options.ttl;
    this.tcp = options.tcp;
    this.interval = options.interval;
    this.note = options.note;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public CheckOptions(JsonObject options) {
    this.id = options.getString(ID_KEY);
    this.name = options.getString(NAME_KEY);
    this.script = options.getString(SCRIPT_KEY);
    this.http = options.getString(HTTP_KEY);
    this.ttl = options.getString(TTL_KEY);
    this.tcp = options.getString(TCP_KEY);
    this.interval = options.getString(INTERVAL_KEY);
    this.note = options.getString(NOTE_KEY);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (id != null) {
      jsonObject.put(ID_KEY, id);
    }
    if (name != null) {
      jsonObject.put(NAME_KEY, name);
    }
    if (note != null) {
      jsonObject.put(NOTE_KEY, note);
    }
    if (script != null) {
      jsonObject.put(SCRIPT_KEY, script);
    }
    if (http != null) {
      jsonObject.put(HTTP_KEY, http);
    }
    if (ttl != null) {
      jsonObject.put(TTL_KEY, ttl);
    }
    if (tcp != null) {
      jsonObject.put(TCP_KEY, tcp);
    }
    if (interval != null) {
      jsonObject.put(INTERVAL_KEY, interval);
    }
    return jsonObject;
  }

  /**
   * Get check ID
   *
   * @return check ID
   */
  public String getId() {
    return id;
  }

  /**
   * Set check ID
   *
   * @param id check ID
   * @return reference to this, for fluency
   */
  public CheckOptions setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get check name
   *
   * @return check name
   */
  public String getName() {
    return name;
  }

  /**
   * Set check name
   *
   * @param name check name
   * @return reference to this, for fluency
   */
  public CheckOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get check note
   *
   * @return check note
   */
  public String getNote() {
    return note;
  }

  /**
   * Set check note
   *
   * @param note check note
   * @return reference to this, for fluency
   */
  public CheckOptions setNote(String note) {
    this.note = note;
    return this;
  }
}
