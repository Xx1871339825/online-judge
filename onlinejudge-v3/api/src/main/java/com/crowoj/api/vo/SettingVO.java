package com.crowoj.api.vo;

import com.crowoj.api.entity.Setting;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author crow
 * @create 2022/3/10 15:18
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SettingVO extends Setting {
    private String nickname;
}
