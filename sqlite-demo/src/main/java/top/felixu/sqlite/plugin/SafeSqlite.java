package top.felixu.sqlite.plugin;

import lombok.Getter;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.felixu.sqlite.properties.SqliteProperties;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author felixu
 * @since 2020.05.11
 */
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SafeSqlite implements Interceptor {

    private final static ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    private final static ReentrantReadWriteLock.WriteLock WRITE_LOCK = LOCK.writeLock();

    private final static ReentrantReadWriteLock.ReadLock READ_LOCK = LOCK.readLock();

    @Autowired
    private SqliteProperties sqliteProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object result;
        switch (method.getName()) {
            case "query":
                boolean readLock = tryReadLock();
                if (!readLock)
                    throw new TryLockTimeoutException(sqliteProperties.getTimeout(), sqliteProperties.getTimeUnit());
                result = invocation.proceed();
                releaseReadLock();
                break;
            case "update":
                boolean writeLock = tryWriteLock();
                if (!writeLock)
                    throw new TryLockTimeoutException(sqliteProperties.getTimeout(), sqliteProperties.getTimeUnit());
                result = invocation.proceed();
                releaseWriteLock();
                break;
            default:
                throw new RuntimeException();
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public boolean tryReadLock() {
        try {
            return READ_LOCK.tryLock(sqliteProperties.getTimeout(), sqliteProperties.getTimeUnit());
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void releaseReadLock() {
        READ_LOCK.unlock();
    }

    public boolean tryWriteLock() {
        try {
            return WRITE_LOCK.tryLock(sqliteProperties.getTimeout(), sqliteProperties.getTimeUnit());
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void releaseWriteLock() {
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