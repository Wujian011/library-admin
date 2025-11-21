package com.libraryadminbackend.model.dto.lendlist;

import com.libraryadminbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 借阅记录查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LendListQueryRequest extends PageRequest implements Serializable {

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
     * 借阅状态：1借出 2已还 3逾期 4丢失
     */
    private Integer status;

    /**
     * 借出日期起始
     */
    private Date lendDateStart;

    /**
     * 借出日期结束
     */
    private Date lendDateEnd;

    private static final long serialVersionUID = 1L;
}
