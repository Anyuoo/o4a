package cn.o4a.rpc.common;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 19:18
 */
public class Executors {

    private static final Map<String, ExecutorService> EXECUTOR_SERVICES = new ConcurrentHashMap<>();
    private Executors() {
    }

    public static ExecutorService sharedExecutor() {
        return sharedExecutor(Constants.SHARED_EXECUTOR, Constants.SHARED_THREADS);
    }


    public static ExecutorService sharedExecutor(String key, int threads) {
        return EXECUTOR_SERVICES.computeIfAbsent(key, k ->
                new ThreadPoolExecutor(0, threads
                        , 60, TimeUnit.SECONDS
                        , new SynchronousQueue<>()
                        , new ThreadPoolExecutor.AbortPolicy())
        );
    }


    public static void destroy(String key, int timeout, TimeUnit timeUnit) {
        final ExecutorService executorService = EXECUTOR_SERVICES.get(key);
        if (executorService != null) {
            try {
                final boolean awaitTermination = executorService.awaitTermination(timeout, timeUnit);
                if (!awaitTermination) {
                    //
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
