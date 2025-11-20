-- ============================================
-- 图书管理系统数据库初始化脚本
-- ============================================

-- 创建数据库，使用 utf8mb4 字符集支持完整的 Unicode 字符（包括 emoji）
CREATE DATABASE IF NOT EXISTS library_admin
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE library_admin;

-- ============================================
-- 用户相关表
-- ============================================

-- 用户表：存储管理员和读者信息
CREATE TABLE user
(
    user_id       BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    user_type     TINYINT      NOT NULL COMMENT '用户类型：1管理员 2读者',
    username      VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录用户名',
    password      VARCHAR(255) NOT NULL COMMENT '密码',
    name          VARCHAR(50)  NULL COMMENT '真实姓名',
    card_no       VARCHAR(30)  NULL UNIQUE COMMENT '借阅证号（仅读者）',
    sex           TINYINT      NULL COMMENT '性别：1男 2女 0未知',
    birth         DATE         NULL COMMENT '出生日期',
    address       VARCHAR(255) NULL COMMENT '联系地址',
    phone         VARCHAR(30)  NULL COMMENT '联系电话',
    status        TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '账号状态：1启用 0禁用',
    last_login_at DATETIME     NULL COMMENT '最后登录时间',
    userId        BIGINT       NULL COMMENT '创建用户id',
    createTime    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete      TINYINT      NOT NULL DEFAULT 0 COMMENT '是否删除',
    INDEX idx_user_type (user_type)
) ENGINE = InnoDB COMMENT ='用户表';

-- ============================================
-- 图书相关表
-- ============================================

-- 图书分类表：存储图书分类信息
CREATE TABLE class_info
(
    class_id   BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    class_name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    userId     BIGINT      NULL COMMENT '创建用户id',
    createTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete   TINYINT     NOT NULL DEFAULT 0 COMMENT '是否删除'
) ENGINE = InnoDB COMMENT ='图书分类表';

-- 图书信息表：存储图书详细信息和库存
CREATE TABLE book_info
(
    book_id       BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '图书ID',
    name          VARCHAR(200)    NOT NULL COMMENT '书名',
    author        VARCHAR(100)    NULL COMMENT '作者',
    ISBN          VARCHAR(20)     NULL COMMENT '国际标准书号',
    publisher     VARCHAR(100)    NULL COMMENT '出版社',
    pub_time      DATE            NULL COMMENT '出版日期',
    language      VARCHAR(50)     NULL COMMENT '语言',
    price         DECIMAL(10, 2)  NULL COMMENT '价格',
    total_qty     INT             NOT NULL DEFAULT 0 COMMENT '藏书总数',
    available_qty INT             NOT NULL DEFAULT 0 COMMENT '可借数量',
    introduction  TEXT            NULL COMMENT '图书简介',
    class_id      BIGINT UNSIGNED NULL COMMENT '所属分类ID',
    status        TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '上架状态：1上架 0下架',
    userId        BIGINT          NULL COMMENT '创建用户id',
    createTime    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete      TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除',
    CONSTRAINT uq_book_isbn UNIQUE (ISBN)
) ENGINE = InnoDB COMMENT ='图书信息表';

-- ============================================
-- 借阅相关表
-- ============================================

-- 借阅记录表：存储图书借阅和归还记录
CREATE TABLE lendlist
(
    lend_id    BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '借阅记录ID',
    user_id    BIGINT UNSIGNED NOT NULL COMMENT '读者ID',
    book_id    BIGINT UNSIGNED NOT NULL COMMENT '图书ID',
    lend_date  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借出日期',
    due_date   DATETIME        NOT NULL COMMENT '应还日期',
    back_date  DATETIME        NULL COMMENT '实际归还时间',
    status     TINYINT         NOT NULL DEFAULT 1 COMMENT '借阅状态：1借出 2已还 3逾期 4丢失',
    note       VARCHAR(255)    NULL COMMENT '备注信息',
    userId     BIGINT          NULL COMMENT '创建用户id',
    createTime DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete   TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除'
) ENGINE = InnoDB COMMENT ='借阅记录表';

-- ============================================
-- 权限相关表（可选）
-- ============================================
-- 说明：如果系统只需要区分管理员和读者两种角色，上面的 admin 和 reader 表已足够
-- 如果需要更细粒度的权限控制（如超级管理员、普通管理员等），可以使用以下角色表

-- 角色表：定义系统角色
CREATE TABLE role
(
    role_id    BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    code       VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码，如：ADMIN, READER',
    name       VARCHAR(50) NOT NULL COMMENT '角色名称',
    userId     BIGINT      NULL COMMENT '创建用户id',
    createTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete   TINYINT     NOT NULL DEFAULT 0 COMMENT '是否删除'
) ENGINE = InnoDB COMMENT ='角色表';

-- 用户角色关联表：将角色分配给用户
CREATE TABLE user_role
(
    id         BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    user_id    BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    role_id    BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    userId     BIGINT          NULL COMMENT '创建用户id',
    createTime DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete   TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY uq_user_role (user_id, role_id)
) ENGINE = InnoDB COMMENT ='用户角色关联表';

-- ============================================
-- 索引优化
-- ============================================
-- 为常用查询字段创建索引，提升查询性能

-- 图书信息表索引
CREATE INDEX idx_book_name ON book_info (name); -- 按书名搜索
CREATE INDEX idx_book_author ON book_info (author); -- 按作者搜索
CREATE INDEX idx_book_class ON book_info (class_id);
-- 按分类查询

-- 用户表索引
CREATE INDEX idx_user_name ON user (name); -- 按姓名搜索
CREATE INDEX idx_user_phone ON user (phone);
-- 按电话搜索

-- 借阅记录表索引
CREATE INDEX idx_lend_user ON lendlist (user_id); -- 查询用户的借阅记录
CREATE INDEX idx_lend_book ON lendlist (book_id); -- 查询图书的借阅记录
CREATE INDEX idx_lend_status ON lendlist (status); -- 按借阅状态查询
CREATE INDEX idx_lend_dates ON lendlist (lend_date, due_date, back_date); -- 按日期范围查询

