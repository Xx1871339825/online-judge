package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.core.enums.SubmitType;
import com.crowoj.api.dto.UserSubmitDTO;
import com.crowoj.api.entity.User;
import com.crowoj.api.entity.UserSubmit;
import com.crowoj.api.param.CompetitionSubmitParam;
import com.crowoj.api.param.SubmitParam;
import com.crowoj.api.vo.SubmitInfoVO;
import com.crowoj.api.vo.SubmitVO;

/**
 * (UserSubmit)表服务接口
 *
 * @author crow
 * @since 2022-02-22 23:32:06
 */
public interface UserSubmitService extends IService<UserSubmit> {
    SubmitVO submit(User user, SubmitParam param);
    Page<UserSubmitDTO> getSubmitList(Page<?> page, Long pid, String nickname, Integer status, SubmitType submitType);

    SubmitInfoVO getSubmitInfo(Long sid, int type);

    SubmitVO cSubmit(User user, CompetitionSubmitParam param);

    Page<UserSubmitDTO> getCSubmitList(Long cid, Page<?> page, Long pid, String nickname, Integer status);
}

