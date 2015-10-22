import io.vertx.core.Handler;

/**
 * No thread demo:
 * <p/>
 * 1) Just "add"
 * 2) With vert.x context
 */
public class Main {

  public static void main(String[] args) {
    System.out.println("1) Thread : " + Thread.currentThread().getName());
    add(1, 1, i -> {
      System.out.println("3) Thread : "
          + Thread.currentThread().getName() + " result : " + i);
    });
    System.out.println("After");
  }

  public static void add(int a, int b, Handler<Integer> handler) {
    int r = a + b;
    handler.handle(r);
  }
}
