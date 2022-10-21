package cn.o4a.rpc.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 14:03
 */
public abstract class AbstractMessage {
    public static final boolean TYPE_REQUEST = true;
    public static final boolean TYPE_RESPONSE = false;
    public static final byte STATUS_OK = 10;
    public static final byte STATUS_TIMEOUT = 20;
    private static final AtomicLong INVOKE_ID = new AtomicLong(0);
    /**
     * id
     */
    protected long id;
    /**
     * 消息类型[tr: request, 1: response]
     */
    protected boolean type;
    /**
     * 状态
     */
    protected byte status;

    protected AbstractMessage(boolean type, byte status) {
        this.type = type;
        this.status = status;
        if (type == TYPE_REQUEST) {
            this.id = INVOKE_ID.getAndIncrement();
        }
    }

    public boolean isSender() {
        return type;
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
}
