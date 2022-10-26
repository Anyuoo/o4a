package cn.o4a.rpc.client;

import cn.newrank.niop.sdk.consumer.AbilityTaskHandler;
import cn.newrank.niop.sdk.model.ConsumerMessage;
import cn.newrank.niop.sdk.model.DataUnit;

import javax.validation.constraints.NotNull;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 11:39
 */
public class TaskExecuteHandlers {


    public static AbilityTaskHandler get(String abilityId) {
        return task -> ConsumerMessage.success(task.getTaskId(),new User("张三-客户端", "123456"));
    }


    public static class User implements DataUnit{
        String username;
        String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public @NotNull String id() {
            return username;
        }
    }

}
