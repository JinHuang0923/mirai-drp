package xyz.sinsong.core.example;

import love.forte.simbot.api.message.MessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.sinsong.core.anotation.Receive;
import xyz.sinsong.core.anotation.Receiver;
import xyz.sinsong.core.model.CommandRequest;
import xyz.sinsong.core.utils.MessageBuiderUtils;

/**
 * @author Jin Huang
 * @date 2021/10/31 9:42
 * 接收处理群消息指令测试
 */
@Receiver
public class TestReceiver {

    @Autowired
    private MessageBuiderUtils messageBuiderUtils;

    public TestReceiver(){
        System.out.println("加载接收器"+this);
    }

    @Receive("测试")
    public MessageContent testContent(CommandRequest request){
        System.out.println(request);
        return messageBuiderUtils.buildMessage("分发指令测试成功");
    }
    @Receive("参数测试")
    public MessageContent testContent2(CommandRequest request){
        System.out.println(request);
        return messageBuiderUtils.buildMessage("参数测试:" + request.getParameter(1) + request.getParameter(2));
    }
    @Receive(value = "混搭私聊测试",isPrivate = true)
    public MessageContent testContent3(CommandRequest request){
        System.out.println(request);
        return messageBuiderUtils.buildMessage("混搭私聊测试成功!");
    }


}
