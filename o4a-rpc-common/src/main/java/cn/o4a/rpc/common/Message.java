package cn.o4a.rpc.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 19:42
 */
public final class Message {
    /**
     * 消息类型
     */
    public static final boolean TYPE_REQUEST = true;
    public static final boolean TYPE_RESPONSE = false;
    /**
     * 状态
     */
    public static final byte STATUS_OK = 10;
    public static final byte STATUS_ERROR = 20;
    /**
     * 心跳事件
     */
    public static final String HEART_BEAT_EVENT = null;
    /**
     * 异常事件
     */
    public static final String EXCEPTION_EVENT = "EXP_EVENT:";
    private static final AtomicLong ID_SUPPLIER = new AtomicLong(0);
    /**
     * id
     */
    private long id;
    /**
     * 消息类型[true: request, false: response]
     */
    private boolean type;
    /**
     * 状态
     */
    private byte status;
    /**
     * 是否是事件
     */
    private boolean event;
    /**
     * 消息体
     */
    private Object body;
    /**
     * 错误信息
     */
    private String error;

    /**
     * 消息code, 对应业务
     */
    private MessageCode code;

    public Message(long id, boolean type, byte status, boolean event, Object body) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.event = event;
        this.body = body;
    }

    /**
     * Request 构造
     *
     * @param status 状态
     * @param event  是否是事件
     * @param body   body
     */
    public Message(byte status, boolean event, Object body) {
        this.type = TYPE_REQUEST;
        this.status = status;
        this.event = event;
        this.body = body;
        this.id = ID_SUPPLIER.getAndIncrement();
    }

    /**
     * Response
     *
     * @param id     id
     * @param status 状态
     * @param event  是否是事件
     * @param body   body
     */

    public Message(long id, byte status, boolean event, Object body) {
        this.type = TYPE_RESPONSE;
        this.status = status;
        this.event = event;
        this.body = body;
        this.id = id;
    }

    public static Message heartBeatEvent() {
        return new Message(STATUS_OK, true, HEART_BEAT_EVENT);
    }

    public static Message heartBeatEvent(long id) {
        return new Message(id, STATUS_OK, true, HEART_BEAT_EVENT);
    }

    public static Message exceptionEvent(String desc) {
        return new Message(STATUS_OK, true, EXCEPTION_EVENT + desc);
    }

    public static Message request(Object body, MessageCode code) {
        final Message req = new Message(STATUS_OK, false, body);
        req.setCode(code);
        return req;
    }

    public static Message response(long id, byte status, Object body, MessageCode code) {
        final Message resp = new Message(id, status, false, body);
        resp.setCode(code);
        return resp;
    }

    public MessageCode getCode() {
        return code;
    }

    public void setCode(MessageCode code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isRequest() {
        return type;
    }

    public boolean isReponse() {
        return !type;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public boolean isHeartBeatMessage() {
        return event && HEART_BEAT_EVENT == body;
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
