package cn.o4a.common.exception;

import cn.o4a.common.EnumVisible;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/20 10:21
 */
public enum ParamError implements BizError, EnumVisible {
    PARAM_ERROR("PARAM_001", "参数错误"),
    ;

    private final String code;
    private final String message;

    ParamError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
