package xyz.sinsong.core.load;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import xyz.sinsong.core.dispatch.CommandProcessor;
import xyz.sinsong.core.dispatch.DispatchCommand;
import xyz.sinsong.core.listener.GlobalListener;
import xyz.sinsong.core.properties.ReceiverConfig;
import xyz.sinsong.core.utils.MessageBuiderUtils;

/**
 * @author Jin Huang
 * @date 2021/10/31 12:12
 * 监听加载器类
 * 当spring容器全都加载完成之后再加载这个类 执行其中的 onApplicationEvent方法 ApplicationListener<ContextRefreshedEvent>
 * 实现ApplicationContextAware接口 可以通过setApplicationContext 接收IOC容器
 *
 * 这里拿到ioc容器 再调用接收器的包扫描 加载接收器
 */
//@Service
@Slf4j
public class StartAddDataListener implements ApplicationContextAware {

    public StartAddDataListener() {
        log.info("StartAddDataListener创建完毕,等待IOC容器初始化完成");
    }

    private static ApplicationContext applicationContext; // Spring应用上下文环境


    public void onApplicationEvent() {
        log.info("IOC初始化完成,注入全局调度器...");
        //1.给消息构建工具类 注入消息构建器工厂
            //1.1 拿到消息工具类 拿到消息构建器工厂(从容器中)
        MessageBuiderUtils buiderUtils = getBean(MessageBuiderUtils.class);
        MessageContentBuilderFactory factory = getBean(MessageContentBuilderFactory.class);
            //1.2 set注入
        buiderUtils.setBuilderFactory(factory);

        //2.给全局消息监听器 注入全局指令调度器

        ReceiverConfig receiverConfig = getBean(ReceiverConfig.class);
//        String commandProcessor = receiverConfig.getCommandProcessor();
        //这里拿的全类名 可以通过全类名拿到ioc容器已经注入的commandprocessor对象 好像可以直接用接口.class拿

        GlobalListener globalListener = getBean(GlobalListener.class);
        CommandProcessor processor =  getBean(CommandProcessor.class);
        globalListener.setDispatchCommand(processor);

        //3.接收器扫描包路径 从配置类中取出来(之前已经从ApplicationContext中先取出对象)
        //4.创建接收器扫描工具 设置扫描包路径参数 执行扫描

        String basePackage = receiverConfig.getBasePackage();
        log.info("开始加载包下接收器资源:{}",basePackage);
            ReceiverHandlerMappingResolver receiverHandlerMappingResolver = new ReceiverHandlerMappingResolver();
        receiverHandlerMappingResolver.setPackagesToScan(new String[]{basePackage});
//        }
//        //或者下面这种方式
//        if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext"))
//        {
//            System.out.println("\n\n\n_________\n\n加载一次的 \n\n ________\n\n\n\n");
//        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//       log.info("获取spring容器上下文!");
        StartAddDataListener.applicationContext = applicationContext;
        //执行后面的注入方法
        onApplicationEvent();
    }
    /**
     * 获取ioc容器中的对象
     * 通过类的对象类型 获取ioc容器中的实例对象
     * @param
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> classType) throws BeansException {
        return applicationContext.getBean(classType);
    }

//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
//            setApplicationContext(event.getApplicationContext());
//            //执行容器加载完成之后的内容
//            onApplicationEvent();
//        }
//    }
}
