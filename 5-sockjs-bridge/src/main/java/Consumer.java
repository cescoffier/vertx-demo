import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.logging.Logger;

/**
 * 1) Start consumer
 * 2) Start Java provider
 * 3) npm start
 * 4) Browser
 */
public class Consumer {

  private static Logger logger = Logger.getAnonymousLogger();

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), ar -> {
      Vertx vertx = ar.result();

      configureSockJSBridge(vertx);

      vertx.eventBus()
          .consumer("events",
              m -> {
                JsonObject json = (JsonObject) m.body();
                logger.info("Receiving "
                    + json.getString("message")
                    + " from " + json.getString("from"));
      });
    });
  }

  private static void configureSockJSBridge(Vertx vertx) {
    Router router = Router.router(vertx);

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    BridgeOptions options = new BridgeOptions()
        .addInboundPermitted(
            new PermittedOptions().setAddress("events"))
        .addOutboundPermitted(
            new PermittedOptions().setAddress("events"));
    sockJSHandler.bridge(options);

    router.route("/eventbus/*").handler(sockJSHandler);
    router.route("/assets/*")
        .handler(StaticHandler.create("assets"));

    vertx.createHttpServer()
        .requestHandler(router::accept).listen(8080);
  }
}
