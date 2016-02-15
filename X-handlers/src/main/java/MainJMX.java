import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

/**
 * HTTP Demo.
 */
public class MainJMX {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true)
        .setJmxEnabled(true).setJmxDomain("vertx-http-app")));

    vertx.createHttpServer().requestHandler(
        req -> {
          req.response().end("Hello !!!");
    }).listen(8080);
  }
}
