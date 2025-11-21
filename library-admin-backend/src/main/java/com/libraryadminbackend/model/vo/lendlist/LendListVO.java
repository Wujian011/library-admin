package com.libraryadminbackend.model.vo.lendlist;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 借阅记录视图
 */
@Data
public class LendListVO implements Serializable {

    /**
     * 借阅记录ID
     */
    private Long lendId;

    /**
     * 读者ID
     */
    private Long userId;

    /**
     * 读者姓名
     */
    private String userName;

    /**
     * 读者借阅证号
     */
    private String cardNo;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 书名
     */
    private String bookName;

    /**
     * ISBN
     */
    private String isbn;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
