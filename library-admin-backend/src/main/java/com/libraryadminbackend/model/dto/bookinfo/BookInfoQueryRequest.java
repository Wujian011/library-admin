package com.libraryadminbackend.model.dto.bookinfo;

import com.libraryadminbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 图书信息查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 图书ID
     */
    private Long bookId;
    /**
     * 书名
     */
    private String name;
    /**
     * 作者
     */
    private String author;
    /**
     * 国际标准书号
     */
    private String isbn;
    /**
     * 出版社
     */
    private String publisher;
    /**
     * 语言
     */
    private String language;
    /**
     * 所属分类ID
     */
    private Long classId;
    /**
     * 上架状态：1上架 0下架
     */
    private Integer status;
}
