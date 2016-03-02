package io.vertx.example;

import io.vertx.core.Vertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MainBlocking {

  /**
   * Create vert.x
   * Create periodic with long grace
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

  }

  private static void grace() {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
