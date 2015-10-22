import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

import java.util.logging.Logger;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Consumer {


  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), ar -> {
      Vertx vertx = ar.result();
      vertx.eventBus().consumer("events", m -> {
        JsonObject json = (JsonObject) m.body();
        Logger.getAnonymousLogger().info("Receiving " + json.getString("message") + " from " + json.getString("from"));
      });
    });
  }
}
