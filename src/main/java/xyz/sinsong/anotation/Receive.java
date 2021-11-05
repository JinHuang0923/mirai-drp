package xyz.sinsong.anotation;

import java.lang.annotation.*;

/**
 * @author Jin Huang
 * @date 2021/10/30 15:42
 * 打上本注解为处理指令的方法
 * value是指令名
 */
@Target({ElementType.METHOD}) //只能加在方法上
@Retention(RetentionPolicy.RUNTIME) //注解的保留生命周期 1:源码 2:编译时存在class文件 3:运行时也保留
@Inherited //当@InheritedAnno注解加在某个类A上时，假如类B继承了A，则B也会带上该注解。
@Documented ////编译成doc文档时会显示一些说明
public @interface Receive {
    //处理的指令名
    String value() default "";
    boolean isPrivate() default false;
}
