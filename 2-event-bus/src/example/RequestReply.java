package example;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.awt.*;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RequestReply {

  /**
   * Create vert.x
   * Create consumer on "address" reploying
   * Periodic sending message on "address" with reply handler
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    EventBus bus = vertx.eventBus();

    bus.consumer("data", message -> {
      message.reply("pong");
    });

    vertx.setPeriodic(1000, l -> {
      bus.send("data", "ping", ar -> {
        System.out.println(">> " + ar.result().body());
      });
    });

  }

}
