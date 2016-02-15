package example;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Receiver {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), result -> {
      if (result.failed()) {
        System.err.println("Cannot create a clustered vert.x : " + result.cause());
      } else {
        Vertx vertx = result.result();
        vertx.eventBus().consumer("data", message -> {
           System.out.println("Data received : " + message.body());
        });
      }
    });
  }

}
