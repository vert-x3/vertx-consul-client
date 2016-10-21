package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds coordinates of servers in datacenter
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class DcCoordinates {

  private static final String DATACENTER_KEY = "Datacenter";
  private static final String COORDS_KEY = "Coordinates";

  private String dc;
  private List<Coordinate> servers;

  /**
   * Constructor from JSON
   *
   * @param coords the JSON
   */
  public DcCoordinates(JsonObject coords) {
    this.dc = coords.getString(DATACENTER_KEY);
    this.servers = coords.getJsonArray(COORDS_KEY).stream()
      .map(obj -> new Coordinate((JsonObject) obj))
      .collect(Collectors.toList());
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    return new JsonObject()
      .put(DATACENTER_KEY, dc)
      .put(COORDS_KEY, new JsonArray(servers.stream()
        .map(Coordinate::toJson)
        .collect(Collectors.toList())));
  }

  /**
   * Get datacenter
   *
   * @return datacenter
   */
  public String getDatacenter() {
    return dc;
  }

  /**
   * Get list of servers in datacenter
   *
   * @return list of servers in datacenter
   */
  public List<Coordinate> getServers() {
    return servers;
  }
}
