package com.crowoj.api.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.entity.User;
import com.crowoj.api.service.UserService;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author crow
 * @create 2022/3/4 0:35
 * @description
 */
@RestController
@RequestMapping("api/upload")
public class UploadController {

    @Resource
    private UserService userService;

    @Value("${web.upload-path}")
    String filePath;
    private final static String PREFIX = "http://127.0.0.1:8080/static";
    @PostMapping("avatar")
    public ResultVO<User> uploadAvatar(MultipartFile avatar){
        // 简易的上传头像接口
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        if (avatar.isEmpty()){
            throw new SimpleException(ResultEnum.BAD_REQUEST_FILE_IS_EMPTY);
        }
        var avatarPath = new File(filePath + "/avatar");
        if (!avatarPath.exists()){
            avatarPath.mkdir();
        }
        //删除当前用户的头像文件
        var user = activityUser.getUser();
        if (user.getAvatar() != null){
            if (user.getAvatar().contains(PREFIX)){
                //本地图片
                var oldAvatarPath = filePath + user.getAvatar().replace(PREFIX,"");
                var oldAvatarFile = new File(oldAvatarPath);
                oldAvatarFile.delete();
            }
        }
        //保存上传的图片
        var suffix = avatar.getOriginalFilename().split("\\.")[1];
        var fileName = UUID.fastUUID().toString(true) +"." +  suffix;
        var newAvatarFile = new File(filePath+"/avatar/"+fileName);
        try {
            avatar.transferTo(newAvatarFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SimpleException(ResultEnum.BAD_REQUEST);
        }
        user.setAvatar(PREFIX + "/avatar/" + fileName);
        userService.saveOrUpdateUser(user);
        return ResultUtil.success(user);
    }

    @RequiresPermissions({"sys:upload:file"})
    @PostMapping
    public ResultVO<String> uploadFile(MultipartFile file){
        if (file.isEmpty())
            throw new SimpleException(ResultEnum.BAD_REQUEST);
        var avatarPath = new File(filePath + "/upload");
        if (!avatarPath.exists()){
            avatarPath.mkdir();
        }
        var suffix = file.getOriginalFilename().split("\\.")[1];
        var fileName = UUID.fastUUID().toString(true) +"." +  suffix;
        var newAvatarFile = new File(filePath+"/upload/"+fileName);
        try {
            file.transferTo(newAvatarFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SimpleException(ResultEnum.BAD_REQUEST);
        }
        return ResultUtil.success(PREFIX + "/upload/" + fileName);
    }
}
