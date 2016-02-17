package examples;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MainWithSeveralInstances {

  public static void main(String[] args) {
    Vertx.vertx()
        .deployVerticle(HttpVerticle.class.getName(),
            new DeploymentOptions().setInstances(3));
  }
}
