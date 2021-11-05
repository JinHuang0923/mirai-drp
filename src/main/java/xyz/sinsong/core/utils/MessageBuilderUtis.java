package xyz.sinsong.core.utils;

import net.mamoe.mirai.message.data.*;

/**
 * @author Jin Huang
 * @date 2021/11/2 15:15
 * 消息构建的工具类
 */
public class MessageBuilderUtis {
    /**
     * 构建简单的文本消息
     * @param text
     * @return
     */
    public static Message buildMessage(String... text) {
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        for (int i = 0; i < text.length; i++) {
            messageChainBuilder.append(new PlainText(text[i]));
        }
        return messageChainBuilder.build();
    }

    /**
     * 单条消息
     * @param onText
     * @return
     */
    public static Message buildMessage(String onText){
        return new PlainText(onText);
    }

    /**
     * 字符序列 可接收StringBuider等 方便
     * @param sequence
     * @return
     */
    public static Message buildMessage(CharSequence sequence){
        return new MessageChainBuilder().append(sequence).build();
    }
}
