package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a verticle. A verticle is a _Vert.x component_. This verticle is implemented in Java, but you can
 * implement them in JavaScript, Groovy or even Ruby.
 */
public class WebAppVerticle extends AbstractVerticle {

  private JDBCClient jdbc;

  @Override
  public void start(Future<Void> fut) {
    jdbc = JDBCClient.createShared(vertx, config(), "My-Whisky-Collection");

    startBackend(
        (connection) -> createSomeData(connection,
            (nothing) -> startWebApp(
                (http) -> completeStartup(http, fut)
            ), fut
        ), fut);
  }

  private void startBackend(Handler<AsyncResult<SQLConnection>> next, Future<Void> fut) {
    jdbc.getConnection(ar -> {
      if (ar.failed()) {
        fut.fail(ar.cause());
      } else {
        next.handle(Future.succeededFuture(ar.result()));
      }
    });
  }

  private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
    // Create a router object.
    Router router = Router.router(vertx);

    router.route("/assets/*")
        .handler(StaticHandler.create("assets"));

    router.get("/api/whiskies")
        .handler(this::getAll);
    router.route("/api/whiskies*")
        .handler(BodyHandler.create());

    router.post("/api/whiskies").handler(this::addOne);
    router.get("/api/whiskies/:id").handler(this::getOne);
    router.put("/api/whiskies/:id").handler(this::updateOne);
    router.delete("/api/whiskies/:id").handler(this::deleteOne);


    // Create the HTTP server and pass the "accept" method to the request handler.
    vertx
        .createHttpServer()
        .requestHandler(router::accept)
        .listen(
            // Retrieve the port from the configuration,
            // default to 8080.
            config().getInteger("http.port", 8080),
            next::handle
        );
  }

  private void completeStartup(AsyncResult<HttpServer> http,
                               Future<Void> fut) {
    if (http.succeeded()) {
      System.out.println("Application started");
      fut.complete();
    } else {
      fut.fail(http.cause());
    }
  }


  @Override
  public void stop() throws Exception {
    jdbc.close();
  }

  private void addOne(RoutingContext routingContext) {
    jdbc.getConnection(ar -> {
      final Whisky whisky =
          Json.decodeValue(routingContext.getBodyAsString(),
          Whisky.class);

      SQLConnection connection = ar.result();
      insert(whisky, connection, (r) ->
          routingContext.response()
              .setStatusCode(201)
              .putHeader("content-type",
                  "application/json; charset=utf-8")
              .end(Json.encodePrettily(r.result())));
    });

  }

  private void getOne(RoutingContext routingContext) {
    final String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      jdbc.getConnection(ar -> {
        SQLConnection connection = ar.result();
        select(id, connection, result -> {
          if (result.succeeded()) {
            routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type",
                    "application/json; charset=utf-8")
                .end(Json.encodePrettily(result.result()));
          } else {
            routingContext.response()
                .setStatusCode(404).end();
          }
        });
      });
    }
  }

  private void updateOne(RoutingContext routingContext) {
    final String id = routingContext.request().getParam("id");
    JsonObject json = routingContext.getBodyAsJson();
    if (id == null || json == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      jdbc.getConnection(ar ->
          update(id, json, ar.result(),
              (whisky) -> {
            if (whisky.failed()) {
              routingContext.response().setStatusCode(404)
                  .end();
            } else {
              routingContext.response()
                  .putHeader("content-type",
                      "application/json; charset=utf-8")
                  .end(Json.encodePrettily(whisky.result()));
            }
          })
      );
    }
  }

  private void deleteOne(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      jdbc.getConnection(ar -> {
        SQLConnection connection = ar.result();
        connection
            .execute("DELETE FROM Whisky WHERE id='" + id + "'",
            result -> routingContext.response()
                .setStatusCode(204).end());
      });
    }
  }

  private void getAll(RoutingContext routingContext) {
    jdbc.getConnection(ar -> {
      SQLConnection connection = ar.result();
      connection.query("SELECT * FROM Whisky", result -> {
        List<Whisky> whiskies =
            result.result().getRows().stream()
                .map(Whisky::new).collect(Collectors.toList());
        routingContext.response()
            .putHeader("content-type",
                "application/json; charset=utf-8")
            .end(Json.encodePrettily(whiskies));
      });
    });
  }

  private void createSomeData(AsyncResult<SQLConnection> result, Handler<AsyncResult<Void>> next, Future<Void> fut) {
    if (result.failed()) {
      fut.fail(result.cause());
    } else {
      SQLConnection connection = result.result();
      connection.execute(
          "CREATE TABLE IF NOT EXISTS Whisky (id INTEGER IDENTITY, name varchar(100), origin varchar" +
              "(100))",
          ar -> {
            if (ar.failed()) {
              connection.close();
              fut.fail(ar.cause());
              return;
            }
            connection.query("SELECT * FROM Whisky",
                select -> {
              if (select.failed()) {
                connection.close();
                fut.fail(ar.cause());
                return;
              }
              if (select.result().getNumRows() == 0) {
                insert(
                    new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay"), connection,
                    (v) -> insert(new Whisky("Talisker 57° North", "Scotland, Island"), connection,
                        (r) -> next.handle(Future.<Void>succeededFuture())));
              } else {
                connection.close();
                next.handle(Future.<Void>succeededFuture());
              }
            });

          });
    }
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

  private void select(String id, SQLConnection connection, Handler<AsyncResult<Whisky>> resultHandler) {
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

  private void update(String id, JsonObject content, SQLConnection connection,
                      Handler<AsyncResult<Whisky>> resultHandler) {
    String sql = "UPDATE Whisky SET name=?, origin=? WHERE id=?";
    connection.updateWithParams(sql,
        new JsonArray().add(content.getString("name")).add(content.getString("origin")).add(id),
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
              Future.succeededFuture(new Whisky(Integer.valueOf(id),
                  content.getString("name"), content.getString("origin"))));
          connection.close();
        });
  }

}
