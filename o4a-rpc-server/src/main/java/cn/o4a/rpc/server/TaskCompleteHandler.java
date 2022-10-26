package cn.o4a.rpc.server;

import cn.newrank.niop.sdk.model.ConsumerMessage;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 19:59
 */
public interface TaskCompleteHandler {

    void complete(ConsumerMessage consumerMessage);
}
