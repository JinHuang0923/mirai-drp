package xyz.sinsong.core.utils;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.util.StringUtils;
import xyz.sinsong.core.config.MiraiDrpProperties;

import java.io.File;

/**
 * @author Jin Huang
 * @date 2021/11/2 12:22
 */
@Slf4j
public class BotFactory {
    public static Bot loadBot(MiraiDrpProperties miraiDrpProperties) {
        log.info("加载配置:{}",miraiDrpProperties);
        BotConfiguration botConfiguration = new BotConfiguration();
        String heartbeatStrategy = miraiDrpProperties.getHeartbeatStrategy();
        long qq = miraiDrpProperties.getQq();
        String password = miraiDrpProperties.getPassword();

        //有自定义心跳策略
        switch (heartbeatStrategy) {
            case "REGISTER":
                botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.REGISTER);
                break;
            case "NONE":
                botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.NONE);
        }
        //没有 默认就是STAT_HB 不用设置
        //工作目录
        String workspace = miraiDrpProperties.getWorkspace();
        if (StringUtils.hasText(workspace)){
            botConfiguration.setWorkingDir(new File(workspace));
        }
        //存储设备信息
        botConfiguration.fileBasedDeviceInfo();
        //机器人创建
        return net.mamoe.mirai.BotFactory.INSTANCE.newBot(qq, password, botConfiguration);

    }
}
