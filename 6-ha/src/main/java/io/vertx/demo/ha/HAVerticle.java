package io.vertx.demo.ha;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.lang.management.ManagementFactory;

/**
 * 1. Launch this app
 * 2. Check the jvm ID
 * 3. Launch vertx bare -cp target/classes/
 * 4. Kill this app (jps -v | grep AppMain)
 * 5. Check the jvm ID
 */
public class HAVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions()
        .setHAEnabled(true), vertx -> {
          vertx.result()
              .deployVerticle(
                  HAVerticle.class.getName(),
                  new DeploymentOptions().setHa(true));
    });
  }


  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      final String name =
          ManagementFactory.getRuntimeMXBean().getName();
      req.response().end("Request served by " + name);
    }).listen(8080);
  }
}
