package top.felixu.dynamic;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import top.felixu.annotation.TargetDataSource;
import top.felixu.utils.HttpUtils;

/**
 * AOP切面
 * {@link Order} 保证该AOP在@Transactional之前执行
 *
 * @author felixu
 * @date 2019.07.23
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) {
        String dsId = dataSourceName(ds);
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            logger.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        } else {
            logger.debug("Use DataSource : {} > {}", dsId, point.getSignature());
            DynamicDataSourceContextHolder.setTargetDataSource(dsId);
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        String dsId = dataSourceName(ds);
        logger.debug("Revert DataSource : {} > {}", dsId, point.getSignature());
        DynamicDataSourceContextHolder.clearTargetDateSource();
    }

    private String dataSourceName(TargetDataSource ds) {
        String dsId = ds.name();
        if (StringUtils.isEmpty(dsId)) {
            dsId = (String) HttpUtils.getRequestAttributes().getAttribute("ds", RequestAttributes.SCOPE_REQUEST);
        }
        return dsId;
    }
}
