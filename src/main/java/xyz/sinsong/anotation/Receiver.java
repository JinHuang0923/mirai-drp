package xyz.sinsong.anotation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @author Jin Huang
 * @date 2021/10/30 15:49
 * 打上本注解的为指令接收器
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
@Component //继承component 就不用打两个注解了
public @interface Receiver {
}
