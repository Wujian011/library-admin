package com.libraryadminbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libraryadminbackend.annotation.AuthCheck;
import com.libraryadminbackend.common.BaseResponse;
import com.libraryadminbackend.common.DeleteRequest;
import com.libraryadminbackend.common.ErrorCode;
import com.libraryadminbackend.common.ResultUtils;
import com.libraryadminbackend.constant.UserConstant;
import com.libraryadminbackend.exception.BusinessException;
import com.libraryadminbackend.exception.ThrowUtils;
import com.libraryadminbackend.model.dto.lendlist.LendListAddRequest;
import com.libraryadminbackend.model.dto.lendlist.LendListQueryRequest;
import com.libraryadminbackend.model.dto.lendlist.LendListUpdateRequest;
import com.libraryadminbackend.model.entity.LendList;
import com.libraryadminbackend.model.entity.User;
import com.libraryadminbackend.model.vo.lendlist.LendListVO;
import com.libraryadminbackend.service.LendListService;
import com.libraryadminbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 借阅记录接口
 */
@RestController
@RequestMapping("/lendlist")
@Slf4j
public class LendListController {

    @Resource
    private LendListService lendListService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 添加借阅记录（借书）
     *
     * @param lendListAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addLendList(@RequestBody LendListAddRequest lendListAddRequest, HttpServletRequest request) {
        if (lendListAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (lendListAddRequest.getUserId() == null || lendListAddRequest.getBookId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID和图书ID不能为空");
        }
        if (lendListAddRequest.getDueDate() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应还日期不能为空");
        }

        Long lendId = lendListService.borrowBook(
                lendListAddRequest.getUserId(),
                lendListAddRequest.getBookId(),
                lendListAddRequest.getDueDate(),
                lendListAddRequest.getNote()
        );
        return ResultUtils.success(lendId);
    }

    /**
     * 删除借阅记录
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteLendList(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = lendListService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新借阅记录
     *
     * @param lendListUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateLendList(@RequestBody LendListUpdateRequest lendListUpdateRequest,
                                                 HttpServletRequest request) {
        if (lendListUpdateRequest == null || lendListUpdateRequest.getLendId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LendList lendList = new LendList();
        BeanUtils.copyProperties(lendListUpdateRequest, lendList);
        boolean result = lendListService.updateById(lendList);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 还书
     *
     * @param lendId
     * @param request
     * @return
     */
    @PostMapping("/return")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> returnBook(@RequestParam Long lendId, HttpServletRequest request) {
        if (lendId == null || lendId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = lendListService.returnBook(lendId);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取借阅记录
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<LendListVO> getLendListById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LendList lendList = lendListService.getById(id);
        ThrowUtils.throwIf(lendList == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(lendListService.getLendListVO(lendList));
    }

    /**
     * 分页获取借阅记录列表（管理员）
     *
     * @param lendListQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<LendListVO>> listLendListByPage(@RequestBody LendListQueryRequest lendListQueryRequest,
                                                              HttpServletRequest request) {
        long current = lendListQueryRequest.getCurrent();
        long size = lendListQueryRequest.getPageSize();
        Page<LendList> lendListPage = lendListService.page(new Page<>(current, size),
                lendListService.getQueryWrapper(lendListQueryRequest));

        // 转换为 VO
        Page<LendListVO> lendListVOPage = new Page<>(current, size, lendListPage.getTotal());
        List<LendListVO> voList = lendListService.getLendListVO(lendListPage.getRecords());
        lendListVOPage.setRecords(voList);

        return ResultUtils.success(lendListVOPage);
    }

    /**
     * 获取当前用户的借阅记录
     *
     * @param lendListQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<LendListVO>> listMyLendListByPage(@RequestBody LendListQueryRequest lendListQueryRequest,
                                                                HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        lendListQueryRequest.setUserId(loginUser.getUserId());

        long current = lendListQueryRequest.getCurrent();
        long size = lendListQueryRequest.getPageSize();
        // 限制每页最大数量
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<LendList> lendListPage = lendListService.page(new Page<>(current, size),
                lendListService.getQueryWrapper(lendListQueryRequest));

        // 转换为 VO
        Page<LendListVO> lendListVOPage = new Page<>(current, size, lendListPage.getTotal());
        List<LendListVO> voList = lendListService.getLendListVO(lendListPage.getRecords());
        lendListVOPage.setRecords(voList);

        return ResultUtils.success(lendListVOPage);
    }

    // endregion
}
