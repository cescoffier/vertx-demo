package example;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Receiver {

  /**
   * Create clustered vert.x
   * Register consumer on "data"
   */
  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), result -> {
      Vertx vertx = result.result();
      vertx.eventBus().consumer("data", message -> {
        System.out.println(message.body());
      });
    });
  }

}
