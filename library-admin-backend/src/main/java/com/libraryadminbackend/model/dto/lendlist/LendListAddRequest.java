package com.libraryadminbackend.model.dto.lendlist;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 借阅记录添加请求
 */
@Data
public class LendListAddRequest implements Serializable {

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
     * 备注信息
     */
    private String note;

    private static final long serialVersionUID = 1L;
}
