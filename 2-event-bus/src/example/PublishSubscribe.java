package example;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.awt.*;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class PublishSubscribe {

  /**
   * Create vert.x
   * Create 2 consumers (A amd B) on "address"
   * Periodic public message on "address"
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    EventBus bus = vertx.eventBus();

    bus.consumer("data", message -> {
      System.out.println("A Received " + message.body());
    });

    bus.consumer("data", message -> {
      System.out.println("B Received " + message.body());
    });

    vertx.setPeriodic(1000, l -> {
      bus.publish("data", "hello");
    });
  }

}
