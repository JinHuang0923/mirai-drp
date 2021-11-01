package xyz.sinsong.core.utils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Jin Huang
 * @date 2021/10/26 14:22
 */

@Slf4j
public class MessageBuiderUtils {

    private MessageContentBuilderFactory builderFactory;

    public MessageContentBuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    public void setBuilderFactory(MessageContentBuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
        log.info("消息构建工具注入消息构建器工厂:{}",builderFactory);
    }

    public MessageBuiderUtils() {
        log.info("消息构建器工具类初始创建");
    }
    /**
     * 构建返回文字消息的方法
     * @return
     */
    public MessageContent buildMessage(String... texts){
        MessageContentBuilder messageContentBuilder = builderFactory.getMessageContentBuilder();
        for (int i = 0; i < texts.length; i++) {
            messageContentBuilder.text(texts[i]);
        }
        return messageContentBuilder.build();
    }
    public MessageContentBuilder getMessageBuilder(){
        return builderFactory.getMessageContentBuilder();
    }
}
