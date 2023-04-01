package com.zk.blogapi.utils;

import com.zk.blogapi.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author zk
 * @date 2023/3/28 16:42
 * @desciption: jwt相关方法
 */

public class JwtUtils {
    /**
     * @author: zk
     * @date: 2023/3/30
     * @description: 私有化无参构造，禁止创建JwtUtils实例
     * @return: null
     */
    private JwtUtils() {
    }
    /**
     * 生成token
     * @param userId
     * @return
     */
    public static String createToken(Long userId){
        String token = Jwts.builder().claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, JwtConfig.JWT_SECRET)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+JwtConfig.JWT_EXPIRATION))
                .compact();
        return token;
    }

    /**
     * 解析token，获取数据
     * @param token
     * @return
     */
    public static String checkToken(String token){
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JwtConfig.JWT_SECRET).parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            return  String.valueOf(body.get("userId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
