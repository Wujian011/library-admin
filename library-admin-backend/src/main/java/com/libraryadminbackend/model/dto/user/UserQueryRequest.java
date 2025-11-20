package com.libraryadminbackend.model.dto.user;

import com.libraryadminbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户类型：1管理员 2读者
     */
    private Integer userType;
    /**
     * 登录用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String name;
    /**
     * 借阅证号
     */
    private String cardNo;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 账号状态：1启用 0禁用
     */
    private Integer status;
}