package xyz.sinsong.core.dispatch;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.Message;
import xyz.sinsong.core.load.ReceiverHandlerFactory;
import xyz.sinsong.core.load.ReceiverHandlerMappingFactory;
import xyz.sinsong.command.CommandRequest;
import xyz.sinsong.command.GroupCommandRequest;
import xyz.sinsong.command.PrivateCommandRequest;
import xyz.sinsong.command.CommandResPonse;
import xyz.sinsong.command.GroupCommandResPonse;
import xyz.sinsong.command.PrivateCommandResPonse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jin Huang
 * @date 2021/11/1 10:51
 * 调度器抽象实现 主要逻辑
 * 后续考虑把响应对象,一起发送给方法调用方 mvc
 */
@Slf4j
public abstract class AbstractCommandProcessor implements CommandProcessor {

    /**
     * 后续好绿这里初始化做封装 而不是在监听器
     * 这里处理请求 参数 请求对象封装
     * 这个不被继承 参数封装自己处理就行
     * @return
     */
    @Override
    public abstract CommandRequest init(CommandRequest request);
    // 分发指令前方法 不做处理 使用者需要再处理
    @Override
    public abstract CommandRequest before(CommandRequest request);
    //分发指令后方法 不做处理 使用者需要再处理
    @Override
    public abstract CommandResPonse after(CommandResPonse commandResPonse);

    /**
     * 回复消息 默认直接响应
     * @param commandResPonse
     */
    @Override
    public void reply(CommandResPonse commandResPonse) {
        if (commandResPonse !=null && commandResPonse.getMessage()!=null){
            commandResPonse.sendMessage();
        }
    }

    /**
     * 主要分发指令的处理方法 这里封装
     * @param request
     * @return
     */
    @Override
    public final CommandResPonse handle(CommandRequest request) {
        // log.info("具体分发指令方法handle()调用...");
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
            Message message = invokeHandlerMethod(instance, methodMapping, request);
            //调用方法之后拿到了消息返回的主体了 需要联系人对象 请求对象是有的 群请求群响应 私请求私响应
            return packResponse(request,message);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 具体执行指令处理方法
     * 放入一个Receiver 实例对象 与方法实例 对象 以及方法参数
     * 方法需要返回一个message 之后调度器发送之前再做处理封装为响应对象
     * @param instance
     * @param method
     * @return
     * @throws Throwable
     */
    public final Message invokeHandlerMethod(Object instance, Method method,CommandRequest request)
            throws Throwable {
        Message result = null;
        if (method != null) {
            try {
//                log.info("找到接收指令方法,开启执行方法处理!");
                //调用method的方法 返回结果 // invoke 第一个参数是实例对象   第二个是可变的方法参数
                result = (Message) method.invoke(instance,request);
            } catch (InvocationTargetException e) {
                log.error("执行指令方法时出错了!");
                throw e.getTargetException();
            }
        }
        return result;
    }

    /**
     * 封装响应对象
     * @return
     * @param request
     * @param message
     */
    public CommandResPonse packResponse(CommandRequest request, Message message){
        CommandResPonse resPonse = null;
        if (request instanceof GroupCommandRequest){
            resPonse = new GroupCommandResPonse();
            resPonse.setMessage(message);
            resPonse.setContact(request.getContact());
        }
        if (request instanceof PrivateCommandRequest){
            resPonse = new PrivateCommandResPonse();
            resPonse.setMessage(message);
            resPonse.setContact(request.getContact());
        }
        return resPonse;

    }
}
