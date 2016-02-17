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
        .requestHandler(request -> {
          request.response().end("Bonsoir !");
        })
        .listen(8080, ar -> {
          if (ar.failed()) {
            System.err.println("D'oh ! Can't start it : " + ar.cause().getMessage());
          } else {
            System.out.println("Started : " + ar.result());
          }
        });
  }

}
