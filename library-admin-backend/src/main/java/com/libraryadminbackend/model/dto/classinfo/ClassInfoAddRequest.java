package com.libraryadminbackend.model.dto.classinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 图书分类创建请求
 */
@Data
public class ClassInfoAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 分类名称
     */
    private String className;
}
