import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Producer {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(),
        ar -> {
          Vertx vertx = ar.result();
          vertx.setPeriodic(1000,
              l -> {
                vertx.eventBus().publish("events",
                        new JsonObject()
                            .put("message", "hello")
                            .put("from", "java producer"));
      });
    });
  }
}
