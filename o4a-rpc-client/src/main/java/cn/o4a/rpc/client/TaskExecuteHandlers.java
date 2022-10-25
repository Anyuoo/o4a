package cn.o4a.rpc.client;

import cn.newrank.niop.sdk.consumer.AbilityTaskHandler;
import cn.newrank.niop.sdk.model.ConsumerMessage;
import cn.newrank.niop.sdk.model.Task;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 11:39
 */
public class TaskExecuteHandlers {


    public static AbilityTaskHandler get(String abilityId) {
        return new AbilityTaskHandler() {
            @Override
            public ConsumerMessage handle(Task task) {
                return null;
            }

        };
    }

}
