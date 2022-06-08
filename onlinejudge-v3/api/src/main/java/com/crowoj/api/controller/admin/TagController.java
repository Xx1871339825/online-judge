package com.crowoj.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.dto.TagDTO;
import com.crowoj.api.param.AddTagParam;
import com.crowoj.api.param.UpdateTagParam;
import com.crowoj.api.service.TagService;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author crow
 * @create 2022/3/12 1:01
 * @description
 */
@RestController("adminTagController")
@RequestMapping("admin/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @RequiresPermissions({"sys:tag:list"})
    @GetMapping("list")
    public ResultVO<Page<TagDTO>> list(String search, Page<TagDTO> page){
        return ResultUtil.success(tagService.searchList(search,page));
    }

    @RequiresPermissions({"sys:tag:add"})
    @PostMapping("add")
    public ResultVO<Boolean> addTag(@RequestBody @Valid AddTagParam param){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(tagService.addTag(activityUser,param));
    }

    @RequiresPermissions({"sys:tag:list"})
    @GetMapping("{tid}")
    public ResultVO<TagDTO> getTag(@PathVariable Long tid){
        return ResultUtil.success(tagService.getTagDTO(tid));
    }

    @RequiresPermissions({"sys:tag:update"})
    @PutMapping("update")
    public ResultVO<Boolean> updateTag(@RequestBody @Valid UpdateTagParam param){
        return ResultUtil.success(tagService.updateTag(param));
    }

    @RequiresPermissions({"sys:tag:delete"})
    @DeleteMapping("delete/{tid}")
    public ResultVO<Boolean> deleteTag(@PathVariable Long tid){

        return ResultUtil.success(tagService.deleteTagById(tid));
    }



}

