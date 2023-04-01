package com.zk.blogapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.zk.blogapi.config.JwtConfig;
import com.zk.blogapi.config.Md5Config;
import com.zk.blogapi.entity.SysUser;
import com.zk.blogapi.service.LoginService;
import com.zk.blogapi.service.SysUserService;
import com.zk.blogapi.utils.JwtUtils;
import com.zk.blogapi.utils.enums.AdminEnum;
import com.zk.blogapi.utils.enums.DeleteEnum;
import com.zk.blogapi.utils.enums.ErrorCode;
import com.zk.blogapi.vo.RegisterVo;
import com.zk.common.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * author zk
 * date 2023/3/28 19:31
 * description: 登陆实现类
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final SysUserService sysUserService;
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public Result login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAM_ERROR.getCode(),ErrorCode.PARAM_ERROR.getMsg());
        }
        //加密密码，比对数据库密码
        password = DigestUtils.md5Hex(password + Md5Config.MD5_SLOT);
//        System.out.println(password);
        SysUser sysUser = sysUserService.findByUsernameAndPassword(username,password);
//        判断用户是否存在
        if (sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
//        存储到redis
        String token = JwtUtils.createToken(sysUser.getId());
        stringRedisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser), JwtConfig.JWT_EXPIRATION, TimeUnit.MILLISECONDS);

//        System.out.println(stringRedisTemplate.opsForValue().get("TOKEN").equals(token));
        return Result.success(token);
    }

    @Override
    @Transactional
    public Result register(RegisterVo registerVo) {
//        校验参数
        String account = registerVo.getAccount();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAM_ERROR.getCode(),ErrorCode.PARAM_ERROR.getMsg());
        }
//        判断用户是否存在
        SysUser sysUser = sysUserService.findUserByUsername(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
//        注册用户
        SysUser user = new SysUser();
        user.setNickname(nickname);
        user.setAccount(account);
        user.setPassword(DigestUtils.md5Hex(password+Md5Config.MD5_SLOT));
        user.setCreateDate(System.currentTimeMillis());
        user.setLastLogin(System.currentTimeMillis());
        user.setAvatar("/static/img/logo.b3a48c0.png");
        user.setAdmin(AdminEnum.ADMIN_TRUE);
        user.setDeleted(DeleteEnum.DELETE_FALSE);
        user.setSalt("");
        user.setStatus("");
        user.setEmail("");
//        保存到数据库
        sysUserService.save(user);
        //        存储到redis
        String token = JwtUtils.createToken(user.getId());
        stringRedisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user), JwtConfig.JWT_EXPIRATION, TimeUnit.MILLISECONDS);
        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        stringRedisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }
}
