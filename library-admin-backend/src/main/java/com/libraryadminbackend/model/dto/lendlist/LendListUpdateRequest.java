package com.libraryadminbackend.model.dto.lendlist;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 借阅记录更新请求
 */
@Data
public class LendListUpdateRequest implements Serializable {

    /**
     * 借阅记录ID
     */
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

    private static final long serialVersionUID = 1L;
}
