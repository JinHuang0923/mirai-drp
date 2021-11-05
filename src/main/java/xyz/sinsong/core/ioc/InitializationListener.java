package xyz.sinsong.core.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import xyz.sinsong.core.load.ReceiverHandlerMappingResolver;
import xyz.sinsong.core.resolver.ReceiverScanProperties;
import xyz.sinsong.core.utils.PropertiesUtils;

/**
 * @author Jin Huang
 * @date 2021/11/2 15:09
 * 监听ioc容器初始化完毕 执行包扫描
 */
@Slf4j
@Order(1)
public class InitializationListener implements ApplicationListener<ContextRefreshedEvent> {

//    private static ApplicationContext applicationContext; // Spring应用上下文环境
//
//    public static Object getBeanByName(String name) {
//        return applicationContext.getBean(name);
//    }
//
//    public static <T> T getBean(Class<T> classType) throws BeansException {
//        return applicationContext.getBean(classType);
//    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //除了spring的 还有我们自己的子容器(所以会重复触发两次这个事件)
        // 我们只需要调用一次 加载完spring applicationContext 调用一次就行
        if (event.getApplicationContext().getParent() == null){
//            ApplicationContext applicationContext = event.getApplicationContext();
//            InitializationListener.applicationContext = applicationContext;
//            //装载iocutils的ioc容器上下文
//            loadIocUtils(applicationContext);
            //不调用两次ioc 那个应该会更快执行
            log.info("ioc容器初始化完成,执行手动注入操作,包扫描");
            //扫描配置文件 获取扫描包路径 与指定的调度器
            ReceiverScanProperties receiverScanProperties = IocUtil.getBean(ReceiverScanProperties.class);
            String basePackage = receiverScanProperties.getScanPackage();
            log.info("开始加载包下接收器资源:{}",basePackage);
            ReceiverHandlerMappingResolver receiverHandlerMappingResolver = new ReceiverHandlerMappingResolver();
            receiverHandlerMappingResolver.setPackagesToScan(new String[]{basePackage});
        }


    }

//    private void loadIocUtils(ApplicationContext applicationContext) {
//        IocUtil.setIoc(applicationContext);
//    }
}
