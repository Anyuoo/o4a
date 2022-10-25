package cn.o4a.rpc.client;

import cn.newrank.niop.sdk.consumer.AbilityTaskHandler;
import cn.newrank.niop.sdk.model.BizState;
import cn.newrank.niop.sdk.model.ConsumerMessage;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 11:39
 */
public class TaskExecuteHandlers {


    public static AbilityTaskHandler get(String abilityId) {
        return task -> ConsumerMessage.success(task.getTaskId(), BizState.SUCCESS);
    }

}
