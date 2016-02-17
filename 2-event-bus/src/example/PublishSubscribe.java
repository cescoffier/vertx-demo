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

    bus.consumer("address", message -> {
      System.out.println("Message received by A: " + message.body() + " (" + Thread.currentThread().getName() + ")");
    });

    bus.consumer("address", message -> {
      System.out.println("Message received by B : " + message.body() + " (" + Thread.currentThread().getName() + ")");
    });

    vertx.setPeriodic(1000, l -> {
      bus.publish("address", "hello");
    });
  }

}
