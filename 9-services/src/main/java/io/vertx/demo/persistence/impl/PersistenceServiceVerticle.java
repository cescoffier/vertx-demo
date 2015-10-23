package io.vertx.demo.persistence.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.demo.persistence.PersistenceService;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class PersistenceServiceVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), ar -> {
      if (ar.failed()) {
        System.err.println(ar.cause());
      } else {
        ar.result().deployVerticle(
            PersistenceServiceVerticle.class.getName(),
            new DeploymentOptions().setConfig(new JsonObject()
                .put("url", "jdbc:hsqldb:file:db/whiskies")
                .put("driver_class", "org.hsqldb.jdbcDriver")
            ));
      }
    });
  }

  PersistenceService service;

  @Override
  public void start() throws Exception {
    service = new PersistenceServiceImpl(vertx, config());
    ProxyHelper.registerService(
        PersistenceService.class, vertx, service,
        "service.persistence");
  }
}
