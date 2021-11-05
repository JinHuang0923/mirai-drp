package xyz.sinsong.core.dispatch;

import xyz.sinsong.command.CommandRequest;
import xyz.sinsong.command.CommandResPonse;


public interface CommandProcessor {


    /**
     *消息整个事件的父接口 为了给灵活 可以交给使用者处理
     * @param
     * @return
     */
    CommandRequest init(CommandRequest request);
    CommandRequest before(CommandRequest request);
    CommandResPonse handle(CommandRequest request);
    CommandResPonse after(CommandResPonse commandResPonse);
    void reply(CommandResPonse commandResPonse);

    /**
     * 默认执行的方法 作为唯一调用入口
     * 管理指令处理 分发的生命周期(模板设计方式)
     * 这里依次进行
     * init() 全局调度器初始化
     * before() 执行之前
     * handle() 执行分发
     * after() 执行之后
     * reply() 回复消息
     * 可通过实现此接口 改变运行的过程
     * 之后考虑把处理请求事件对象的过程到init使用 可重写 有更多操作自定义的空间
     * @return
     */
    default void execute(CommandRequest request){
        CommandRequest request0 = init(request);
        CommandRequest request1 = before(request0);
        CommandResPonse handleResponse = handle(request1);
        CommandResPonse afterResponse = after(handleResponse);
        reply(afterResponse);
    }
}
