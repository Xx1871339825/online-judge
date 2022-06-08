package com.crowoj.api.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowoj.api.core.enums.BanFlag;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (Role)表实体类
 *
 * @author crow
 * @since 2022-03-08 14:28:17
 */
@Data
@Accessors(chain = true)   
public class Role{
    //角色id
    @TableId(type = IdType.AUTO)
    private Long rid;
    //角色名称
    private String roleName;
    //角色标识
    private String roleCode;
    //角色描述
    private String roleDesc;
    //状态，0=可用，1=不可用
    private BanFlag status;
}

