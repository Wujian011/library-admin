package com.libraryadminbackend.model.vo.classinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图书分类视图（脱敏）
 */
@Data
public class ClassInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 分类ID
     */
    private Long classId;
    /**
     * 分类名称
     */
    private String className;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
