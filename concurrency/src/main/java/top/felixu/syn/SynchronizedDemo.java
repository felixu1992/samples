package top.felixu.syn;

/**
 * @author felixu
 * @since 2020.04.10
 */
public class SynchronizedDemo {

    public synchronized void test1() {

    }

    public void test2() {
        synchronized (this) {

        }
    }
}
