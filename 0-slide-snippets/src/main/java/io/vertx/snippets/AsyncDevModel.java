package io.vertx.snippets;

import io.vertx.core.Handler;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class AsyncDevModel {

  void operation(int a, int b, Handler<Integer> handler) {
    // ...
    int result = a + b;
    handler.handle(result);
    // ...
  }

  void handle(Integer result) {
     // ...
  }

  void main() {
    operation(1, 1, this::handle);
  }

}
