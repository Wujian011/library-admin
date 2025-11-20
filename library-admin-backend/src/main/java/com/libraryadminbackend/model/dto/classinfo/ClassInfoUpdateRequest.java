package com.libraryadminbackend.model.dto.classinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 图书分类更新请求
 */
@Data
public class ClassInfoUpdateRequest implements Serializable {

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
