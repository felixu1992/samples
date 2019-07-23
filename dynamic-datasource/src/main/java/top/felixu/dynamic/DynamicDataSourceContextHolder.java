package top.felixu.dynamic;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 动态数据存储
 *
 * @author felixu
 * @date 2019.07.23
 */
public class DynamicDataSourceContextHolder {

    /**
     * 线程本地变量用于存储当前目标数据源
     */
    private static final ThreadLocal<String> DATA_SOURCE_CONTEXT = new ThreadLocal<>();

    /**
     * 存储所有的数据源id
     */
    public static final Set<String> DATA_SOURCE_IDS = new ConcurrentSkipListSet<>();

    /**
     * 设置当前线程的目标数据源
     *
     * @param dataSourceId 目标数据源id
     */
    public static void setTargetDataSource(String dataSourceId) {
        DATA_SOURCE_CONTEXT.set(dataSourceId);
    }

    /**
     * 获取当前线程的目标数据源
     *
     * @return 当前目标数据源的id
     */
    public static String getTargetDataSource() {
        return DATA_SOURCE_CONTEXT.get();
    }

    /**
     * 清楚当前线程的目标数据源
     */
    public static void clearTargetDateSource() {
        DATA_SOURCE_CONTEXT.remove();
    }

    /**
     * 已加载的数据源列表中是否有当前指定的数据源
     *
     * @param dataSourceId 指定的数据源id
     * @return 是否被加载
     */
    public static boolean containsDataSource(String dataSourceId){
        return DATA_SOURCE_IDS.contains(dataSourceId);
    }
}
