package xyz.sinsong.core.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;

/**
 * @author Jin Huang
 * @date 2021/11/2 13:07
 * 获取ioc容器中的bean的工具类 监听ioc容器加载 加载完成之后注入本类 提供getBean方法
 */
@Slf4j
@Order(-1)
public class IocUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext; // Spring应用上下文环境

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("ioc容器加载完成,注入IOCUtil");
        IocUtil.applicationContext = applicationContext;
    }

    public static Object getBeanByName(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> classType) throws BeansException {
        return applicationContext.getBean(classType);
    }
    public static void setIoc(ApplicationContext applicationContext){
        IocUtil.applicationContext = applicationContext;
    }
}
