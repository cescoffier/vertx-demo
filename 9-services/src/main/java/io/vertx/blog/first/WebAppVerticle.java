package io.vertx.blog.first;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.demo.persistence.PersistenceService;
import io.vertx.demo.persistence.Whisky;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class WebAppVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    //-javaagent:/.../agents/jolokia-jvm.jar=port=7777,host=localhost
    Vertx.clusteredVertx(new VertxOptions()
        .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true)
            .setJmxEnabled(true)
            .setJmxDomain("vertx-http-app")), ar -> {
      if (ar.failed()) {
        System.err.println(ar.cause());
      } else {
        ar.result().deployVerticle(WebAppVerticle.class.getName());
      }
    });
  }

  private PersistenceService persistence;

  @Override
  public void start(Future<Void> fut) {
    persistence = PersistenceService.createProxy(vertx, "service.persistence");

    persistence.initialize(ar -> {
      if (ar.failed()) {
        fut.fail(ar.cause());
      } else {
        startWebApp(http -> completeStartup(http, fut));
      }
    });
  }

  private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
    // Create a router object.
    Router router = Router.router(vertx);

    router.route("/assets/*").handler(StaticHandler.create("assets"));

    router.get("/api/whiskies").handler(this::getAll);
    router.route("/api/whiskies*").handler(BodyHandler.create());
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

  private void completeStartup(AsyncResult<HttpServer> http, Future<Void> fut) {
    if (http.succeeded()) {
      System.out.println("Application started");
      fut.complete();
    } else {
      fut.fail(http.cause());
    }
  }

  private void addOne(RoutingContext routingContext) {
    final Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(),
        Whisky.class);
    persistence.addOne(whisky, r -> {
      if (r.succeeded()) {
        routingContext.response()
            .setStatusCode(201)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(Json.encodePrettily(r.result()));
      } else {
        routingContext.response()
            .setStatusCode(500)
            .end(r.cause().getMessage());
      }
    });
  }

  private void getOne(RoutingContext routingContext) {
    final String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      persistence.getOne(Integer.valueOf(id), result -> {
        if (result.succeeded()) {
          routingContext.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json; charset=utf-8")
              .end(Json.encodePrettily(result.result()));
        } else {
          routingContext.response()
              .setStatusCode(404).end();
        }
      });
    }
  }

  private void updateOne(RoutingContext routingContext) {
    final String id = routingContext.request().getParam("id");
    JsonObject json = routingContext.getBodyAsJson();
    if (id == null || json == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      persistence.updateOne(Integer.valueOf(id), new Whisky(json.getString("name"), json.getString("origin")),
          whisky -> {
            if (whisky.failed()) {
              routingContext.response().setStatusCode(404).end();
            } else {
              routingContext.response()
                  .putHeader("content-type", "application/json; charset=utf-8")
                  .end(Json.encodePrettily(whisky.result()));
            }
          });
    }
  }

  private void deleteOne(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      persistence.deleteOne(Integer.valueOf(id),
          result -> routingContext.response().setStatusCode(204).end());
    }
  }

  private void getAll(RoutingContext routingContext) {
    persistence.getAll(result -> {
      routingContext.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(result.result()));
    });
  }

}
