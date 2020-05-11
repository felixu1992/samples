package top.felixu.sqlite;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author felixu
 * @since 2020.05.11
 */
public class SqliteLock {

    private final static ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    private final static ReentrantReadWriteLock.WriteLock WRITE_LOCK = LOCK.writeLock();
    private final static ReentrantReadWriteLock.ReadLock READ_LOCK = LOCK.readLock();
    @Getter
    private static long tryLockTimeout = 1;
    @Getter
    private static TimeUnit tryLockTimeUnit = TimeUnit.SECONDS;

    public void setTryLockTimeout(long tryLockTimeout, @NonNull TimeUnit tryLockTimeUnit) {
        SqliteLock.tryLockTimeout = tryLockTimeout;
        SqliteLock.tryLockTimeUnit = tryLockTimeUnit;
    }

    public static boolean tryReadLock() {
        try {
            return READ_LOCK.tryLock(tryLockTimeout, tryLockTimeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static void releaseReadLock() {
        READ_LOCK.unlock();
    }

    public static boolean tryWriteLock() {
        try {
            return WRITE_LOCK.tryLock(tryLockTimeout, tryLockTimeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static void releaseWriteLock() {
        WRITE_LOCK.unlock();
    }

    public static class TryLockTimeoutException extends Exception {
        private static final long serialVersionUID = 6968057376316189716L;

        @Getter
        private final long tryLockTimeout;
        @Getter
        private final TimeUnit tryLockTimeUnit;

        public TryLockTimeoutException(long tryLockTimeout, TimeUnit tryLockTimeUnit) {
            super("未能在指定时间内（" + tryLockTimeout + tryLockTimeUnit + "）获得锁");
            this.tryLockTimeout = tryLockTimeout;
            this.tryLockTimeUnit = tryLockTimeUnit;
        }
    }
}
