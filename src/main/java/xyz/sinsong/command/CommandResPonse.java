package xyz.sinsong.command;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Message;

/**
 * @author Jin Huang
 * @date 2021/11/2 14:30
 * 消息响应接口 消息响应封装为此对象
 */
public interface CommandResPonse {
    void sendMessage(); //将这个消息发送出去 发送的内容为消息体 发送的对象是这里的联系人对象

      void setContact(Contact contact);
      void setMessage(Message message);
      Message getMessage();
//    Contact getContact();
//
//    void setContact(Contact contact);
//
//    Message getMessage();
//
//    void setMessage(Message message);
}
