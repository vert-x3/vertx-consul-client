package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Options used to register service.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class ServiceOptions {

  private static final String SERVICE_ID = "ID";
  private static final String SERVICE_NAME = "Name";
  private static final String SERVICE_TAGS = "Tags";
  private static final String SERVICE_ADDRESS = "Address";
  private static final String SERVICE_PORT = "Port";
  private static final String SERVICE_CHECK = "Check";

  private String id;
  private String name;
  private List<String> tags;
  private String address;
  private int port;
  private CheckOptions checkOptions;

  /**
   * Default constructor
   */
  public ServiceOptions() {
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public ServiceOptions(ServiceOptions options) {
    this.id = options.id;
    this.name = options.name;
    this.tags = options.tags;
    this.address = options.address;
    this.port = options.port;
    this.checkOptions = options.checkOptions;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public ServiceOptions(JsonObject options) {
    this.id = options.getString(SERVICE_ID);
    this.name = options.getString(SERVICE_NAME);
    JsonArray tagsArr = options.getJsonArray(SERVICE_TAGS);
    this.tags = tagsArr == null ? null : tagsArr.getList();
    this.address = options.getString(SERVICE_ADDRESS);
    this.port = options.getInteger(SERVICE_PORT, 0);
    JsonObject checkObj = options.getJsonObject(SERVICE_CHECK);
    this.checkOptions = checkObj == null ? null : new CheckOptions(checkObj);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (id != null) {
      jsonObject.put(SERVICE_ID, id);
    }
    if (name != null) {
      jsonObject.put(SERVICE_NAME, name);
    }
    if (tags != null) {
      jsonObject.put(SERVICE_TAGS, new JsonArray(tags));
    }
    if (address != null) {
      jsonObject.put(SERVICE_ADDRESS, address);
    }
    if (port != 0) {
      jsonObject.put(SERVICE_PORT, port);
    }
    if (checkOptions != null) {
      jsonObject.put(SERVICE_CHECK, checkOptions.toJson());
    }
    return jsonObject;
  }

  /**
   * Get the ID of session
   *
   * @return the ID of session
   */
  public String getId() {
    return id;
  }

  /**
   * Set the ID of session
   *
   * @param id the ID of session
   * @return reference to this, for fluency
   */
  public ServiceOptions setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get service name
   *
   * @return service name
   */
  public String getName() {
    return name;
  }

  /**
   * Set service name
   *
   * @param name service name
   * @return reference to this, for fluency
   */
  public ServiceOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get list of tags associated with service
   *
   * @return list of tags
   */
  public List<String> getTags() {
    return tags;
  }

  /**
   * Set list of tags associated with service
   *
   * @param tags list of tags
   * @return reference to this, for fluency
   */
  public ServiceOptions setTags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  /**
   * Get service address
   *
   * @return service address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set service address
   *
   * @param address service address
   * @return reference to this, for fluency
   */
  public ServiceOptions setAddress(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get service port
   *
   * @return service port
   */
  public int getPort() {
    return port;
  }

  /**
   * Set service port
   *
   * @param port service port
   * @return reference to this, for fluency
   */
  public ServiceOptions setPort(int port) {
    this.port = port;
    return this;
  }

  /**
   * Get check options of service
   *
   * @return check options
   */
  public CheckOptions getCheckOptions() {
    return checkOptions;
  }

  /**
   * Set check options of service
   *
   * @param checkOptions check options
   * @return reference to this, for fluency
   */
  public ServiceOptions setCheckOptions(CheckOptions checkOptions) {
    this.checkOptions = checkOptions;
    return this;
  }
}
