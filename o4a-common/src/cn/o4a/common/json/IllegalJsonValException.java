package cn.o4a.common.json;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/26 11:17
 */
public class IllegalJsonValException extends RuntimeException{


    public IllegalJsonValException(String message, Throwable cause) {
        super(message, cause);
    }
}
