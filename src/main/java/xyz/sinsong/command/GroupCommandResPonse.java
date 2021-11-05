package xyz.sinsong.command;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Message;

/**
 * @author Jin Huang
 * @date 2021/11/2 14:37
 * 群消息响应对象
 */
public class GroupCommandResPonse extends AbstractCommandResPonse {

    @Override
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public void setMessage(Message message) {
        this.message = message;
    }
}
