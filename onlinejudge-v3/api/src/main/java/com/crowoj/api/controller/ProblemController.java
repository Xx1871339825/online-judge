package com.crowoj.api.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.enums.ProblemType;
import com.crowoj.api.core.enums.SubmitType;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.JWTUtil;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.dto.UserSubmitDTO;
import com.crowoj.api.param.CompetitionSubmitParam;
import com.crowoj.api.param.SubmitParam;
import com.crowoj.api.service.ProblemService;
import com.crowoj.api.service.UserSubmitService;
import com.crowoj.api.vo.ResultVO;
import com.crowoj.api.vo.SubmitInfoVO;
import com.crowoj.api.vo.SubmitVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author crow
 * @create 2022/2/20 23:15
 * @description 问题控制器
 */
@RestController
@RequestMapping("api/problem")
public class ProblemController{

    @Resource
    private ProblemService problemService;

    @Resource
    private UserSubmitService userSubmitService;

    @GetMapping("list")
    public ResultVO<Page<ProblemDTO>> search(String search, Page<ProblemDTO> page,HttpServletRequest request){
        search = search==null? "":search;
        var uid = parseToken(request);
        return ResultUtil.success(problemService.search(uid==null?-1:uid,search, ProblemType.DEFAULT,page));
    }

    @GetMapping("info/{pid}")
    public ResultVO<ProblemDTO> getProblemInfo(@PathVariable Long pid, HttpServletRequest request){
        var uid = parseToken(request);
        return ResultUtil.success(problemService.getProblemById(pid,uid,ProblemType.DEFAULT));
    }

    @PostMapping("submit")
    public ResultVO<SubmitVO> submit(@RequestBody @Valid SubmitParam param){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(userSubmitService.submit(activityUser.getUser(),param));
    }
    @PostMapping("c-submit")
    public ResultVO<SubmitVO> submit(@RequestBody @Valid CompetitionSubmitParam param){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(userSubmitService.cSubmit(activityUser.getUser(),param));
    }

    @GetMapping("submit/info/{sid}")
    public ResultVO<SubmitInfoVO> getSubmitInfo(@PathVariable Long sid){
        return ResultUtil.success(userSubmitService.getSubmitInfo(sid,0));
    }

    @GetMapping("submit/list")
    public ResultVO<Page<UserSubmitDTO>> getSubmitList(Page<?> page,Long pid,String nickname,Integer status){
        if (nickname!=null && nickname.equals("")){
            nickname = null;
        }
        return ResultUtil.success(userSubmitService.getSubmitList(page,pid,nickname,status, SubmitType.DEFAULT));
    }

    @GetMapping("c-submit/list/{cid}")
    public  ResultVO<Page<UserSubmitDTO>> getCSubmitList(@PathVariable Long cid,Page<?> page,Long pid,String nickname,Integer status){
        if (nickname!=null && nickname.equals("")){
            nickname = null;
        }
        return ResultUtil.success(userSubmitService.getCSubmitList(cid,page,pid,nickname,status));
    }



    private Long parseToken(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        if (token == null){
            return null;
        }
        token = token.replace("Bearer ","");
        try {
           return Long.parseLong(JWTUtil.parseAccessToken(token).getId());
        }catch (Exception e){
            return null;
        }
    }

}
