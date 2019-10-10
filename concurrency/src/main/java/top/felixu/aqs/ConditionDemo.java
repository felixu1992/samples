package top.felixu.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author felixu
 * @date 2019.07.25
 */
public class ConditionDemo {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        condition.await();
        condition.signal();
    }
}
