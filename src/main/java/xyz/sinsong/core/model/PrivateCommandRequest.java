package xyz.sinsong.core.model;

import love.forte.simbot.api.message.containers.AccountInfo;

/**
 * @author Jin Huang
 * @date 2021/11/1 16:10
 * 私聊消息请求对象的实现类
 */
public class PrivateCommandRequest extends AbstractCommandRequest{
    private AccountInfo accountInfo;

    public PrivateCommandRequest(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public PrivateCommandRequest() {
    }
}
