package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
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

  public static CheckOptions script(String script, String interval) {
    CheckOptions checkOptions = new CheckOptions();
    checkOptions.script = script;
    checkOptions.interval = interval;
    return checkOptions;
  }

  public static CheckOptions http(String http, String interval) {
    CheckOptions checkOptions = new CheckOptions();
    checkOptions.http = http;
    checkOptions.interval = interval;
    return checkOptions;
  }

  public static CheckOptions ttl(String ttl) {
    CheckOptions checkOptions = new CheckOptions();
    checkOptions.ttl = ttl;
    return checkOptions;
  }

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

  public CheckOptions() {
  }

  public CheckOptions(JsonObject jsonObject) {
    this.id = jsonObject.getString(ID_KEY);
    this.name = jsonObject.getString(NAME_KEY);
    this.script = jsonObject.getString(SCRIPT_KEY);
    this.http = jsonObject.getString(HTTP_KEY);
    this.ttl = jsonObject.getString(TTL_KEY);
    this.tcp = jsonObject.getString(TCP_KEY);
    this.interval = jsonObject.getString(INTERVAL_KEY);
    this.note = jsonObject.getString(NOTE_KEY);
  }

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

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getNote() {
    return note;
  }

  public CheckOptions setId(String id) {
    this.id = id;
    return this;
  }

  public CheckOptions setName(String name) {
    this.name = name;
    return this;
  }

  public CheckOptions setNote(String note) {
    this.note = note;
    return this;
  }
}
