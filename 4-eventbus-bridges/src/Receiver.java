import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Receiver {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), result -> {
      if (result.failed()) {
        System.err.println("Cannot create a clustered vert.x : " + result.cause());
      } else {
        Vertx vertx = result.result();

        configureSockJSBridge(vertx);
      }
    });
  }

  private static void configureSockJSBridge(Vertx vertx) {
    Router router = Router.router(vertx);

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    BridgeOptions options = new BridgeOptions()
        .addInboundPermitted(
            new PermittedOptions().setAddress("data"))
        .addOutboundPermitted(
            new PermittedOptions().setAddress("data"));
    sockJSHandler.bridge(options);

    router.route("/eventbus/*").handler(sockJSHandler);
    router.route("/assets/*")
        .handler(StaticHandler.create("assets"));

    vertx.createHttpServer()
        .requestHandler(router::accept).listen(8080);
  }

}
