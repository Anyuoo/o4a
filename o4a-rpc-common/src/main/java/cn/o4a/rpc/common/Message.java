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
    private Object body;
    /**
     * 额外信息
     */
    private String extMsg;

    public Message(boolean type, byte status, Object body) {
        this(type, false, status, body);
    }

    public Message(boolean type,boolean event, byte status, Object body) {
        super(type, status, event);
        this.body = body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
    public static Message event(String event) {
        return new Message(TYPE_REQUEST, true, STATUS_OK, event);
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

    public boolean isHeartBeatMessage() {
        return event && HEART_BEAT_EVENT == body;
    }

    public String getExtMsg() {
        return extMsg;
    }

    public void setExtMsg(String extMsg) {
        this.extMsg = extMsg;
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
