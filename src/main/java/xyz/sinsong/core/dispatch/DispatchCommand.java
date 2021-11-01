package xyz.sinsong.core.dispatch;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.events.MessageGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.sinsong.core.load.ReceiverHandlerFactory;
import xyz.sinsong.core.load.ReceiverHandlerMappingFactory;
import xyz.sinsong.core.model.AUser;
import xyz.sinsong.core.model.CommandRequest;
import xyz.sinsong.core.utils.MapUtils;
import xyz.sinsong.core.utils.MessageBuiderUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jin Huang
 * @date 2021/10/30 15:26
 * 统一全局指令处理器
 * 这里会将指令派遣到不同注解声明的指令方法上
 * 具体的实现已经再抽象类中处理
 * 这里可以对抽象类的全局派发初始化 前置 后置 三个方法进行重写处理
 */
@Slf4j
public class DispatchCommand extends AbstractCommandProcessor {
    @Override
    public CommandRequest init( MessageGet messageGet) {
        return super.init(messageGet);
    }

    @Override
    public CommandRequest before(CommandRequest request) {
//        log.info("分发前调用before()...");
        return request;
    }

    @Override
    public MessageContent after(MessageContent messageContent) {
//        log.info("处理后调用after()处理返回结果...");
        return messageContent;
    }
}

