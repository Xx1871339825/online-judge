package com.crowoj.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.enums.ProblemType;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.entity.Problem;
import com.crowoj.api.param.AddProblemParam;
import com.crowoj.api.service.ProblemService;
import com.crowoj.api.service.UserSubmitService;
import com.crowoj.api.vo.ResultVO;
import com.crowoj.api.vo.SubmitInfoVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/11 15:20
 * @description
 */
@RestController("adminProblemController")
@RequestMapping("admin/problem")
public class ProblemController {

    @Resource
    private ProblemService problemService;

    @Resource
    private UserSubmitService userSubmitService;

    @RequiresPermissions({"sys:problem:list"})
    @GetMapping("list")
    public ResultVO<Page<ProblemDTO>> list(String search, Integer problemType, Page<ProblemDTO> page){
        var activityUser = (ActivityUser)SecurityUtils.getSubject().getPrincipal();
        ProblemType p = null;
        for (ProblemType value : ProblemType.values()) {
            if (value.getProblemType().equals(problemType)){
                p = value;
                break;
            }
        }
        if (search == null) search = "";
        return ResultUtil.success(problemService.search(activityUser.getUser().getUid(),search,p,page));
    }
    @RequiresPermissions({"sys:problem:list"})
    @GetMapping("get-all-problem-list")
    public ResultVO<List<Problem>> getAllProblemList(){
        return ResultUtil.success(problemService.list());
    }

    @RequiresPermissions({"sys:problem:list"})
    @GetMapping("get-search-problem-list")
    public ResultVO<List<ProblemDTO>> getSearchProblemList(String search){
        if (search == null) search = "";
        return ResultUtil.success(problemService.getSearchProblemList(search));
    }

    @RequiresPermissions(value = {"sys:problem:add","sys:problem:update"},logical = Logical.AND)
    @PostMapping
    public ResultVO<Boolean> addProblem(@RequestBody @Valid AddProblemParam param){
        var activityUser = (ActivityUser)SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(problemService.saveOrUpdateProblem(activityUser,param));
    }

    @RequiresPermissions({"sys:problem:list"})
    @GetMapping("{pid}")
    public ResultVO<AddProblemParam> getProblemInfo(@PathVariable Long pid){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(problemService.getProblemInfo(activityUser,pid));
    }


    @GetMapping("submit/info/{sid}")
    public ResultVO<SubmitInfoVO> getSubmitInfo(@PathVariable Long sid){
        return ResultUtil.success(userSubmitService.getSubmitInfo(sid, 1));
    }
}

