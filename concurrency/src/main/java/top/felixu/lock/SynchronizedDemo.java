package top.felixu.lock;

public class SynchronizedDemo {

    public static synchronized void method1() {

    }

    public synchronized void method2() {

    }

    public void method3() {
        synchronized (this) {

        }
    }
}