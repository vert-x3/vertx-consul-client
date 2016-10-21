package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Holds network coordinates of node
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/internals/coordinates.html">Network coordinates</a>
 */
@DataObject
public class Coordinate {

  private static final String NODE_KEY = "Node";
  private static final String COORD_KEY = "Coord";
  private static final String ADJ_KEY = "Adjustment";
  private static final String ERR_KEY = "Error";
  private static final String HEIGHT_KEY = "Height";
  private static final String VEC_KEY = "Vec";

  private String node;
  private float adj;
  private float err;
  private float height;
  private List<Float> vec;

  /**
   * Copy constructor
   *
   * @param coordinate the one to copy
   */
  public Coordinate(Coordinate coordinate) {
    this.node = coordinate.node;
    this.adj = coordinate.adj;
    this.err = coordinate.err;
    this.height = coordinate.height;
    this.vec = coordinate.vec;
  }

  /**
   * Constructor from JSON
   *
   * @param coordinate the JSON
   */
  public Coordinate(JsonObject coordinate) {
    this.node = coordinate.getString(NODE_KEY);
    JsonObject coord = coordinate.getJsonObject(COORD_KEY);
    if (coord != null) {
      this.adj = coord.getFloat(ADJ_KEY, 0f);
      this.err = coord.getFloat(ERR_KEY, 0f);
      this.height = coord.getFloat(HEIGHT_KEY, 0f);
      JsonArray arr = coord.getJsonArray(VEC_KEY);
      this.vec = arr == null ? null : arr.getList();
    }
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (node != null) {
      jsonObject.put(NODE_KEY, node);
    }
    JsonObject coord = new JsonObject();
    if (adj != 0) {
      coord.put(ADJ_KEY, adj);
    }
    if (err != 0) {
      coord.put(ERR_KEY, err);
    }
    if (height != 0) {
      coord.put(HEIGHT_KEY, height);
    }
    if (vec != null) {
      coord.put(VEC_KEY, vec);
    }
    if (!coord.isEmpty()) {
      jsonObject.put(COORD_KEY, coord);
    }
    return jsonObject;
  }

  /**
   * Get name of node
   *
   * @return name of node
   */
  public String getNode() {
    return node;
  }

  public float getAdj() {
    return adj;
  }

  public float getErr() {
    return err;
  }

  public float getHeight() {
    return height;
  }

  public List<Float> getVec() {
    return vec;
  }
}
