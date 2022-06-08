package com.crowoj.api.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.dao.CompetitionDao;
import com.crowoj.api.dto.CompetitionDTO;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.entity.CompetitionProblem;
import com.crowoj.api.entity.Problem;
import com.crowoj.api.param.AddCompetitionParam;
import com.crowoj.api.param.UpdateCompetitionParam;
import com.crowoj.api.service.CompetitionProblemService;
import com.crowoj.api.service.CompetitionService;
import com.crowoj.api.service.ProblemService;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/18 14:11
 * @description
 */
@RestController("adminCompetitionController")
@RequestMapping("admin/competition")
public class CompetitionController {
    @Resource
    private CompetitionService competitionService;
    @Resource
    private CompetitionProblemService competitionProblemService;
    @Resource
    private ProblemService problemService;

    @PostMapping("add")
    @RequiresPermissions({"sys:competition:add"})
    public ResultVO<Boolean> addCompetition(@RequestBody @Valid AddCompetitionParam param){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(competitionService.addCompetition(activityUser,param));
    }

    @DeleteMapping("delete/{cid}")
    @RequiresPermissions({"sys:competition:delete"})
    public ResultVO<Boolean> deleteById(@PathVariable Long cid){
        return ResultUtil.success(competitionService.deleteById(cid));
    }

    @PostMapping("update")
    @RequiresPermissions({"sys:competition:update"})
    public ResultVO<Boolean> updateCompetition(@RequestBody @Valid UpdateCompetitionParam param){
        return ResultUtil.success(competitionService.updateCompetition(param));
    }


    @GetMapping("search")
    public ResultVO<Page<CompetitionDTO>> getCompetitionList(
            String search,
            Integer status,
            Integer type,
            Page<CompetitionDTO> page){
        if (search == null) search = "";
        if (status == null) status = -1;
        if (type == null) type = -1;
        return ResultUtil.success(competitionService.search(search,status,type,-1L,page));
    }

    @GetMapping("get-problem-list-by-cid/{cid}")
    public ResultVO<List<Problem>> getPidListByCid(@PathVariable Long cid){
        var qw = new QueryWrapper<CompetitionProblem>()
                .lambda().eq(CompetitionProblem::getCid,cid);
        var list = competitionProblemService.list(qw);
        var pidList = new ArrayList<Long>();
        list.forEach(p-> pidList.add(p.getPid()));
        return ResultUtil.success(problemService.listByIds(pidList));
    }

    @GetMapping("get-pdto-list-by-cid/{cid}")
    public ResultVO<List<ProblemDTO>> getProblemListByCid(@PathVariable Long cid){
        return ResultUtil.success(competitionService.getProblemListByCid(cid));
    }

}
