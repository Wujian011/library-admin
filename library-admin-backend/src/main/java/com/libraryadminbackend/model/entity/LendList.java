package com.libraryadminbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 借阅记录实体
 */
@TableName(value = "lendlist")
@Data
public class LendList implements Serializable {

    /**
     * 借阅记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long lendId;

    /**
     * 读者ID
     */
    private Long userId;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 借出日期
     */
    private Date lendDate;

    /**
     * 应还日期
     */
    private Date dueDate;

    /**
     * 实际归还时间
     */
    private Date backDate;

    /**
     * 借阅状态：1借出 2已还 3逾期 4丢失
     */
    private Integer status;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 创建用户id
     */
    private Long userIdCreator;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
