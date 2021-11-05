package xyz.sinsong.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jin Huang
 * @date 2021/11/2 13:55
 * 消息发送人
 * 最基础的发送人昵称与qq号信息
 *
 */
@Data
@AllArgsConstructor
public class Sender {
    long code;
    String nickName;
}
