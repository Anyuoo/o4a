package cn.o4a.common;

import java.util.Objects;

/**
 * bean copy
 *
 * @author dengjiawei
 */
public class BeanUtil {
    private BeanUtil() {
    }

    /**
     * 实例化类
     *
     * @param clazz 类信息
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            if (clazz.getConstructors().length == 0) {
                // No public constructors
                SecurityManager sm = System.getSecurityManager();
                if (sm != null) {
                    // Throws an exception when access is not allowed
                    sm.checkPackageAccess("Safe");
                }
            }
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("实例化对象失败,传入参数:" + clazz.toString() + "不合法");
        }
    }

    /**
     * 反射获取枚举属性
     *
     * @param clazz 枚举类
     * @param <E>   元素类型
     * @return 元素
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E[] listEnumEle(Class<? extends Enum<E>> clazz) {
        Objects.requireNonNull(clazz);
        return (E[]) clazz.getEnumConstants();
    }
}
