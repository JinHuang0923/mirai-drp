package xyz.sinsong.core.load;

import lombok.extern.slf4j.Slf4j;
import xyz.sinsong.core.anotation.Receive;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Huang
 * @date 2021/10/30 19:31
 * 处理把带@Receiver的类对象交给 handlerFactory添加进去
 * 这里扫描类的所有方法 找出带@Receive注解的 指令处理方法
 * 加入保存指令 方法对象的map k:指令 v:处理指令的方法对象
 */
@Slf4j
public class ReceiverHandlerMappingFactory {

    //日志对象
//    private static Logger logger = Logger.getLogger(ServletHandlerMappingFactory.class);

    //保存 key 方法 的map key:指令 value:执行的方法 处理群聊指令的集
    private static Map<String, Method> receiverHandlerMapping = new HashMap<String, Method>();

    //处理私聊指令的集
    private static Map<String, Method> privateHandlerMapping = new HashMap<String, Method>();

    /**
     * 把加载进来的 带Receiver注解的对象 加入classmapping
     * @param clazz 带Receiver注解的运行时类对象
     */
    public static void addClassMapping(Class<?> clazz) {

        //key 指令
        String command = "";

        /*//获取类上的注解对象
        HandlerMapping handlerMapping = clazz.getAnnotation(HandlerMapping.class);
        if (handlerMapping != null) {
            // 类上的 requestMapping url路径
            url = handlerMapping.value();
        } else {
            //类上没有的话
            //拿到类的简单名字
            String classSimpleName = clazz.getSimpleName().toLowerCase();
            //url拼接 / 如果有servlet到servlet为止 例: /UserController
            url = "/" + classSimpleName.substring(0,
                    classSimpleName.indexOf("servlet"));
        }

        if (url != null) {
            //endswith() 方法用于判断字符串是否以指定后缀结尾
            if(url.endsWith("/")){
                //如果以/结尾 就截取到前面部分 不要最后的 /
                url = url.substring(url.length() - 1);
            }
            }
         */
            //加入处理器工厂 这里专门存放调用方法的实例对象
            ReceiverHandlerFactory.put(clazz);
//            log.info(" 加载处理的Receiver类对象:" + clazz.getName());
//            logger.info(" Load servlet handler class:" + clazz.getName() + " url:" + url);

            //扫描处理器方法 添加进 处理的 kv map里面 servletHandlerMapping
            scanHandlerMethod(clazz,command);

    }



    public static void scanHandlerMethod(Class<?> clazz,String classMapping) {

        //拿到此对象所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //遍历方法 获取每个方法的 Receive 注解对象
            Receive receive = method.getAnnotation(Receive.class);

            //判断是否获取到了注解对象(这个方法上含有这个注解) 并且注解对象 value 不是空的
            //如果为空说明这个方法不含有这个注解 略过
            if (receive != null && receive.value() != null) {
                //k 方法上要接收的指令
                String command = receive.value();
                //判断是否是接收私聊消息的 是的话应该加入的是私聊指令处理映射集
                if (receive.isPrivate()){ //默认是false 说明是私聊的 加入私聊映射集
                    addPrivateMethodMapping(command,method);
                }else {
                    //加入群聊的
                    addMethodMapping(command,method);

                }


//                //是否是以 / 开始 不是的话 就执行 给前面加上 /
//                if(!mapping.startsWith("/")){
//                    mapping = "/" + mapping;
//                }
//                //完整的key 就是 类上面的url + 方法上的url
//                mapping = classMapping + mapping;

                //调用方法尝试 加入这个map key:指令 value:处理指令的方法对象

            }
        }
    }

    private static void addPrivateMethodMapping(String command, Method method) {
        log.info(" 加载私聊处理指令方法, method:" + method.getName() + " for 指令:" + command);
        //通过key 获取一遍是否有重复加入的对象
        Method handlerMethod = receiverHandlerMapping.get(command);

        //有重复的已经加过一遍了 抛出异常
        if(handlerMethod != null){
            throw new IllegalArgumentException(" command :" + command + " is already mapped by :" + handlerMethod);
        }else{
            //没有重复的 加入这个私聊map
            privateHandlerMapping.put(command, method);
        }
    }

    public static void addMethodMapping(String command,Method method) {
        log.info(" 加载群聊处理指令方法, method:" + method.getName() + " for 指令:" + command);
//        logger.info(" Load servlet handler mapping, method:" + method.getName() + " for url:" + url);

        //通过key 获取一遍是否有重复加入的对象
        Method handlerMethod = receiverHandlerMapping.get(command);

        //有重复的已经加过一遍了 抛出异常
        if(handlerMethod != null){
            throw new IllegalArgumentException(" command :" + command + " is already mapped by :" + handlerMethod);
        }else{
            //没有重复的 加入这个map
            receiverHandlerMapping.put(command, method);
        }
    }

    /**
     * 外界 其他地方获取这一组指令映射的方法(如调度器)
     * @param command
     * @return
     */
    public static Method getMethodMapping(String command) {
        return receiverHandlerMapping.get(command);
    }

    /**
     * 外界 其他地方获取这一组私聊指令映射的方法(如调度器)
     * @param command
     * @return
     */
    public static Method getPrivateMethodMapping(String command) {
        //返回私聊集中的
        return privateHandlerMapping.get(command);
    }
}
