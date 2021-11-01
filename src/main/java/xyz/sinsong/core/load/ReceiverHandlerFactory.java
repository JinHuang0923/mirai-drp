package xyz.sinsong.core.load;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Huang
 * @date 2021/10/31 9:06
 * 存储实例对象
 */
@Slf4j
public class ReceiverHandlerFactory {
    //日志对象
//    private static Logger logger = Logger.getLogger(ServletHandlerFactory.class);

    //保存Receiver类 示例对象的map key是className
    private static Map<String,Object> classes = new HashMap<String,Object>();




    /**
     * 放进这个工厂 这个方法会将这些类实例化对象 保存在classes 这个map中
     * @param clazz
     */
    public static void put(Class<?> clazz){
        try {
//            log.info("初始化ReceiverHandler类:"+ clazz.getName());
//            logger.info("初始化ServletHandler类:"+ clazz.getName());
            //通过类的运行时对象 获取ioc容器中的实例化对象
            Object receiver = StartAddDataListener.getBean(clazz);
//            Object receiver = clazz.newInstance();

            //放入存储这个对象的map中 k:类名 v:类对象
            classes.put(clazz.getName(), receiver);
            log.info("初始化ReceiverHandler类成功:"+ clazz.getName());


        } catch (Exception e) {
            log.error("初始化ReceiverHandler类:" + clazz.getName() + "失败:" + e.getMessage());
//            logger.error("初始化Servlet类:" + clazz.getName() + "失败:" + e.getMessage());
        }
    }

    /**
     *
     * 提供get方法 可从外界通过类名取出这个类(特指调度器用指令映射map中拿到方法再拿到类名调用)
     * get方法 通过类名取出这个类
     * 应该是dispatchservlet 分发请求的时候调用出来好执行
     * @param className
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")//告诉编译器忽略 unchecked 警告信息，如使用List，ArrayList等未进行参数化产生的警告信息。
    public static <T> T get(String className){
        return (T)classes.get(className);
    }
}
