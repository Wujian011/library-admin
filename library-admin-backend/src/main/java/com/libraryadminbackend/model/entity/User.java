package com.libraryadminbackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @TableId( type = IdType.AUTO)
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
     * 密码
     */
    private String password;
    /**
     * 真实姓名
     */
    private String name;
    /**
     * 借阅证号（仅读者）
     */
    private String cardNo;
    /**
     * 性别：1男 2女 0未知
     */
    private Integer sex;
    /**
     * 出生日期
     */
    private Date birth;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 账号状态：1启用 0禁用
     */
    private Integer status;
    /**
     * 最后登录时间
     */
    private Date lastLoginAt;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}