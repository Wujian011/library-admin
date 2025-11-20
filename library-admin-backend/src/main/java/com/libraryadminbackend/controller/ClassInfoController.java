package com.libraryadminbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libraryadminbackend.annotation.AuthCheck;
import com.libraryadminbackend.common.BaseResponse;
import com.libraryadminbackend.common.DeleteRequest;
import com.libraryadminbackend.common.ErrorCode;
import com.libraryadminbackend.common.ResultUtils;
import com.libraryadminbackend.constant.UserConstant;
import com.libraryadminbackend.exception.BusinessException;
import com.libraryadminbackend.exception.ThrowUtils;
import com.libraryadminbackend.model.dto.classinfo.ClassInfoAddRequest;
import com.libraryadminbackend.model.dto.classinfo.ClassInfoQueryRequest;
import com.libraryadminbackend.model.dto.classinfo.ClassInfoUpdateRequest;
import com.libraryadminbackend.model.entity.ClassInfo;
import com.libraryadminbackend.service.ClassInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图书分类接口
 */
@RestController
@RequestMapping("/classinfo")
@Slf4j
public class ClassInfoController {

    @Resource
    private ClassInfoService classInfoService;

    // region 增删改查

    /**
     * 添加分类
     *
     * @param classInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addClassInfo(@RequestBody ClassInfoAddRequest classInfoAddRequest, HttpServletRequest request) {
        if (classInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(classInfoAddRequest, classInfo);
        // 校验分类名称
        if (StrUtil.isBlank(classInfo.getClassName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称不能为空");
        }
        boolean result = classInfoService.save(classInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(classInfo.getClassId());
    }

    /**
     * 删除分类
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteClassInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = classInfoService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新分类
     *
     * @param classInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateClassInfo(@RequestBody ClassInfoUpdateRequest classInfoUpdateRequest,
                                                  HttpServletRequest request) {
        if (classInfoUpdateRequest == null || classInfoUpdateRequest.getClassId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(classInfoUpdateRequest, classInfo);
        boolean result = classInfoService.updateById(classInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取分类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ClassInfo> getClassInfoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ClassInfo classInfo = classInfoService.getById(id);
        ThrowUtils.throwIf(classInfo == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(classInfo);
    }

    /**
     * 分页获取分类列表
     *
     * @param classInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ClassInfo>> listClassInfoByPage(@RequestBody ClassInfoQueryRequest classInfoQueryRequest,
                                                              HttpServletRequest request) {
        long current = classInfoQueryRequest.getCurrent();
        long size = classInfoQueryRequest.getPageSize();
        Page<ClassInfo> classInfoPage = classInfoService.page(new Page<>(current, size),
                classInfoService.getQueryWrapper(classInfoQueryRequest));
        return ResultUtils.success(classInfoPage);
    }

    // endregion
}
