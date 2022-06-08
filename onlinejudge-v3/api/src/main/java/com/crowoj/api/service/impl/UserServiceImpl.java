package com.crowoj.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.enums.StatusEnum;
import com.crowoj.api.core.enums.SubmitType;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dao.RoleDao;
import com.crowoj.api.dto.UserAllInfoDTO;
import com.crowoj.api.entity.User;
import com.crowoj.api.dao.UserDao;
import com.crowoj.api.entity.UserSubmit;
import com.crowoj.api.service.UserService;
import com.crowoj.api.service.UserSubmitService;
import com.crowoj.api.vo.UserInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author crow
 * @create 2021/10/21 21:47
 * @description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService {

    @Resource
    private UserSubmitService userSubmitService;

    @Resource
    private RoleDao roleDao;

    @Override
    public List<User> getRankList(String search) {
        var uQw = new QueryWrapper<User>();
        uQw.lambda().like(User::getNickname,search).orderByDesc(User::getAcceptCount);
        return list(uQw);
    }

    @Override
    public User getUserByUid(Long uid) {

        return getById(uid);
    }

    @Override
    public User getUserByUidAndBanFlag(Long uid, BanFlag banFlag) {
        var qw = new QueryWrapper<User>();
        qw.lambda().eq(User::getUid,uid).eq(User::getBanFlag,banFlag);
        return getOne(qw);
    }

    @Transactional
    @Override
    public void saveOrUpdateUser(User user) {
        saveOrUpdate(user);
    }

    @Override
    public UserInfoVO getUserInfoDtoByUid(Long uid) {
        var dto = getBaseMapper().getUserInfoDtoByUid(uid);
        var vo = new UserInfoVO();
        BeanUtil.copyProperties(dto,vo);
        //获取到该用户提交成功的题目
        var usQw = new QueryWrapper<UserSubmit>();
        usQw.select("DISTINCT problem_id")
                .lambda()
                .eq(UserSubmit::getUserId,uid)
                .eq(UserSubmit::getStatus, StatusEnum.Accepted)
                .eq(UserSubmit::getSubmitType, SubmitType.DEFAULT)
                .orderByDesc(UserSubmit::getSubmitTime);
        var submitList = userSubmitService.list(usQw);
        var pidList = new ArrayList<Long>();
        submitList.forEach(item -> pidList.add(item.getProblemId()));
        vo.setPidList(pidList);
        return vo;
    }

    @Override
    public Page<User> getRankList(String search, Page<User> page) {
        var uQw = new QueryWrapper<User>();
        uQw.lambda().like(User::getNickname,search).orderByDesc(User::getAcceptCount);
        return getBaseMapper().selectPage(page,uQw);
    }

    @Override
    public User updateUserInfo(User user, ActivityUser activityUser) {
        if (user == null){
            throw new SimpleException(ResultEnum.NOT_FOUND);
        }
        user.setUid(null);

        var oldUser = activityUser.getUser();
        BeanUtil.copyProperties(user,oldUser, CopyOptions.create().ignoreNullValue());
        saveOrUpdate(oldUser);
        return oldUser;
    }

    @Override
    public Page<UserAllInfoDTO> getUserAllInfoDTO(String search, Integer banFlag, Page<UserAllInfoDTO> page) {
        if (search == null) search = "";
        if (banFlag == -1) banFlag = null;
        return getBaseMapper().getUserAllInfoDTO(search,banFlag,page);
    }

    @Transactional
    @Override
    public Boolean banUser(Long[] uid) {
        System.err.println(uid);
        List<User> list = listByIds(List.of(uid));
        list.forEach(user -> {
            if(user.getBanFlag().equals(BanFlag.IS_BAN)){
                user.setBanFlag(BanFlag.NOT_BAN);
            }else {
                user.setBanFlag(BanFlag.IS_BAN);
            }
        });
        return updateBatchById(list);
    }


}
