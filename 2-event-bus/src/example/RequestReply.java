package example;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

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
  }

}
