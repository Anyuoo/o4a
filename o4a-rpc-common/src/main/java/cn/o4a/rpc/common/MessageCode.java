package cn.o4a.rpc.common;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 20:41
 */
public enum MessageCode {
    NORMAL,
    /**
     * 服务注册
     */
    REGISTER,
    /**
     * 任务完成
     */
    TASK_COMPLETED,
    /**
     * 任务分发
     */
    TASK_DISPATCH,
}
