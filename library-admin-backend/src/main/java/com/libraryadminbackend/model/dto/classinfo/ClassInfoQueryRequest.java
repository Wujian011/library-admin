package com.libraryadminbackend.model.dto.classinfo;

import com.libraryadminbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 图书分类查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClassInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分类ID
     */
    private Long classId;
    /**
     * 分类名称
     */
    private String className;
}
