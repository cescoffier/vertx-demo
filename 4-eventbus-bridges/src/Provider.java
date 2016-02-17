import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Provider {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), result -> {
      if (result.failed()) {
        System.err.println("Cannot create a clustered vert.x : " + result.cause());
      } else {
        Vertx vertx = result.result();
        vertx.setPeriodic(3000, l -> {
          vertx.eventBus().send("data",
              new JsonObject()
                .put("message", "hello")
                .put("from", "java producer"));
        });
      }
    });
  }

}
