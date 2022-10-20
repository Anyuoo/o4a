package cn.o4a.rpc.common;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 15:24
 */
public interface Constants {

    default Constants self() {
        return new Constants(){};
    }

    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
}
