package top.felixu.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author felixu
 * @date 2019.07.23
 */
@Component
public class ApplicationUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;

    }

    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
