package example;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

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

  }

}
