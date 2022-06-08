package com.crowoj.judgeserver.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author xiaoxing
 * @Description
 * @create 2020/12/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JWTVO {
    private String accessToken;
    private String refreshToken;
}
