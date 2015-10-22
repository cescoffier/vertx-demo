package io.vertx.demo.persistence.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.demo.persistence.PersistenceService;
import io.vertx.demo.persistence.Whisky;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class PersistenceServiceImpl implements PersistenceService {

  private final JDBCClient jdbc;

  public PersistenceServiceImpl(Vertx vertx, JsonObject config) {
    this.jdbc = JDBCClient.createShared(vertx, config, "My-Whisky-Collection");
  }

  @Override
  public void initialize(Handler<AsyncResult<Void>> completion) {
    jdbc.getConnection(ar -> {
      if (ar.failed()) {
        completion.handle(Future.failedFuture(ar.cause()));
      } else {
        createSomeData(ar.result(), completion);
      }
    });
  }

  @Override
  public void getAll(Handler<AsyncResult<List<Whisky>>> result) {
    jdbc.getConnection(ar -> {
      SQLConnection connection = ar.result();
      connection.query("SELECT * FROM Whisky", selection -> {
        List<Whisky> whiskies = selection.result().getRows().stream().map(Whisky::new).collect(Collectors.toList());
        result.handle(Future.succeededFuture(whiskies));
      });
    });
  }

  @Override
  public void getOne(int id, Handler<AsyncResult<Whisky>> result) {
    jdbc.getConnection(ar -> {
      if (ar.failed()) {
        result.handle(Future.failedFuture(ar.cause()));
      } else {
        select(id, ar.result(), result);
      }
    });
  }

  @Override
  public void addOne(Whisky whisky, Handler<AsyncResult<Whisky>> result) {
    jdbc.getConnection(ar -> {
      if (ar.failed()) {
        result.handle(Future.failedFuture(ar.cause()));
      } else {
        insert(whisky, ar.result(), result);
      }
    });
  }

  @Override
  public void updateOne(int id, Whisky newValues, Handler<AsyncResult<Whisky>> result) {
    jdbc.getConnection(ar -> {
      if (ar.failed()) {
        result.handle(Future.failedFuture(ar.cause()));
      } else {
        update(id, newValues.toJson(), ar.result(), result);
      }
    });
  }

  @Override
  public void deleteOne(int id, Handler<AsyncResult<Void>> result) {
    jdbc.getConnection(ar -> {
      if (ar.failed()) {
        result.handle(Future.failedFuture(ar.cause()));
      } else {
        ar.result().execute("DELETE FROM Whisky WHERE id='" + id + "'", result);
      }
    });
  }

  private void createSomeData(SQLConnection connection, Handler<AsyncResult<Void>> completion) {
    connection.execute(
        "CREATE TABLE IF NOT EXISTS Whisky (id INTEGER IDENTITY, name varchar(100), origin varchar" +
            "(100))",
        ar -> {
          if (ar.failed()) {
            connection.close();
            completion.handle(Future.failedFuture(ar.cause()));
            return;
          }

          connection.query("SELECT * FROM Whisky", select -> {
            if (select.failed()) {
              connection.close();
              completion.handle(Future.failedFuture(ar.cause()));
              return;
            }

            if (select.result().getNumRows() == 0) {
              insert(
                  new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay"), connection,
                  (v) -> insert(new Whisky("Talisker 57° North", "Scotland, Island"), connection,
                      (r) -> completion.handle(Future.<Void>succeededFuture())));
            } else {
              connection.close();
              completion.handle(Future.<Void>succeededFuture());
            }

          });

        });
  }

  private void insert(Whisky whisky, SQLConnection connection, Handler<AsyncResult<Whisky>> next) {
    String sql = "INSERT INTO Whisky (name, origin) VALUES ?, ?";
    connection.updateWithParams(sql,
        new JsonArray().add(whisky.getName()).add(whisky.getOrigin()),
        (ar) -> {
          if (ar.failed()) {
            next.handle(Future.failedFuture(ar.cause()));
            return;
          }
          UpdateResult result = ar.result();
          // Build a new whisky instance with the generated id.
          Whisky w = new Whisky(result.getKeys().getInteger(0), whisky.getName(), whisky.getOrigin());
          next.handle(Future.succeededFuture(w));
          connection.close();
        });
  }

  private void select(int id, SQLConnection connection, Handler<AsyncResult<Whisky>> resultHandler) {
    connection.queryWithParams("SELECT * FROM Whisky WHERE id=?", new JsonArray().add(id), ar -> {
      if (ar.failed()) {
        resultHandler.handle(Future.failedFuture("Whisky not found"));
      } else {
        if (ar.result().getNumRows() >= 1) {
          resultHandler.handle(Future.succeededFuture(new Whisky(ar.result().getRows().get(0))));
        } else {
          resultHandler.handle(Future.failedFuture("Whisky not found"));
        }
      }
    });
  }

  private void update(int id, JsonObject content, SQLConnection connection,
                      Handler<AsyncResult<Whisky>> resultHandler) {
    String sql = "UPDATE Whisky SET name=?, origin=? WHERE id=?";
    connection.updateWithParams(sql,
        new JsonArray().add(content.getString("NAME")).add(content.getString("ORIGIN")).add(id),
        update -> {
          if (update.failed()) {
            resultHandler.handle(Future.failedFuture("Cannot update the whisky"));
            return;
          }
          if (update.result().getUpdated() == 0) {
            resultHandler.handle(Future.failedFuture("Whisky not found"));
            return;
          }
          resultHandler.handle(
              Future.succeededFuture(new Whisky(id,
                  content.getString("name"), content.getString("origin"))));
          connection.close();
        });
  }
}
