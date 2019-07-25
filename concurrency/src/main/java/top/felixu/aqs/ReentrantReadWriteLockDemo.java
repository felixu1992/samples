package top.felixu.aqs;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author felixu
 * @date 2019.07.24
 */
public class ReentrantReadWriteLockDemo {

    public static void main(String[] args) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        readLock.unlock();
        writeLock.lock();
        writeLock.unlock();
    }
}
