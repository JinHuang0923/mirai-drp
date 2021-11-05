package xyz.sinsong.command;

import net.mamoe.mirai.contact.Contact;
import xyz.sinsong.core.model.Sender;

import java.util.HashMap;

/**
 * @author Jin Huang
 * @date 2021/10/30 19:09
 * 指令请求封装接口 包含了指令 指令参数 发送人等等信息 实现类有 群聊消息请求对象 私聊消息请求对象
 */

public interface CommandRequest {

    String getParameter(int index);
    String getCommand();

     void setCommand(String command);

     HashMap<String, String> getParamMap();

     void setParamMap(HashMap<String, String> paramMap);

     Sender getSender();
     void setSender(Sender sender);

     Contact getContact();

     void setContact(Contact contact);

}
