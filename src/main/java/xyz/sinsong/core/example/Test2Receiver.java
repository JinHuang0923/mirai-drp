package xyz.sinsong.core.example;

import love.forte.simbot.api.message.MessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.sinsong.core.anotation.Receive;
import xyz.sinsong.core.anotation.Receiver;
import xyz.sinsong.core.model.CommandRequest;
import xyz.sinsong.core.utils.MessageBuiderUtils;

/**
 * @author Jin Huang
 * @date 2021/10/31 12:59
 */
@Receiver
public class Test2Receiver {
    @Autowired
    private MessageBuiderUtils messageBuiderUtils;

    @Receive(value = "私聊测试",isPrivate = true)
    public MessageContent testContent(CommandRequest request){
        System.out.println(request);
        return messageBuiderUtils.buildMessage("私聊测试成功");
    }
    @Receive(value = "私聊参数测试",isPrivate = true)
    public MessageContent testContent1(CommandRequest request){
        System.out.println(request);
        return messageBuiderUtils.buildMessage("私聊参数测试成功" + request.getParameter(1)+request.getParameter(2));
    }

}
