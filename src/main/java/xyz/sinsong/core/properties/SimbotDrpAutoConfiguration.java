package xyz.sinsong.core.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.sinsong.core.dispatch.CommandProcessor;
import xyz.sinsong.core.dispatch.DispatchCommand;

/**
 * @author Jin Huang
 * @date 2021/11/1 9:50
 * 加载读取扫描接收器包路径的配置类
 */
@Configuration
@EnableConfigurationProperties(ReceiverScanProperties.class)
@Slf4j
//@ConditionalOnProperty(prefix = "simbot-drp")
public class SimbotDrpAutoConfiguration {

    public final ReceiverScanProperties receiverScanProperties;

    public SimbotDrpAutoConfiguration(ReceiverScanProperties receiverScanProperties) {
        this.receiverScanProperties = receiverScanProperties;
    }

    @ConditionalOnMissingBean
    @Bean
    public ReceiverConfig receiverConfig() {
        return new ReceiverConfig(receiverScanProperties);
    }


    /**
     * 全局处理的也这里加载
     * @return
     */
    @Bean
    public CommandProcessor commandProcessor() {
        String commandProcessor = this.receiverScanProperties.getCommandProcessor();
        //不是默认类才加载
        if (!"xyz.sinsong.core.dispatch.DispatchCommand".equals(commandProcessor)) {
            log.info("加载自定义全局调度处理类:{}", commandProcessor);
            try {
                Class<?> aClass = SimbotDrpAutoConfiguration.class.getClassLoader().loadClass(commandProcessor);
                return (CommandProcessor) aClass.newInstance();
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException,未找到类:{},使用默认指令调度器:xyz.sinsong.core.dispatch.DispatchCommand", commandProcessor);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return new DispatchCommand();
    }


}
