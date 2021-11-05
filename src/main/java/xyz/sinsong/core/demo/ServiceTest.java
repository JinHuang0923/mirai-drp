package xyz.sinsong.core.demo;

import net.mamoe.mirai.message.data.Message;
import org.springframework.stereotype.Service;
import xyz.sinsong.core.utils.MessageBuilderUtis;

/**
 * @author Jin Huang
 * @date 2021/11/2 15:42
 */
@Service
public class ServiceTest {
    public String test(){
        return "service注入成功!";
    }
}
