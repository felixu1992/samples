package top.felixu.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author felixu
 * @date 2019.07.24
 */
public class ReentrantLockDemo {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();
    }
}
