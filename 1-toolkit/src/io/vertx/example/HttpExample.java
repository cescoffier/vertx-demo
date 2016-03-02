package io.vertx.example;

import io.vertx.core.Vertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HttpExample {

  /**
   * Create vert.x
   * Create HTTP server (8080)
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.createHttpServer()
        .requestHandler(req -> {
          req.response().end("Bonjour !");
        })
        .listen(8080, ar -> {
          if (ar.failed()) {
            System.out.println("Cannot start... " + ar.cause());
          } else {
            System.out.println("Server started");
          }
        });
  }

}
