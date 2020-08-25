package top.felixu.thread;

/**
 * @author felixu
 * @since 2020.06.17
 */
public class Test {

    public static void main(String[] args) throws Exception {

        // 创建三个线程 A, B C
        Thread threadA = new Thread(() -> {
            System.out.println(Thread.currentThread() + "执行");
        }, "线程A");
        Thread threadB = new Thread(() -> {
            System.out.println(Thread.currentThread() + "执行");
        }, "线程B");
        Thread threadC = new Thread(() -> {
            System.out.println(Thread.currentThread() + "执行");
        }, "线程C");

        threadA.start();
        threadA.join();
        threadB.start();
        threadB.join();
        threadC.start();
        threadC.join();
    }
}
