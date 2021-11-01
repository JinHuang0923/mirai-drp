package xyz.sinsong.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jin Huang
 * @date 2021/10/29 17:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AUser extends BaseEntity {
    /**
     * 用户昵称(一般使用qq的)
     */
    private String nickName;

    /**
     * 用户的qq号
     */
    private Long qqNumber;

}