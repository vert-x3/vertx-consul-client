package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Check {

  private static final String SERVICE_ID_KEY = "ServiceID";
  private static final String SERVICE_NAME_KEY = "ServiceName";
  private static final String ID_KEY = "CheckID";
  private static final String NAME_KEY = "Name";
  private static final String STATUS_KEY = "Status";
  private static final String NOTES_KEY = "Notes";
  private static final String OUTPUT_KEY = "Output";

  private String id;
  private String name;
  private CheckStatus status;
  private String notes;
  private String output;
  private String serviceId;
  private String serviceName;

  /**
   * Constructor from JSON
   *
   * @param check the JSON
   */
  public Check(JsonObject check) {
    this.id = check.getString(ID_KEY);
    this.name = check.getString(NAME_KEY);
    this.status = CheckStatus.of(check.getString(STATUS_KEY));
    this.notes = check.getString(NOTES_KEY);
    this.output = check.getString(OUTPUT_KEY);
    this.serviceId = check.getString(SERVICE_ID_KEY);
    this.serviceName = check.getString(SERVICE_NAME_KEY);
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
    if (status != null) {
      jsonObject.put(STATUS_KEY, status.key);
    }
    if (notes != null) {
      jsonObject.put(NOTES_KEY, notes);
    }
    if (output != null) {
      jsonObject.put(OUTPUT_KEY, output);
    }
    if (serviceId != null) {
      jsonObject.put(SERVICE_ID_KEY, serviceId);
    }
    if (serviceName != null) {
      jsonObject.put(SERVICE_NAME_KEY, serviceName);
    }
    return jsonObject;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public CheckStatus getStatus() {
    return status;
  }

  public String getNotes() {
    return notes;
  }

  public String getOutput() {
    return output;
  }

  public String getServiceId() {
    return serviceId;
  }

  public String getServiceName() {
    return serviceName;
  }
}
