package cn.o4a.rpc.common;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 19:42
 */
public final class Message extends AbstractMessage {
    /**
     * 消息体
     */
    private final Object body;

    public Message(boolean type, byte status, Object body) {
        super(type, status);
        this.body = body;
    }

    public static Message request(Object body) {
        return new Message(TYPE_REQUEST, STATUS_OK, body);
    }

    public static Message response(byte status, Object body) {
        return new Message(TYPE_RESPONSE, status, body);
    }

    public Object getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", type=" + type +
                ", status=" + status +
                ", body=" + body +
                '}';
    }
}
