package com.crowoj.api.param;

import com.crowoj.api.entity.Problem;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/18 14:18
 * @description
 */
@Data
@Accessors(chain = true)
public class AddCompetitionParam {
    //比赛标题
    @NotBlank
    private String title;
    //0=公开赛，1=需要密码
    @NotNull
    private Integer auth;
    //比赛密码
    private String password;
    //开始时间
    @NotNull
    private Date startTime;
    //结束时间
    @NotNull
    private Date endTime;
    //比赛说明
    @NotBlank
    private String description;

    @NotNull
    private List<Problem> problemList;
}
