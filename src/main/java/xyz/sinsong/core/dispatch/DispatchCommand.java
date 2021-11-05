package xyz.sinsong.core.dispatch;

import lombok.extern.slf4j.Slf4j;
import xyz.sinsong.command.CommandRequest;
import xyz.sinsong.command.CommandResPonse;

/**
 * @author Jin Huang
 * @date 2021/10/30 15:26
 * 统一全局指令处理器
 * 这里会将指令派遣到不同注解声明的指令方法上
 * 具体的实现已经再抽象类中处理
 * 这里可以对抽象类的全局派发初始化 前置 后置 三个方法进行重写处理
 */
@Slf4j
public class DispatchCommand extends AbstractCommandProcessor {

    @Override
    public CommandRequest init(CommandRequest request) {
        return request;
    }

    @Override
    public CommandRequest before(CommandRequest request) {
        return request;
    }

    @Override
    public CommandResPonse after(CommandResPonse commandResPonse) {
        return commandResPonse;
    }
}

