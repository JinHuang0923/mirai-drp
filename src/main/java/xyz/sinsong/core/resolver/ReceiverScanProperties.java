package xyz.sinsong.core.resolver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jin Huang
 * @date 2021/11/1 9:48
 * 扫描包路径的配置类 读取配置文件 主要是接收器扫描的包
 */
@Data
public class ReceiverScanProperties {
    String scanPackage = "no.default.properties";
    String commandProcessor = "xyz.sinsong.core.dispatch.DispatchCommand";
}
