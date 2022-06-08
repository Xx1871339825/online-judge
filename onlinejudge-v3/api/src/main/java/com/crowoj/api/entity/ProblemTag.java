package com.crowoj.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class ProblemTag {

    @TableId(type = IdType.AUTO)
    private Long ptId;
    /**
     * 问题Id
     */
    private Long problemId;
    /**
     * 标签Id
     */
    private Long tagId;


}
