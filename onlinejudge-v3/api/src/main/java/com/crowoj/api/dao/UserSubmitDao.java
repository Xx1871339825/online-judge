package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.dto.SubmitInfoDTO;
import com.crowoj.api.dto.UserSubmitDTO;
import com.crowoj.api.entity.UserSubmit;
import com.crowoj.api.vo.SubmitInfoVO;
import com.crowoj.api.vo.SubmitVO;

/**
 * (UserSubmit)表数据库访问层
 *
 * @author crow
 * @since 2022-02-22 23:31:55
 */
public interface UserSubmitDao extends BaseMapper<UserSubmit> {

    SubmitInfoDTO getSubmitInfoBySid(Long sid);

    Page<UserSubmitDTO> getSubmitList(Page<?> page,Long pid,String nickname,Integer status,Integer submitType);

    Page<UserSubmitDTO> getCSubmitList(Long cid, Page<?> page, Long pid, String nickname, Integer status);
}

