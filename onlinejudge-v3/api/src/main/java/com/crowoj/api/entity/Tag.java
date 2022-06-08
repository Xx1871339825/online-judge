package com.crowoj.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long tid;
    /**
     * 添加者，默认0=管理员
     */
    private Long uid;
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 标签下的题目数量
     */
    private Long problemCounts;

}
