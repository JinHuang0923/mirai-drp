package xyz.sinsong.core.component;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.sinsong.core.dispatch.CommandProcessor;
import xyz.sinsong.core.dispatch.DispatchCommand;
import xyz.sinsong.core.listener.GlobalListener;
import xyz.sinsong.core.load.StartAddDataListener;
import xyz.sinsong.core.utils.MessageBuiderUtils;

/**
 * @author Jin Huang
 * @date 2021/11/1 12:32
 * 这里自动装配用到的ioc容器类
 *
 */
@Configuration
public class ComponentConfiguration {

    //*******无循环依赖的类 可直接放进容器*********
//    @Bean
//    public DispatchCommand dispatchCommand(){
//        return new DispatchCommand();
//    }
//
    //   ioc容器初始化完毕事件监听器 这里好像不行 不交给springioc 监听的方法并不会调用
//    @Bean
//    public StartAddDataListener startAddDataListener(){
//        return new StartAddDataListener();
//    }
    //*******有循环依赖的类 先调空参构造器放入容器 之后ioc加载完毕再注入*********
    @Bean
    public GlobalListener globalListener(){
        return new GlobalListener();
    }
    @Bean
    public MessageBuiderUtils messageBuiderUtils(){
        return new MessageBuiderUtils();
    }
}
