package cn.o4a.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

/**
 * 枚举元素查询
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/20 11:00
 */
public final class EnumFinder {

    private static final ConcurrentHashMap<String, Enum<?>> ENUM_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据枚举元素标识id获取对应枚举
     *
     * @param enumClass 枚举类
     * @param enumId 元素标识唯一id
     * @return 枚举元素或 {@code null}
     */
    public static <E extends Enum<E> & EnumVisible> E findStrict(Class<E> enumClass, String enumId) {
        return find(enumClass, enumId, MatchCondition.strictEq());
    }

    /**
     * 根据枚举元素标识elementId获取对应枚举, 匹配时忽略大小写
     *
     * @param enumClass 枚举类
     * @param enumId 元素标识唯一id
     * @return 枚举元素或 {@code null}
     */
    public static <E extends Enum<E> & EnumVisible> E find(Class<E> enumClass, String enumId) {
        return find(enumClass, enumId, MatchCondition.ignoreCaseEq());
    }


    @SuppressWarnings("unchecked")
    private static <E extends Enum<E> & EnumVisible> E find(Class<E> enumClass, String enumId, MatchCondition matchCondition) {
        final String key = String.join(":", enumClass.getName(), enumId);

        final E cacheEnum = (E) ENUM_CACHE.get(key);

        if (cacheEnum != null) {
            return cacheEnum;
        }

        for (E enumConstant : enumClass.getEnumConstants()) {
            if (matchCondition.test(enumConstant.enumId(), enumId)) {
                ENUM_CACHE.putIfAbsent(key, enumConstant);
                return enumConstant;
            }
        }

        return null;
    }



    /**
     * 匹配规则
     */
    interface MatchCondition extends BiPredicate<String, String> {

        /**
         * 忽略大小写匹配
         */
        static MatchCondition ignoreCaseEq() {
            return (enumId, argId) -> enumId != null && enumId.equalsIgnoreCase(argId);
        }

        /**
         * 完全匹配
         */
        static MatchCondition strictEq() {
            return (enumId, argId) -> enumId != null && enumId.equals(argId);
        }
    }
}
