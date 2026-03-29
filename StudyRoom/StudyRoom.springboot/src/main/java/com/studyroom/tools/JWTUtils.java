package com.studyroom.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.studyroom.SysConst;

import java.util.Calendar;
import java.util.Map;

/**
 * jwt的公共方法
 */
public class JWTUtils {
    /**
     * 签名，由于生成token和解签时都需要使用sign，所以作为成员变量。
     */
    private static final String SIGN = "$%!FDGS^@G!GF!AFDSF&%^F";

    private JWTUtils() {
    }

    /**
     * 生成token      header.payload.signature
     * @param map 用户信息，以 Map<String, String> 类型封装
     * @return token字符串
     */
    public static String getToken(Map<String, String> map) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 180);     // 默认180天过期

        //创建 JWTBuilder
        JWTCreator.Builder builder = JWT.create();

        //header不写则使用默认值
        // payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        String token = builder
                .withExpiresAt(instance.getTime())      //过期时间
                .sign(Algorithm.HMAC256(SIGN));         //签名算法

        return token;
    }

    /**
     * 验证token是否合法，若不合法则会抛出异常
     * @param token token字符串
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

    /**
     * 获取token的信息，通过调用 DecodedJWT 的 get 方法，可以得到 token 的各种信息
     * 该方法也可以和验证 token 方法合并
     * @param token token字符串
     * @return DecodedJWT
     */
    public static DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }


}
