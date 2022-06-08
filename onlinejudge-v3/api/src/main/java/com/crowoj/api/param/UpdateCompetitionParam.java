package com.crowoj.api.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/3/21 0:21
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UpdateCompetitionParam extends AddCompetitionParam{
    private Long id;
}
