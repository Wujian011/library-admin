package com.libraryadminbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libraryadminbackend.common.ErrorCode;
import com.libraryadminbackend.constant.CommonConstant;
import com.libraryadminbackend.exception.BusinessException;
import com.libraryadminbackend.mapper.ClassInfoMapper;
import com.libraryadminbackend.model.dto.classinfo.ClassInfoQueryRequest;
import com.libraryadminbackend.model.entity.ClassInfo;
import com.libraryadminbackend.model.vo.classinfo.ClassInfoVO;
import com.libraryadminbackend.service.ClassInfoService;
import com.libraryadminbackend.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 图书分类服务实现
 */
@Service
@Slf4j
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements ClassInfoService {

    @Override
    public void validClassInfo(ClassInfo classInfo, boolean add) {
        if (classInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String className = classInfo.getClassName();

        // 创建时，参数不能为空
        if (add) {
            if (StrUtil.isBlank(className)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称不能为空");
            }
        }
        // 有参数则校验
        if (StrUtil.isNotBlank(className) && className.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称过长");
        }
    }

    @Override
    public QueryWrapper<ClassInfo> getQueryWrapper(ClassInfoQueryRequest classInfoQueryRequest) {
        QueryWrapper<ClassInfo> queryWrapper = new QueryWrapper<>();
        if (classInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long classId = classInfoQueryRequest.getClassId();
        String className = classInfoQueryRequest.getClassName();
        String sortField = classInfoQueryRequest.getSortField();
        String sortOrder = classInfoQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(classId != null, "class_id", classId);
        queryWrapper.like(StrUtil.isNotBlank(className), "class_name", className);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ClassInfoVO getClassInfoVO(ClassInfo classInfo) {
        if (classInfo == null) {
            return null;
        }
        ClassInfoVO classInfoVO = new ClassInfoVO();
        BeanUtils.copyProperties(classInfo, classInfoVO);
        return classInfoVO;
    }
}
