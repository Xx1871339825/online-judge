package com.crowoj.api.core.shiro;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.MyAuthenticationException;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.utils.ResultUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author crow
 * @create 2021/10/21 18:28
 * @description
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        var token = ((HttpServletRequest)request).getHeader("Authorization");
        //.replace("Bearer ","");
        if(token == null){
            responseErr(response,ResultEnum.UNAUTHORIZED.getStatus(),ResultEnum.UNAUTHORIZED.getMessage());
            return false;
        }
        token = token.replace("Bearer ","");

        if (StrUtil.isNotBlank(token)){
            try {
                executeLogin(request,response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof MyAuthenticationException){
                    responseErr(response,((MyAuthenticationException) e).getStatus(),e.getMessage());
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 跨域放行
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void responseErr(ServletResponse servletResponse,Integer status,String message){
        var resp = WebUtils.toHttp(servletResponse);
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(401);
        try(var out = resp.getWriter()){
            out.write(JSONUtil.toJsonStr(ResultUtil.error(status,message)));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseErr(ServletResponse servletResponse,SimpleException simpleException){
        var resp = WebUtils.toHttp(servletResponse);
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(simpleException.getStatus());
        try(var out = resp.getWriter()){
            out.append(JSONUtil.toJsonStr(simpleException));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        var token = ((HttpServletRequest)request).getHeader("Authorization").replace("Bearer ","");

        getSubject(request,response).login(new JWTToken(token));

        return true;
    }

}
