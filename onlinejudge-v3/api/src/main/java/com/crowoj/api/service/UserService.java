package com.crowoj.api.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dto.UserAllInfoDTO;
import com.crowoj.api.entity.User;
import com.crowoj.api.vo.UserInfoVO;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> getRankList(String search);

    User getUserByUid(Long uid);

    User getUserByUidAndBanFlag(Long uid, BanFlag banFlag);

    void saveOrUpdateUser(User user);

    UserInfoVO getUserInfoDtoByUid(Long uid);

    Page<User> getRankList(String search, Page<User> page);

    User updateUserInfo(User user, ActivityUser activityUser);

    Page<UserAllInfoDTO> getUserAllInfoDTO(String search,Integer banFlag, Page<UserAllInfoDTO> page);

    Boolean banUser(Long[] uid);
}
