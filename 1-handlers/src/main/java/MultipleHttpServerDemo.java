import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

import java.util.logging.Logger;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MultipleHttpServerDemo extends AbstractVerticle {

  public static final Logger LOGGER = Logger.getLogger("demo");

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MultipleHttpServerDemo.class.getName(), new DeploymentOptions().setInstances(2));
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response().end("Served from " + Thread.currentThread().getName());
    }).listen(8080);
  }
}
