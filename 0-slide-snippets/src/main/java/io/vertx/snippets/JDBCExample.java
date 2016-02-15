package io.vertx.snippets;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class JDBCExample {

  Vertx vertx = Vertx.vertx();
  JDBCClient jdbc = JDBCClient.createShared(vertx, new JsonObject());

  public void connect() {
    jdbc.getConnection(ar -> {
      SQLConnection connection = ar.result();
      connection.query("SELECT * FROM Beer", resp -> {
        if (!resp.failed()) {
          List<Beer> beverages =
              resp.result().getRows().stream()
                  .map(Beer::new).collect(Collectors.toList());
        }
        //...
      });
    });
  }

  static class Beer {
    Beer() {

    }

    Beer(JsonObject json) {

    }
  }
}
