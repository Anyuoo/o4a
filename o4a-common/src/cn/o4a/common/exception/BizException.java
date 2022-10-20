package cn.o4a.common.exception;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/20 10:12
 */
public class BizException extends RuntimeException{

    private final BizError bizError;


    private BizException(BizError bizError, String message) {
        super(message == null ? bizError.message() : message);
        this.bizError = bizError;
    }

    public static BizException causeBy(BizError bizError, String customMessage) {
        return new BizException(bizError, customMessage);
    }

    public static BizException causeBy(BizError bizError) {
        return new BizException(bizError, null);
    }

    public BizError getBizError() {
        return bizError;
    }
}
