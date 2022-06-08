package com.crowoj.api.core.shiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author crow
 * @create 2021/10/21 19:43
 * @description
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class JWTToken implements AuthenticationToken {
    private String token;
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
