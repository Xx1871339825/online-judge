package com.crowoj.api.dto;

import com.crowoj.api.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/3/12 1:04
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TagDTO extends Tag {
    private String nickname;
    private String pid;
}
