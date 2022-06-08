package com.crowoj.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.aop.Delay;
import com.crowoj.api.core.enums.IdentityType;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.IpUtil;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.dto.UserInfoDTO;
import com.crowoj.api.entity.User;
import com.crowoj.api.entity.UserAuths;
import com.crowoj.api.param.UpdateEmailParam;
import com.crowoj.api.param.UpdatePwdParam;
import com.crowoj.api.service.EmailService;
import com.crowoj.api.service.UserAuthsService;
import com.crowoj.api.service.UserService;
import com.crowoj.api.vo.ResultVO;
import com.crowoj.api.vo.UserInfoVO;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@Api(tags = "用户Api")
@RestController
@RequestMapping("api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserAuthsService userAuthsService;

    @Resource
    private EmailService emailService;

    @GetMapping("info/{uid}")
    public ResultVO<UserInfoVO> getUserInfoDtoByUid(@PathVariable Long uid){
        return ResultUtil.success(userService.getUserInfoDtoByUid(uid));
        
    }

    @PutMapping("update")
    public ResultVO<User> updateUserInfo(@RequestBody User user){
        var activityUser = (ActivityUser)SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(userService.updateUserInfo(user,activityUser));
    }

    @PutMapping("update-pwd")
    public ResultVO<Boolean> updatePassword(@RequestBody @Valid UpdatePwdParam param){
        var activityUser = (ActivityUser)SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(userAuthsService.updatePwd(param,activityUser.getUser()));
    }

    @PutMapping("update-email")
    public ResultVO<Boolean> updateEmail(@RequestBody @Valid UpdateEmailParam param){
        var activityUser = (ActivityUser)SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(userAuthsService.updateEmail(param,activityUser.getUser()));
    }

//    @GetMapping("rank-list")
//    public ResultVO<Page<User>> getRankList(String search,Page<User> page){
//        if (search == null){
//            search = "";
//        }
//        return ResultUtil.success(userService.getRankList(search,page));
//    }
    @GetMapping("rank-list")
    public ResultVO<List<User>> getRankList(String search, Page<User> page){
        if (search == null){
            search = "";
        }
        return ResultUtil.success(userService.getRankList(search));
    }


    @GetMapping("test")
    public User testCurrentUser(){
        var activityUser = (ActivityUser)SecurityUtils.getSubject().getPrincipal();
        return activityUser.getUser();
    }

    @Delay(path = "send-update-pwd-email")
    @PostMapping("send-update-pwd-email")
    public ResultVO<Object> sendUpdatePwdEmail(){
        var activityUser = (ActivityUser)SecurityUtils.getSubject().getPrincipal();
        var qw = new QueryWrapper<UserAuths>();
        qw.lambda().eq(UserAuths::getUid,activityUser.getUser().getUid())
                .eq(UserAuths::getIdentityType, IdentityType.EMAIL);
        var ua = userAuthsService.getOne(qw);
        emailService.sendUpdateEmailValidCode(ua.getIdentifier());
        return ResultUtil.success();
    }

}
