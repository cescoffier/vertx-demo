package io.vertx.snippets;

import io.vertx.core.Vertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class AsyncDevModel2 {

  Vertx vertx = Vertx.vertx();


  public void start() {
    vertx.createHttpServer()
        .requestHandler(req -> {
          req.response().end("Hello, my name is Steem");
        })
        .listen(ar -> {
          if (ar.failed()) {
            System.err.println("D'oh! I cannot start the HTTP server : " + ar.cause().getMessage());
          } else {
            System.out.println("HTTP server started " + ar.result());
          }
        });
  }

}
