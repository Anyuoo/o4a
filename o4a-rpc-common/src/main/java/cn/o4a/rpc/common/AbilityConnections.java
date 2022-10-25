package cn.o4a.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 13:39
 */
public class AbilityConnections {
    private static final Map<Channel, Ability> C_A_ROUTOR = new ConcurrentHashMap<>();
    private static final Map<Ability, Channel> A_C_ROUTOR = new ConcurrentHashMap<>();
}
