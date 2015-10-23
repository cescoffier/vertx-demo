import io.vertx.core.Vertx;

import java.util.logging.Logger;

/**
 * Handler Demo
 * <p/>
 * 1) Consumer / Provider
 * 2) Multiple provider
 * 3) Blocking
 */
public class Main {

  public static final Logger LOGGER =
      Logger.getLogger("demo");

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.eventBus()
        .consumer("events", event -> {
          LOGGER.info("Receiving "
              + event.body() + " on "
              + Thread.currentThread().getName());
        });

    vertx.setPeriodic(1000,
        l -> vertx.eventBus().publish("events", "hello"));
  }
}
