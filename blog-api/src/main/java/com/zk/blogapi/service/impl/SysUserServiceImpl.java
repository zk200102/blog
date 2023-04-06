package com.zk.blogapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.blogapi.entity.SysUser;
import com.zk.blogapi.mapper.SysUserMapper;
import com.zk.blogapi.service.SysUserService;
import com.zk.blogapi.utils.JwtUtils;
import com.zk.blogapi.vo.LoginUserVo;
import com.zk.common.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.zk.blogapi.utils.enums.ErrorCode.TOKEN_ERROR;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public SysUser findByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname)
                .eq(SysUser::getAccount,username)
                .eq(SysUser::getPassword,password);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Result getUserByToken(String token) {
//        判断token是否存在
        if (StringUtils.isBlank(token)) return Result.fail(TOKEN_ERROR.getCode(), TOKEN_ERROR.getMsg());
        String userId = JwtUtils.checkToken(token);
        if (userId.isEmpty()){
            return Result.fail(TOKEN_ERROR.getCode(), TOKEN_ERROR.getMsg());
        }
        String stringUser = stringRedisTemplate.opsForValue().get("TOKEN_"+token);
        SysUser sysUser = JSON.parseObject(stringUser, SysUser.class);
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(sysUser,loginUserVo);
        return Result.success(loginUserVo);
    }

    @Override
    public Result getUserInfo(String account) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount,account);
        SysUser sysUser = baseMapper.selectOne(wrapper);
        return Result.success(sysUser);
    }

    @Override
    public SysUser findUserByUsername(String account) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().eq("account",account).last("limit 1"));
    }
}
