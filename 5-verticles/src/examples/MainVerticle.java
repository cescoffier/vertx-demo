package examples;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    Vertx.vertx()
        .deployVerticle(MainVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    vertx.deployVerticle("examples.HttpVerticle");
  }
}
