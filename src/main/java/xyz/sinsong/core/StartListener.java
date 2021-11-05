package xyz.sinsong.core;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.boot.CommandLineRunner;
import xyz.sinsong.core.config.MiraiDrpProperties;
import xyz.sinsong.core.dispatch.CommandProcessor;
import xyz.sinsong.core.ioc.IocUtil;
import xyz.sinsong.command.CommandRequest;
import xyz.sinsong.command.GroupCommandRequest;
import xyz.sinsong.command.PrivateCommandRequest;
import xyz.sinsong.core.model.Sender;
import xyz.sinsong.core.utils.BotFactory;
import xyz.sinsong.core.utils.MapUtils;
import xyz.sinsong.core.utils.PropertiesUtils;

import java.util.HashMap;

/**
 * @author Jin Huang
 * @date 2021/11/2 8:39
 * 实现这个接口在 程序启动完毕之后执行run方法
 * 必须要加入ioc容器才能触发 考虑之后加入spring.factories
 */
//@Component
@Slf4j
public class StartListener implements CommandLineRunner {

    private CommandProcessor commandProcessor;
    @Override
    public void run(String... args) throws Exception {
        //1.读取配置文件 获取配置对象
        MiraiDrpProperties miraiDrpProperties = PropertiesUtils.loadConfiguration();
        //2.调取bot工厂获取bot对象
        Bot bot = BotFactory.loadBot(miraiDrpProperties);
        //3.登录机器人
        bot.login();
        //4.日志输出机器人信息
        log.info("{}({})已成功登录!",bot.getNick(),bot.getId());
        //5.注入全局指令调度器
        this.commandProcessor = IocUtil.getBean(CommandProcessor.class);
        //5.加载全局监听器 监听所有消息
        loadGlobalListener();


        //创建bot配置类 通过工具类 直接给一个 MiraiDrpProperties配置对象就行 (不通过静态方式 这里读取更灵活)
//        Bot bot = BotFactory.INSTANCE.newBot(3403929463l, "zaw123456");
//        bot.login();
    }

    public void loadGlobalListener(){
        log.info("加载全局监听器...");
        // 创建监听 //监听器中处理消息事件 封装消息请求对象 调用全局调度器

        //全局群消息事件监听器
        Listener GroupListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
//            MessageChain chain = event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
//            event.getSubject().sendMessage("Hello!"); // 回复消息
            eventProcess(event);
        });

        //全局好友消息事件监听器
        Listener friendlistener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, event -> {
//            MessageChain chain = event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
//            event.getSubject().sendMessage("Hello!"); // 回复消息
            eventProcess(event);
        });

    }

    /**
     * 事件处理
     * @param event
     */
    public void eventProcess(MessageEvent event){
        CommandRequest request = null;
        //先new的原因是必须new对象才能设置值
        //群消息处理封装
        if (event instanceof GroupMessageEvent){
            request = new GroupCommandRequest();
            request = commonProcess(event,request);
            //判断是否需要分发 不需要的话就不用处理后续参数了
            if (request == null){
                return;
            }
            //基础参数有了 封装更多的群信息
            GroupMessageEvent groupMessageEvent = (GroupMessageEvent) event;
            Group groupSubject = groupMessageEvent.getSubject();//群对象
            request.setContact(groupSubject);//设置回复对象
        }
        //好友消息处理封装
        if (event instanceof FriendMessageEvent){

            request = new PrivateCommandRequest();
            request = commonProcess(event,request);
            //判断是否需要分发
            if (request == null){
                return;
            }
            FriendMessageEvent friendMessageEvent = (FriendMessageEvent) event;
            Friend friendSubject = friendMessageEvent.getSubject();//好友对象
            request.setContact(friendSubject); //设置请求中的回复好友对象

        }
        //调度器分发
        commandProcessor.execute(request);


    }
    //重复的处理过程
    public CommandRequest commonProcess(MessageEvent event, CommandRequest request){
        //拿到文本消息
        String msgStr = event.getMessage().contentToString();
        //不用做空判断 发不出来空消息的
        //转化为指令 判断是不是要直接过滤掉 指令过长
        String[] strings = msgStr.split("\\s+");//按空格分割
        if (strings[0].length() > 8){
            return null;
        }
        //可能是指令 处理去吧
        HashMap<String, String> paramMap = MapUtils.getParamMap(strings);
        String command = paramMap.get("0"); //这是指令 其他都是参数 单独封装
        //封装 common 共同参数 指令 指令参数与发送人信息
        request.setCommand(command);
        request.setParamMap(paramMap);

        User user = event.getSender();
        long id = user.getId();
        String nick = user.getNick();

        Sender sender = new Sender(id,nick);
        request.setSender(sender);
        return request;

    }
}
