package cn.o4a.common;

import cn.o4a.common.exception.BizError;
import cn.o4a.common.exception.BizException;
import cn.o4a.common.exception.ParamError;

import java.util.Collection;

/**
 * 条件断言工具类
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/14 15:36
 */
public class Assert {
    private static BizError assertError = ParamError.PARAM_ERROR;

    private Assert() {
    }

    public static void setAssertError(BizError error) {
        assertError = error;
    }

    /**
     * 为空断言
     *
     * @param val       值
     * @param error     指定错误
     * @param customMsg 自定义错误信息
     */
    public static void isNull(final Object val, BizError error, String customMsg) {
        if (val != null) {
            throw BizException.causeBy(error, customMsg);
        }
    }

    /**
     * 为空断言
     *
     * @param val       值
     * @param customMsg 自定义错误信息
     */
    public static void isNull(final Object val, String customMsg) {
        isNull(val, assertError, customMsg);
    }

    /**
     * 不为空断言
     *
     * @param val       值
     * @param error     指定错误
     * @param customMsg 自定义错误信息
     */
    public static void notNull(final Object val, BizError error, String customMsg) {
        if (val == null) {
            throw BizException.causeBy(error, customMsg);
        }
    }

    /**
     * 不为空断言
     *
     * @param val       值
     * @param customMsg 自定义错误信息
     */
    public static void notNull(final Object val, String customMsg) {
        notNull(val, assertError, customMsg);
    }

    /**
     * 为空或空白字符串断言
     *
     * @param text      值
     * @param customMsg 自定义错误信息
     */
    public static void isBlank(final CharSequence text, String customMsg) {
        if (!isBlank(text)) {
            throw BizException.causeBy(assertError, customMsg);
        }
    }

    /**
     * 为空或空白字符串断言
     *
     * @param text      值
     * @param customMsg 自定义错误信息
     */
    public static void isBlank(final CharSequence text, BizError error, String customMsg) {
        if (!isBlank(text)) {
            throw BizException.causeBy(error, customMsg);
        }
    }

    /**
     * 不为空或空白字符串断言
     *
     * @param text      值
     * @param customMsg 自定义错误信息
     */
    public static void notBlank(final CharSequence text, String customMsg) {
        if (isBlank(text)) {
            throw BizException.causeBy(assertError, customMsg);
        }
    }

    /**
     * 不为空或空白字符串断言
     *
     * @param text      值
     * @param customMsg 自定义错误信息
     */
    public static void notBlank(final CharSequence text, BizError error, String customMsg) {
        if (isBlank(text)) {
            throw BizException.causeBy(error, customMsg);
        }
    }

    /**
     * 是否为空或空白字符串判断
     *
     * @param text 值
     */
    private static boolean isBlank(CharSequence text) {
        final int len = text == null ? 0 : text.length();
        if (len == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 集合为空断言
     *
     * @param values    值
     * @param customMsg 自定义错误信息
     */
    public static void isEmpty(final Iterable<?> values, String customMsg) {
        if (!isEmpty(values)) {
            throw BizException.causeBy(assertError, customMsg);
        }
    }

    /**
     * 集合为空断言
     *
     * @param values    值
     * @param error     指定错误
     * @param customMsg 自定义错误信息
     */
    public static void isEmpty(final Iterable<?> values, BizError error, String customMsg) {
        if (!isEmpty(values)) {
            throw BizException.causeBy(error, customMsg);
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param values 值
     */
    private static boolean isEmpty(Iterable<?> values) {
        if (values == null) {
            return true;
        }
        if (values instanceof Collection && ((Collection<?>) values).isEmpty()) {
            return true;
        }
        return !values.iterator().hasNext();
    }

    /**
     * 集合不为空断言
     *
     * @param values    值
     * @param customMsg 自定义错误信息
     */
    public static void notEmpty(final Iterable<?> values, String customMsg) {
        if (isEmpty(values)) {
            throw BizException.causeBy(assertError, customMsg);
        }
    }

    /**
     * 集合不为空断言
     *
     * @param values    值
     * @param error     指定错误
     * @param customMsg 自定义错误信息
     */
    public static void notEmpty(final Iterable<?> values, BizError error, String customMsg) {
        if (isEmpty(values)) {
            throw BizException.causeBy(error, customMsg);
        }
    }
}
