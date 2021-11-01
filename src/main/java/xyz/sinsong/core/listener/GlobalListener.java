package xyz.sinsong.core.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.sinsong.core.dispatch.CommandProcessor;
import xyz.sinsong.core.dispatch.DispatchCommand;
import xyz.sinsong.core.model.AUser;

/**
 * @author Jin Huang
 * @date 2021/11/1 10:25
 * 全局监听器
 * 这里负责监听所有消息 调用指令处理器分发处理
 */
@Slf4j
public class GlobalListener {



    private CommandProcessor commandProcessor;

    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    public void setDispatchCommand(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        log.info("全局消息监听器注入全局指令调度器成功:{}",commandProcessor);
    }

    public GlobalListener(){
        log.info("全局消息监听器初始创建成功!");
    }





    /**
     * 全局监听私聊消息
     * 交给分发器处理
     */
    @OnPrivate
    public void sendPrivareMsg(PrivateMsg groupMsg, MsgSender sender) {
        long codeNumber = groupMsg.getAccountInfo().getAccountCodeNumber();//发送人qq号
        MessageContent messageContent = commandProcessor.execute(groupMsg);
        //有消息返回才送信
        if (messageContent != null){
            // MsgSender中存在三大送信器，以及非常多的重载方法。
            sender.SENDER.sendPrivateMsg(codeNumber,messageContent);//参数1:发送给的qq号 参数2:返回消息
        }
    }
    /**
     * 全局监听群聊消息
     * 交给分发器处理
     */
    @OnGroup
    public void sendGroupMsg(GroupMsg groupMsg, MsgSender sender) {
        long groupCodeNumber = groupMsg.getGroupInfo().getGroupCodeNumber();//群号
        MessageContent messageContent = commandProcessor.execute(groupMsg);
//        GroupInfo groupInfo = groupMsg.getGroupInfo();//群信息

//        String groupName = groupInfo.getGroupName();//群名
//        String msgStr = groupMsg.getMsgContent().getMsg();//消息文本
//        long senderQqNumber = groupMsg.getAccountInfo().getAccountCodeNumber();//发送人qq号
//        String nickName = groupMsg.getAccountInfo().getAccountNickname();//发送人昵称
//        log.info("{}({}):{}({}):{}",groupName,groupCodeNumber,nickName,senderQqNumber,msgStr);
        //1.封装获取发送人对象(保存发送人信息)
//        AUser auser = userProcessor.getAuser(senderQqNumber, nickName);
//        AUser auser = new AUser();
        //2.调用指令调度去 处理指令 获得返回消息
//        MessageContent messageContent = dispatchCommand.execution(msgStr,MessageGet);
//        MessageContent messageContent = commandProcessor.judgeCommand(msgStr,auser);

        //有消息返回才送信
        if (messageContent != null){
            // MsgSender中存在三大送信器，以及非常多的重载方法。
            sender.SENDER.sendGroupMsg(groupCodeNumber,messageContent);//参数1:发送给的qq号 参数2:返回消息
        }
    }
}
