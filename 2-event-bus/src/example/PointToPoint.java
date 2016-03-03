package example;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class PointToPoint {

  /**
   * Create vert.x
   * Create consumer on "address"
   * Periodic sending message on "address"
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    EventBus bus = vertx.eventBus();

    bus.consumer("data", message -> {
      System.out.println("Received " + message.body() + " / " + Thread.currentThread().getName());
    });

    vertx.setPeriodic(1000, l -> {
       bus.send("data", "hello");
    });
  }

}
