package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dao.TagDao;
import com.crowoj.api.dto.TagDTO;
import com.crowoj.api.entity.ProblemTag;
import com.crowoj.api.entity.Tag;
import com.crowoj.api.param.AddTagParam;
import com.crowoj.api.param.UpdateTagParam;
import com.crowoj.api.service.ProblemTagService;
import com.crowoj.api.service.TagService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * (Tag)表服务实现类
 *
 * @author crow
 * @since 2022-03-12 01:07:28
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {

    @Resource
    private ProblemTagService problemTagService;

    @Override
    public Page<TagDTO> searchList(String search, Page<TagDTO> page) {
        if (search == null) search = "";
        return getBaseMapper().searchList(search,page);
    }

    @Transactional
    @Override
    public Boolean addTag(ActivityUser activityUser, AddTagParam param) {
        var tag = new Tag().setProblemCounts((long) param.getPidArr().size())
                .setTagName(param.getTitle())
                .setUid(activityUser.getUser().getUid());
        var tagSave = save(tag);
        var ptList = new ArrayList<ProblemTag>();
        param.getPidArr().forEach(item -> {
            ptList.add(new ProblemTag().setProblemId(item).setTagId(tag.getTid()));
        });
        problemTagService.saveBatch(ptList);
        return tagSave;
    }

    @Override
    public TagDTO getTagDTO(Long tid) {
        var tagDTO = getBaseMapper().getTagByTagId(tid);
        if (tagDTO == null)
            throw new SimpleException(ResultEnum.NOT_FOUND);
        return tagDTO;
    }

    @Override
    public Boolean updateTag(UpdateTagParam param) {
        //判断list
        var pidList = param.getPidArr();
        var ptQw = new QueryWrapper<ProblemTag>();
        ptQw.lambda().eq(ProblemTag::getTagId,param.getTid());
        problemTagService.remove(ptQw);
        ptQw.clear();
        var ptList = new ArrayList<ProblemTag>();
        pidList.forEach(item -> {
            ptList.add(new ProblemTag().setProblemId(item).setTagId(param.getTid()));
        });
        problemTagService.saveBatch(ptList);
        var tUw = new UpdateWrapper<Tag>();
        tUw.lambda().eq(Tag::getTid,param.getTid())
                .set(Tag::getTagName,param.getTitle())
                .set(Tag::getProblemCounts,pidList.size());
        return update(tUw);
    }

    @Override
    public Boolean deleteTagById(Long tid) {
        var ptQw = new QueryWrapper<ProblemTag>();
        ptQw.lambda().eq(ProblemTag::getTagId,tid);
        problemTagService.remove(ptQw);
        return removeById(tid);
    }
}

