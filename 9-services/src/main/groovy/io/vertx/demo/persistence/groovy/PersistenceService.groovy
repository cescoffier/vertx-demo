/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.demo.persistence.groovy;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import java.util.List
import io.vertx.groovy.core.Vertx
import io.vertx.demo.persistence.Whisky
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
@CompileStatic
public class PersistenceService {
  private final def io.vertx.demo.persistence.PersistenceService delegate;
  public PersistenceService(Object delegate) {
    this.delegate = (io.vertx.demo.persistence.PersistenceService) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static PersistenceService createProxy(Vertx vertx, String address) {
    def ret= InternalHelper.safeCreate(io.vertx.demo.persistence.PersistenceService.createProxy((io.vertx.core.Vertx)vertx.getDelegate(), address), io.vertx.demo.persistence.groovy.PersistenceService.class);
    return ret;
  }
  public static PersistenceService create(Vertx vertx, Map<String, Object> config) {
    def ret= InternalHelper.safeCreate(io.vertx.demo.persistence.PersistenceService.create((io.vertx.core.Vertx)vertx.getDelegate(), config != null ? new io.vertx.core.json.JsonObject(config) : null), io.vertx.demo.persistence.groovy.PersistenceService.class);
    return ret;
  }
  public void initialize(Handler<AsyncResult<Void>> completion) {
    this.delegate.initialize(completion);
  }
  public void getAll(Handler<AsyncResult<List<Map<String, Object>>>> result) {
    this.delegate.getAll(new Handler<AsyncResult<List<Whisky>>>() {
      public void handle(AsyncResult<List<Whisky>> event) {
        AsyncResult<List<Map<String, Object>>> f
        if (event.succeeded()) {
          f = InternalHelper.<List<Map<String, Object>>>result(event.result().collect({
            io.vertx.demo.persistence.Whisky element ->
            (Map<String, Object>)InternalHelper.wrapObject(element?.toJson())
          }) as List)
        } else {
          f = InternalHelper.<List<Map<String, Object>>>failure(event.cause())
        }
        result.handle(f)
      }
    });
  }
  public void getOne(int id, Handler<AsyncResult<Map<String, Object>>> result) {
    this.delegate.getOne(id, new Handler<AsyncResult<io.vertx.demo.persistence.Whisky>>() {
      public void handle(AsyncResult<io.vertx.demo.persistence.Whisky> event) {
        AsyncResult<Map<String, Object>> f
        if (event.succeeded()) {
          f = InternalHelper.<Map<String, Object>>result((Map<String, Object>)InternalHelper.wrapObject(event.result()?.toJson()))
        } else {
          f = InternalHelper.<Map<String, Object>>failure(event.cause())
        }
        result.handle(f)
      }
    });
  }
  public void addOne(Map<String, Object> whisky = [:], Handler<AsyncResult<Map<String, Object>>> result) {
    this.delegate.addOne(whisky != null ? new io.vertx.demo.persistence.Whisky(new io.vertx.core.json.JsonObject(whisky)) : null, new Handler<AsyncResult<io.vertx.demo.persistence.Whisky>>() {
      public void handle(AsyncResult<io.vertx.demo.persistence.Whisky> event) {
        AsyncResult<Map<String, Object>> f
        if (event.succeeded()) {
          f = InternalHelper.<Map<String, Object>>result((Map<String, Object>)InternalHelper.wrapObject(event.result()?.toJson()))
        } else {
          f = InternalHelper.<Map<String, Object>>failure(event.cause())
        }
        result.handle(f)
      }
    });
  }
  public void updateOne(int id, Map<String, Object> newValues, Handler<AsyncResult<Map<String, Object>>> result) {
    this.delegate.updateOne(id, newValues != null ? new io.vertx.demo.persistence.Whisky(new io.vertx.core.json.JsonObject(newValues)) : null, new Handler<AsyncResult<io.vertx.demo.persistence.Whisky>>() {
      public void handle(AsyncResult<io.vertx.demo.persistence.Whisky> event) {
        AsyncResult<Map<String, Object>> f
        if (event.succeeded()) {
          f = InternalHelper.<Map<String, Object>>result((Map<String, Object>)InternalHelper.wrapObject(event.result()?.toJson()))
        } else {
          f = InternalHelper.<Map<String, Object>>failure(event.cause())
        }
        result.handle(f)
      }
    });
  }
  public void deleteOne(int id, Handler<AsyncResult<Void>> result) {
    this.delegate.deleteOne(id, result);
  }
}
