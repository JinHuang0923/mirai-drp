package xyz.sinsong.core.dispatch;

import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.events.MessageGet;
import xyz.sinsong.core.model.AUser;
import xyz.sinsong.core.model.CommandRequest;


public interface CommandProcessor {


    /**
     *
     * @param messageGet 消息整个事件的父接口 为了给灵活 可以交给使用者处理
     * @return
     */
    CommandRequest init(MessageGet messageGet);
    CommandRequest before(CommandRequest request);
    MessageContent handle(CommandRequest request);
    MessageContent after(MessageContent messageContent);

    /**
     * 默认执行的方法 作为唯一调用入口
     * 管理指令处理 分发的生命周期(模板设计方式)
     * 这里依次进行
     * init() 全局调度器初始化
     * before() 执行之前
     * handle() 执行分发
     * after() 执行之后
     * 可通过实现此接口 改变运行的过程
     * @return
     */
    default MessageContent execute(MessageGet messageGet){
        CommandRequest request0 = init(messageGet);
        CommandRequest request1 = before(request0);
        MessageContent result = handle(request1);
        return after(result);
    }
}
