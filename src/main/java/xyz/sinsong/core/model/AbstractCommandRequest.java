package xyz.sinsong.core.model;

import java.util.HashMap;

/**
 * @author Jin Huang
 * @date 2021/11/1 16:01
 */
public abstract class AbstractCommandRequest implements CommandRequest{
    public String command;//指令
    public HashMap<String,String> paramMap;//指令参数 获取直接 get("1") 每一个参数都这么获取 get("0") 就是指令
    public AUser user;//发送人

    public String getParameter(int index){
        if (index == 0){
            return command;
        }
        return paramMap.get(String.valueOf(index));
    }
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public HashMap<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(HashMap<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public AUser getUser() {
        return user;
    }

    public void setUser(AUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CommandRequest{" +
                "command='" + command + '\'' +
                ", paramMap=" + paramMap +
                ", user=" + user +
                '}';
    }

}
