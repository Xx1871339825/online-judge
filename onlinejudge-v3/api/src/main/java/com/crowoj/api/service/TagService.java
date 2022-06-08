package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dto.TagDTO;
import com.crowoj.api.entity.Tag;
import com.crowoj.api.param.AddTagParam;
import com.crowoj.api.param.UpdateTagParam;

/**
 * (Tag)表服务接口
 *
 * @author crow
 * @since 2022-03-12 01:07:28
 */
public interface TagService extends IService<Tag> {

    Page<TagDTO> searchList(String search, Page<TagDTO> page);

    Boolean addTag(ActivityUser activityUser, AddTagParam param);

    TagDTO getTagDTO(Long tid);

    Boolean updateTag(UpdateTagParam param);

    Boolean deleteTagById(Long tid);
}

