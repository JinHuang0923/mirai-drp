package xyz.sinsong.core.dispatch;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import xyz.sinsong.core.load.ReceiverHandlerFactory;
import xyz.sinsong.core.load.ReceiverHandlerMappingFactory;
import xyz.sinsong.core.model.AUser;
import xyz.sinsong.core.model.CommandRequest;
import xyz.sinsong.core.model.GroupCommandRequest;
import xyz.sinsong.core.model.PrivateCommandRequest;
import xyz.sinsong.core.utils.MapUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Jin Huang
 * @date 2021/11/1 10:51
 */
@Slf4j
public abstract class AbstractCommandProcessor implements CommandProcessor {

    /**
     * 这里处理请求 参数 请求对象封装
     * 这个不被继承 参数封装自己处理就行
     * @return
     */
    @Override
    public CommandRequest init(MessageGet messageGet) {
//        log.info("调用init方法处理消息...");
        CommandRequest commandRequest = null;
        //判断是不是群消息 是的话做以下处理
        if (messageGet instanceof GroupMsg){
            GroupMsg groupMsg = (GroupMsg) messageGet;
            //封装发送人对象与 request对象
            //取出基础信息
            GroupInfo groupInfo = groupMsg.getGroupInfo();//群信息
            long groupCodeNumber = groupInfo.getGroupCodeNumber();//群号
            String groupName = groupInfo.getGroupName();//群名
            String msgStr = groupMsg.getMsgContent().getMsg();//消息文本
            long senderQqNumber = groupMsg.getAccountInfo().getAccountCodeNumber();//发送人qq号
            String nickName = groupMsg.getAccountInfo().getAccountNickname();//发送人昵称

            //打印日志
            log.info("群聊[{}({})]:{}({}):{}",groupName,groupCodeNumber,nickName,senderQqNumber,msgStr);

            //封装user对象
            AUser auser = new AUser();
            auser.setNickName(nickName);
            auser.setQqNumber(senderQqNumber);

            //封装指令request对象
            String[] strings = msgStr.split("\\s+");//按空格分割
            HashMap<String, String> paramMap = MapUtils.getParamMap(strings);
            String command = paramMap.get("0"); //这是指令 其他都是参数 单独封装
            //封装请求参数
            commandRequest = new GroupCommandRequest(groupInfo);
            commandRequest.setCommand(command);
            commandRequest.setParamMap(paramMap);
            commandRequest.setUser(auser);

        }

        // TODO: 2021/11/1  如果是私聊消息 这里处理封装私聊消息对象
        if (messageGet instanceof PrivateMsg){
            PrivateMsg privateMsg = (PrivateMsg) messageGet;
            //取出基础信息
            AccountInfo accountInfo = privateMsg.getAccountInfo();//发送人信息
            String msgStr = privateMsg.getMsgContent().getMsg();//消息文本
            long senderQqNumber =accountInfo.getAccountCodeNumber();//发送人qq号
            String nickName = accountInfo.getAccountNickname();//发送人昵称
            //打印日志
            log.info("私聊[{}({})]:{}",nickName,senderQqNumber,msgStr);

            //封装对象
            commandRequest = new PrivateCommandRequest(accountInfo);
            String[] strings = msgStr.split("\\s+");//按空格分割
            HashMap<String, String> paramMap = MapUtils.getParamMap(strings);
            String command = paramMap.get("0"); //这是指令 其他都是参数 单独封装

            AUser auser = new AUser();
            auser.setNickName(nickName);
            auser.setQqNumber(senderQqNumber);

            commandRequest.setCommand(command);
            commandRequest.setParamMap(paramMap);
            commandRequest.setUser(auser);
        }
        return commandRequest;
    }


    // 分发指令前方法 不做处理 使用者需要再处理
    @Override
    public abstract CommandRequest before(CommandRequest request);
    //分发指令后方法 不做处理 使用者需要再处理
    @Override
    public abstract MessageContent after(MessageContent messageContent);

    /**
     * 主要分发指令的处理方法 这里封装
     * @param request
     * @return
     */
    @Override
    public final MessageContent handle(CommandRequest request) {
//        log.info("具体分发指令方法handle()调用...");
        //从request 对象中取出指令
        String command = request.getCommand();

        Method methodMapping = null;
        //先判断是私聊请求还是群聊请求 需要从不同的指令映射集中 获取对应方法
        if (request instanceof GroupCommandRequest){
            //群聊消息请求 通过指令去指令映射工厂 拿到对应的方法(群聊指令映射集中)
            methodMapping = ReceiverHandlerMappingFactory.getMethodMapping(command);
        }else {
            //只有两种可能 那么说明是私聊请求拿私聊的  //通过指令去指令映射工厂 拿到对应的方法()
            methodMapping = ReceiverHandlerMappingFactory.getPrivateMethodMapping(command);
        }
        //拿到处理对应的方法了 判断是否存在处理这个指令的方法
        if (methodMapping == null){
            //不返回发送消息
            return null;
        }
        //走到这里说明存在处理指令的方法 取出声明这个方法的类对象名字
        String className = methodMapping.getDeclaringClass().getName();
        //通过名字从处理器实例对象工厂取出对应实例对象
        Object instance = ReceiverHandlerFactory.get(className);
        //判断是否为空
        if (instance == null){
            //为空没有处理的实例 不返回消息
            log.info("未找到对应指令接收器");
            return null;
        }
        //实际调用方法处理 request对象作为他的方法
        try {
            return invokeHandlerMethod(instance,methodMapping,request);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 具体执行指令处理方法
     * 放入一个Receiver 实例对象 与方法实例 对象 以及方法参数
     * @param instance
     * @param method
     * @return
     * @throws Throwable
     */
    public final MessageContent invokeHandlerMethod(Object instance, Method method,CommandRequest request)
            throws Throwable {
        MessageContent result = null;
        if (method != null) {
            try {
//                log.info("找到接收指令方法,开启执行方法处理!");
                //调用method的方法 返回结果 // invoke 第一个参数是实例对象   第二个是可变的方法参数
                result = (MessageContent) method.invoke(instance,request);
            } catch (InvocationTargetException e) {
                log.error("执行指令方法时出错了!");
                throw e.getTargetException();
            }
        }
        return result;
    }
}
