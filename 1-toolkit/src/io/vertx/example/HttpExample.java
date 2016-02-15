package io.vertx.example;

import io.vertx.core.Vertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HttpExample {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.createHttpServer()
        .requestHandler(
          request -> request.response().end("Hello, My name is Steem !")
        )
        .listen(8080, result -> {
          if (result.failed()) {
            System.err.println("D'oh ! something bad happened while starting the HTTP server : " + result.cause()
                .getMessage());
          } else {
            System.out.println("HTTP server started");
          }
        });
  }

}
