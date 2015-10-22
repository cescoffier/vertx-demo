import io.vertx.core.Vertx;

/**
 * HTTP Demo.
 */
public class Main {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.createHttpServer().requestHandler(req -> {
      req.response().end("Hello !!!");
    }).listen(8080);
  }
}
