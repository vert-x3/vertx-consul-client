package io.vertx.ext.consul.connect;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

@DataObject
public class ExposeOptions {
  private static final String PATHS = "Paths";

  private List<ExposePathOptions> paths;

  /**
   * Default constructor
   */
  public ExposeOptions() {
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public ExposeOptions(JsonObject options) {
    this.paths = options.getJsonArray(PATHS).stream()
      .map(o -> new ExposePathOptions((JsonObject) o))
      .collect(Collectors.toList());
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put(PATHS, paths.stream().map(ExposePathOptions::toJson).collect(Collectors.toList()));
    return jsonObject;
  }

  public List<ExposePathOptions> getPaths() {
    return paths;
  }

  public ExposeOptions setPaths(List<ExposePathOptions> paths) {
    this.paths = paths;
    return this;
  }
}
