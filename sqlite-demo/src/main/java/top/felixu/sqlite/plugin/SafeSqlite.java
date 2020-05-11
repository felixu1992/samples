package top.felixu.sqlite.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import top.felixu.sqlite.SqliteLock;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author felixu
 * @since 2020.05.11
 */
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SafeSqlite implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object result;
        switch (method.getName()) {
            case "query":
                boolean readLock = SqliteLock.tryReadLock();
                if (!readLock)
                    throw new SqliteLock.TryLockTimeoutException(SqliteLock.getTryLockTimeout(), SqliteLock.getTryLockTimeUnit());
                result = invocation.proceed();
                SqliteLock.releaseReadLock();
                break;
            case "update":
                boolean writeLock = SqliteLock.tryWriteLock();
                if (!writeLock)
                    throw new SqliteLock.TryLockTimeoutException(SqliteLock.getTryLockTimeout(), SqliteLock.getTryLockTimeUnit());
                result = invocation.proceed();
                SqliteLock.releaseWriteLock();
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
}