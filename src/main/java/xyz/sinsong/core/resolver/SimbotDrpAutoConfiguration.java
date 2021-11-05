package xyz.sinsong.core.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;
import xyz.sinsong.core.dispatch.CommandProcessor;
import xyz.sinsong.core.dispatch.DispatchCommand;
import xyz.sinsong.core.utils.PropertiesUtils;

/**
 * @author Jin Huang
 * @date 2021/11/1 9:50
 * 加载读取扫描接收器包路径的配置类
 */


@Slf4j
public class SimbotDrpAutoConfiguration {
    /**
     * 全局处理的也这里加载
     * @return
     */
    @Bean
    @DependsOn("receiverScanProperties")
    public CommandProcessor commandProcessor(ReceiverScanProperties receiverScanProperties) {
        String commandProcessor = receiverScanProperties.getCommandProcessor();
        //不是默认类才加载
        if (!"xyz.sinsong.core.dispatch.DispatchCommand".equals(commandProcessor) && StringUtils.hasText(commandProcessor)) {
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
    @Bean
    public ReceiverScanProperties receiverScanProperties(){
        return PropertiesUtils.loadScan();
    }


}
