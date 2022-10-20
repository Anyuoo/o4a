package cn.o4a.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * class 相关的工具类
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/4/2 14:38
 */
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 获取子类及其父类的所有字段属性
     *
     * @param clazz 类
     * @return 字段属性
     */
    public static List<Field> listAllDeclaredFields(Class<?> clazz) {
        final List<Field> fields = new ArrayList<>();
        if (clazz == null) {
            return fields;
        }
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    /**
     * 获取类及其父类标注的注解
     *
     * @param clazz           类
     * @param annotationClass 注解
     * @return 如果存在返回, 不存在返回null
     */
    public static <T extends Annotation> T getAnnotationOfClass(Class<?> clazz, Class<T> annotationClass) {
        if (clazz == null || clazz == Object.class) {
            return null;
        }
        final T annotation = clazz.getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        //递归解析
        return getAnnotationOfClass(clazz.getSuperclass(), annotationClass);
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        T annotationOfClass = getAnnotationOfClass(clazz, annotationClass);
        if (annotationOfClass != null) {
            return annotationOfClass;
        }
        return getAnnotationOfInterface(clazz, annotationClass);
    }

    /**
     * 获取类及其父类标注的注解
     *
     * @param clazz           类
     * @param annotationClass 注解
     * @return 如果存在返回, 不存在返回null
     */
    public static <T extends Annotation> T getAnnotationOfInterface(Class<?> clazz, Class<T> annotationClass) {
        if (clazz == null) {
            return null;
        }
        for (Class<?> inter : clazz.getInterfaces()) {
            final T annotation = inter.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 获取类的所有接口
     *
     * @param clazz 类
     * @return 接口
     */
    public static List<Class<?>> listAllInterfacesOfClass(Class<?> clazz) {
        final List<Class<?>> interfaces = new ArrayList<>();
        if (clazz == null) {
            return Collections.emptyList();
        }

        while (clazz != Object.class) {
            interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
            clazz = clazz.getSuperclass();
        }

        return interfaces;
    }
}
