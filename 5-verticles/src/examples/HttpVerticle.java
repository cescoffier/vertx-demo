package examples;

import io.vertx.core.AbstractVerticle;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HttpVerticle extends AbstractVerticle {

  public HttpVerticle() {
    System.out.println("Creating HTTP server");
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Served from " + Thread.currentThread().getName()))
        .listen(8080);
  }
}
