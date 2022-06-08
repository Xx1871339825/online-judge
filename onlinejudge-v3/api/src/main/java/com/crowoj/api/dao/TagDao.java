package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.dto.TagDTO;
import com.crowoj.api.entity.Tag;

/**
 * (Tag)表数据库访问层
 *
 * @author crow
 * @since 2022-03-12 01:07:28
 */
public interface TagDao extends BaseMapper<Tag> {
    Page<TagDTO> searchList(String search,Page<TagDTO> page);

    TagDTO getTagByTagId(Long tid);
}

