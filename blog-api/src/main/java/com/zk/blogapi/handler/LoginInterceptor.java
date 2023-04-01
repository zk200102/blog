package com.zk.blogapi.handler;

import com.alibaba.fastjson.JSON;
import com.zk.blogapi.entity.SysUser;
import com.zk.blogapi.service.SysUserService;
import com.zk.blogapi.utils.JwtUtils;
import com.zk.blogapi.utils.UserThreadLocal;
import com.zk.blogapi.utils.enums.ErrorCode;
import com.zk.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author zk
 * date 2023/3/30 13:31
 * description: 登陆拦截器
 */
@Configuration
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private SysUserService sysUserService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        判断是访问方法还是资源的handle,如果是访问资源，那么直接放行
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
//        获取请求头的token
        String token = request.getHeader("Authorization");
        log.info("============================拦截器启动================================");
        String requestURI = request.getRequestURI();
        log.info("request url:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("============================拦截器结束================================");
//        判断token是否存在,不存在token拦截
        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
//            token不存在或不合法，调用response返回信息
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSON(result));
            return false;
        }
//      token认证
        String userId = JwtUtils.checkToken(token);
        if (StringUtils.isBlank(userId)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
//            token不存在或不合法，调用response返回信息
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSON(result));
            return false;
        }
//        验证成功，存储到ThreadLocal当中
        SysUser user = sysUserService.getById(userId);
        UserThreadLocal.put(user);
        return true;
    }
//      最后移除ThreadLocal的数据，解决内存泄漏风险
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
