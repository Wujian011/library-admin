package com.libraryadminbackend.model.dto.bookinfo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 图书信息更新请求
 */
@Data
public class BookInfoUpdateRequest implements Serializable {
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
     * 出版日期
     */
    private Date pubTime;
    /**
     * 语言
     */
    private String language;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 藏书总数
     */
    private Integer totalQty;
    /**
     * 可借数量
     */
    private Integer availableQty;
    /**
     * 图书简介
     */
    private String introduction;
    /**
     * 所属分类ID
     */
    private Long classId;
    /**
     * 上架状态：1上架 0下架
     */
    private Integer status;
}
