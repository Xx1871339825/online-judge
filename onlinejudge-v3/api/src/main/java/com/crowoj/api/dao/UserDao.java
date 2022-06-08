package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.dto.UserAllInfoDTO;
import com.crowoj.api.dto.UserInfoDTO;
import com.crowoj.api.entity.User;

public interface UserDao extends BaseMapper<User> {
    UserInfoDTO getUserInfoDtoByUid(Long uid);

    Page<UserAllInfoDTO> getUserAllInfoDTO(String search,Integer banFlag, Page<UserAllInfoDTO> page);
}
