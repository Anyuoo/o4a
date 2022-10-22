package cn.o4a.rpc.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 14:03
 */
public abstract class AbstractMessage {
    /**
     * 消息类型
     */
    public static final boolean TYPE_REQUEST = true;
    public static final boolean TYPE_RESPONSE = false;
    /**
     * 状态
     */
    public static final byte STATUS_OK = 20;
    public static final byte STATUS_TIMEOUT = 30;
    public static final byte STATUS_ERROR = 40;
    /**
     * 心跳事件
     */
    public static final String HEART_BEAT_EVENT = null;

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);
    /**
     * id
     */
    protected long id;
    /**
     * 消息类型[true: request, false: response]
     */
    protected final boolean type;
    /**
     * 状态
     */
    protected final byte status;
    /**
     * 是否是事件
     */
    protected final boolean event;

    protected AbstractMessage(boolean type, byte status, boolean event) {
        this.type = type;
        this.status = status;
        this.event = event;
        if (type == TYPE_REQUEST) {
            this.id = INVOKE_ID.getAndIncrement();
        }
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
