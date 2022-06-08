package com.crowoj.judgeserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowoj.judgeserver.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author crow
 * @since 2022-02-28 18:27:41
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}

