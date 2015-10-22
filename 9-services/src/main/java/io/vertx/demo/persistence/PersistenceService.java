package io.vertx.demo.persistence;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.demo.persistence.impl.PersistenceServiceImpl;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.List;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@ProxyGen // Generate the proxy and handler
@VertxGen // Generate clients in non-java languages
public interface PersistenceService {

  static PersistenceService createProxy(Vertx vertx, String address) {
    return ProxyHelper.createProxy(PersistenceService.class, vertx, address);
  }

  static PersistenceService create(Vertx vertx, JsonObject config) {
    return new PersistenceServiceImpl(vertx, config);
  }

  void initialize(Handler<AsyncResult<Void>> completion);

  void getAll(Handler<AsyncResult<List<Whisky>>> result);

  void getOne(int id, Handler<AsyncResult<Whisky>> result);

  void addOne(Whisky whisky, Handler<AsyncResult<Whisky>> result);

  void updateOne(int id, Whisky newValues, Handler<AsyncResult<Whisky>> result);

  void deleteOne(int id, Handler<AsyncResult<Void>> result);


}
