package xyz.sinsong.core.utils;

import cn.hutool.setting.dialect.Props;
import org.springframework.util.StringUtils;
import xyz.sinsong.core.config.MiraiDrpProperties;
import xyz.sinsong.core.resolver.ReceiverScanProperties;

/**
 * @author Jin Huang
 * @date 2021/11/2 12:16
 */
public class PropertiesUtils {
    public static MiraiDrpProperties loadConfiguration(){
        //1.读取配置信息
        Props props = Props.getProp("mirai-drp.properties");// resource 目录下读取配置文件
        Long qq = props.getLong("mirai-drp.bot.qq");
        String password = props.getStr("mirai-drp.bot.password");
        String workspace = props.getStr("mirai-drp.workspace");
        String heartbeatStrategy = props.getStr("mirai-drp.heartbeatStrategy");
        //2.创建配置文件对象
        MiraiDrpProperties miraiDrpProperties = new MiraiDrpProperties();
        miraiDrpProperties.setQq(qq);
        miraiDrpProperties.setPassword(password);
        miraiDrpProperties.setWorkspace(workspace);
        miraiDrpProperties.setHeartbeatStrategy(heartbeatStrategy);

        return miraiDrpProperties;
    }
    public static ReceiverScanProperties loadScan(){
        //1.读取配置信息
        Props props = Props.getProp("mirai-drp.properties");// resource 目录下读取配置文件
        String scanPackge = props.getStr("mirai-drp.scanPackage");//扫描接收器的包
        String commandProcessor = props.getStr("mirai-drp.commandProcessor");//指定调度器

        //2.创建配置文件对象
        ReceiverScanProperties receiverScanProperties = new ReceiverScanProperties();
        if (StringUtils.hasText(commandProcessor)){
            receiverScanProperties.setCommandProcessor(commandProcessor);
        }
        receiverScanProperties.setScanPackage(scanPackge);

        return receiverScanProperties;
    }
}
