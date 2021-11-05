package xyz.sinsong.core.config;

import cn.hutool.setting.dialect.Props;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Jin Huang
 * @date 2021/11/2 10:54
 */
@Data
public class MiraiDrpProperties {
     String workspace;
     String heartbeatStrategy;
     long qq;
     String password;
     String scanPackage; //接收器包扫描路径

    public  String toString(){
        return "qq:" + qq + " 心跳策略:" + heartbeatStrategy + " 运行目录:" + workspace;
    }


}
