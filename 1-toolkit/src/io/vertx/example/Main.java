package io.vertx.example;

import io.vertx.core.Vertx;

public class Main {

  /**
   * Create vertx
   * Set periodic
   * Dump thread name
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.setPeriodic(1000, l -> {
       System.out.println("Hello " + Thread.currentThread().getName());
    });
  }

}
