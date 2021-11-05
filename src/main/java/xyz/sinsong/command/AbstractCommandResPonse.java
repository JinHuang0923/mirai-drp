package xyz.sinsong.command;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Message;

/**
 * @author Jin Huang
 * @date 2021/11/2 14:31
 */
public abstract class AbstractCommandResPonse implements CommandResPonse {
    public Contact contact;//响应目标
    public Message message;//响应消息主体

    public Contact getContact() {
        return contact;
    }

    public abstract void setContact(Contact contact);
    public abstract void setMessage(Message message);

    public Message getMessage() {
        return message;
    }

    @Override
    public final void sendMessage() {
        contact.sendMessage(message);
    }
}
