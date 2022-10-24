package cn.o4a.rpc.client;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 16:47
 */
public interface Dispatcher {

    void send(String ability, Object message);
}
