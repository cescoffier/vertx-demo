package io.vertx.example;

import io.vertx.core.Vertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Main {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.setPeriodic(1000, l -> {
      System.out.println(Thread.currentThread().getName() + " >> called");
    });
  }

}
