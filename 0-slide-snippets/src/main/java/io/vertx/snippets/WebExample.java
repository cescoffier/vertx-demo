package io.vertx.snippets;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class WebExample {

  Vertx vertx = Vertx.vertx();

  public void router() {
    Router router = Router.router(vertx);
    router.route("/assets/*").handler(StaticHandler.create("assets"));
    router.get("/api/whiskies").handler(this::getAll);
    router.route("/api/whiskies*").handler(BodyHandler.create());
    router.post("/api/whiskies").handler(this::addOne);
    router.put("/api/whiskies/:id").handler(this::updateOne);
    router.delete("/api/whiskies/:id").handler(this::deleteOne);
    //...
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }

  private void deleteOne(RoutingContext routingContext) {

  }

  private void updateOne(RoutingContext routingContext) {

  }

  private void addOne(RoutingContext routingContext) {

  }

  private void getAll(RoutingContext routingContext) {

  }
}
