package io.vertx.demo.persistence;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class Whisky {

  private final int id;

  private String name;

  private String origin;

  public Whisky(Whisky other) {
    this.name = other.name;
    this.origin = other.origin;
    this.id = other.id;
  }

  public Whisky(String name, String origin) {
    this.name = name;
    this.origin = origin;
    this.id = -1;
  }

  public Whisky(JsonObject json) {
    this.name = json.getString("NAME");
    this.origin = json.getString("ORIGIN");
    this.id = json.getInteger("ID", -1);
  }

  public Whisky() {
    this.id = -1;
  }

  public Whisky(int id, String name, String origin) {
    this.id = id;
    this.name = name;
    this.origin = origin;
  }

  public JsonObject toJson() {
    return new JsonObject()
        .put("NAME", name)
        .put("ORIGIN", origin)
        .put("ID", id);
  }

  public String getName() {
    return name;
  }

  public String getOrigin() {
    return origin;
  }

  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }
}