package xyz.sinsong.core.model;

import love.forte.simbot.api.message.containers.GroupInfo;

import java.util.HashMap;

/**
 * @author Jin Huang
 * @date 2021/11/1 16:00
 */
public class GroupCommandRequest extends AbstractCommandRequest {
    private GroupInfo groupInfo;


    public GroupCommandRequest(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public GroupCommandRequest() {
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

}
