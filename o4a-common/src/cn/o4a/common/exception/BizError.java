package cn.o4a.common.exception;

import cn.o4a.common.EnumVisible;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/20 10:13
 */
public interface BizError extends EnumVisible {

    String code();


    String message();

    @Override
    default String enumId() {
        return code();
    }
}
