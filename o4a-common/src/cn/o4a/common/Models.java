package cn.o4a.common;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 实体、bean 相关操作工具类
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/3/15 17:43
 */
public class Models {

    private Models() {
    }

    /**
     * 实体映射转换
     *
     * @param entities 实体
     * @param mapper   映射
     * @return 结果集
     */
    public static <T, R> List<R> castToOthers(Iterable<T> entities, Function<T, R> mapper) {
        if (isEmpty(entities)) {
            return emptyList();
        }
        return streamOf(entities).map(mapper).collect(Collectors.toList());
    }

    /**
     * 实体映射转换
     *
     * @param entities 实体
     * @param mapper   映射
     * @return 结果集
     */
    public static <T, R> Set<R> castToDistinctOthers(Iterable<T> entities, Function<T, R> mapper) {
        return new HashSet<>(castToOthers(entities, mapper));
    }

    /**
     * 映射实体中属性中的属性值
     *
     * @param entities    实体
     * @param fieldGetter 实体属性字段
     * @param fieldMapper 属性转换
     * @param <T>         实体类型
     * @param <E>         实体属性类型
     * @param <R>         属性映射结果类型
     * @return 值
     */
    public static <T, E, R> List<R> flatCastToOthers(Iterable<T> entities, Function<T, Collection<E>> fieldGetter, BiFunction<T, E, R> fieldMapper) {
        if (isEmpty(entities)) {
            return emptyList();
        }
        return streamOf(entities).map(entity -> {
            Collection<E> properties = fieldGetter.apply(entity);
            List<R> rs;
            if (isEmpty(properties)) {
                rs = emptyList();
            } else {
                rs = properties.stream().map(r -> fieldMapper.apply(entity, r)).collect(Collectors.toList());
            }
            return rs;
        }).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * 映射实体中属性中的属性值
     *
     * @param entities    实体
     * @param fieldGetter 实体属性字段
     * @param fieldMapper 属性转换
     * @param <T>         实体类型
     * @param <E>         实体属性类型
     * @param <R>         属性映射结果类型
     * @return 值
     */
    public static <T, E, R> Set<R> flatCastToDistinctOthers(Iterable<T> entities, Function<T, Collection<E>> fieldGetter, BiFunction<T, E, R> fieldMapper) {
        Assert.notNull(fieldGetter, "属性获取方法不能为 null");
        Assert.notNull(fieldMapper, "属性值装换方法不能为 null");
        if (isEmpty(entities)) {
            return emptySet();
        }
        return streamOf(entities).map(entity -> {
            Collection<E> properties = fieldGetter.apply(entity);
            Set<R> rs;
            if (isEmpty(properties)) {
                rs = emptySet();
            } else {
                rs = properties.stream().map(r -> fieldMapper.apply(entity, r)).collect(Collectors.toSet());
            }
            return rs;
        }).flatMap(Set::stream).collect(Collectors.toSet());
    }

    /**
     * 实体过滤
     *
     * @param entities  实体数据
     * @param predicate 过滤条件
     * @return 满足条件的值
     */
    public static <T> List<T> collectPart(Iterable<T> entities, Predicate<T> predicate) {
        Assert.notNull(predicate, "过滤条件不能为 null");
        if (isEmpty(entities)) {
            return emptyList();
        }
        return streamOf(entities).filter(predicate).collect(Collectors.toList());
    }

    /**
     * 实体按某个字段值过滤
     *
     * @param entities    实体数据
     * @param fieldMapper 字段映射
     * @param predicate   过滤条件
     * @return 满足条件的值
     */
    public static <T, F> List<T> collectPart(Iterable<T> entities, Function<T, F> fieldMapper, Predicate<F> predicate) {
        Assert.notNull(fieldMapper, "字段映射不能为 null");
        Assert.notNull(predicate, "过滤条件不能为 null");
        if (isEmpty(entities)) {
            return emptyList();
        }
        return streamOf(entities).filter(entity -> predicate.test(fieldMapper.apply(entity))).collect(Collectors.toList());
    }

    /**
     * 转Map , key重复丢弃后续
     *
     * @param entities  实体
     * @param keyMapper key映射
     * @param valMapper val映射
     * @return map
     */
    public static <T, K, V> Map<K, V> castToMap(Iterable<T> entities, Function<T, K> keyMapper, Function<T, V> valMapper) {
        Assert.notNull(keyMapper, "key映射不能为 null");
        Assert.notNull(keyMapper, "val映射不能为 null");
        if (isEmpty(entities)) {
            return emptyMap();
        }
        return streamOf(entities).collect(Collectors.toMap(keyMapper, valMapper, (oldVal, newVal) -> oldVal));
    }

    /**
     * Converts Iterable to stream
     */
    public static <T> Stream<T> streamOf(final Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<T>) iterable).stream();
        }
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * 集合是否为空
     *
     * @param entities 实体
     */
    public static boolean isEmpty(Iterable<?> entities) {
        if (entities == null) {
            return true;
        }
        if (entities instanceof Collection) {
            return ((Collection<?>) entities).isEmpty();
        }
        return !entities.iterator().hasNext();
    }

    /**
     * 集合是否不为空
     *
     * @param entities 实体集合
     */
    public static boolean isNotEmpty(Iterable<?> entities) {
        return !isEmpty(entities);
    }

    /**
     * 空的List
     */
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    /**
     * 空的List
     */
    public static <E> Set<E> emptySet() {
        return new HashSet<>();
    }

    /**
     * 空的Map
     */
    public static <K, V> Map<K, V> emptyMap() {
        return new HashMap<>();
    }
}
