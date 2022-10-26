package cn.o4a.rpc.server;

import cn.newrank.niop.sdk.model.Task;
import cn.o4a.rpc.common.Ability;
import cn.o4a.rpc.common.Channel;
import cn.o4a.rpc.common.Message;
import cn.o4a.rpc.common.MessageCode;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 20:10
 */
public class AbilityDispatcher {
    private final ConcurrentHashMap<String, ConcurrentHashMap<Channel, Integer>> ABILITY_ROUTES = new ConcurrentHashMap<>();
    private static final  Map<Channel, Set<String>> CHANNEL_ABILITY_ID_MAP = new ConcurrentHashMap<>();
    private Map<String, Ability> abilityMap;

    public void registerAbility(Channel channel, Set<String> abilityIds) {
        CHANNEL_ABILITY_ID_MAP.put(channel, abilityIds);

        if (abilityIds == null || abilityIds.isEmpty()) {
            return;
        }

        for (String abilityId : abilityIds) {
            final Map<Channel, Integer> channelMap = ABILITY_ROUTES.computeIfAbsent(abilityId, id -> new ConcurrentHashMap<>());
            channelMap.put(channel, 1);
        }

    }

    public void unregisterAbility(Channel channel) {
        CHANNEL_ABILITY_ID_MAP.remove(channel);
    }

    public boolean dispatch(String abilityId, Task task) {
        if (!ABILITY_ROUTES.containsKey(abilityId)) {
            return false;
        }

        final ConcurrentHashMap<Channel, Integer> map = ABILITY_ROUTES.get(abilityId);
        if (map.isEmpty()) {
            return false;
        }
        Channel channel = null;
        for (Map.Entry<Channel, Integer> entry : map.entrySet()) {
            channel = entry.getKey();
        }
        if (channel != null) {
            final Message request = Message.request(task,MessageCode.TASK_DISPATCH );
            channel.send(request);
        }
        return true;
    }

}
