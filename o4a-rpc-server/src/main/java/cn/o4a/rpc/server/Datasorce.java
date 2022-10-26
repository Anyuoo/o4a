package cn.o4a.rpc.server;

import cn.newrank.niop.sdk.model.Task;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 17:44
 */
public class Datasorce {
    private final AbilityDispatcher abilityDispatcher;

    private static final  AtomicLong taskId = new AtomicLong(0);

    public static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, r -> {
        final Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    public Datasorce(AbilityDispatcher abilityDispatcher) {
        this.abilityDispatcher = abilityDispatcher;
    }


    public void submit() {
        executor.schedule(() -> {
            final Task task = new Task();
            task.setTaskId("task_id_" + taskId.getAndIncrement());
            abilityDispatcher.dispatch("1234", task);
            submit();
        }, 1, TimeUnit.SECONDS);
    }

}
