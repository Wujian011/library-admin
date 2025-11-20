package com.libraryadminbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.libraryadminbackend.model.dto.classinfo.ClassInfoQueryRequest;
import com.libraryadminbackend.model.entity.ClassInfo;
import com.libraryadminbackend.model.vo.classinfo.ClassInfoVO;

/**
 * 图书分类服务
 */
public interface ClassInfoService extends IService<ClassInfo> {

    /**
     * 校验数据
     *
     * @param classInfo
     * @param add       对创建的数据进行校验
     */
    void validClassInfo(ClassInfo classInfo, boolean add);

    /**
     * 获取查询条件
     *
     * @param classInfoQueryRequest
     * @return
     */
    QueryWrapper<ClassInfo> getQueryWrapper(ClassInfoQueryRequest classInfoQueryRequest);

    /**
     * 获取图书分类封装
     *
     * @param classInfo
     * @return
     */
    ClassInfoVO getClassInfoVO(ClassInfo classInfo);
}
