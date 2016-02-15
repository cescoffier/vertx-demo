package examples;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Bare {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions().setClustered(true).setHAEnabled(true), result -> {

    });
  }
}
