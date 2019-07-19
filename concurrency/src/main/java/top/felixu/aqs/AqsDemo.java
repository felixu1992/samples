package top.felixu.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author felixu
 * @date 2019.07.19
 */
public class AqsDemo {

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        lock.lock();
    }
}
