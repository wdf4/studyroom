package com.studyroom.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

/**
 * jwt的公共方法
 */
@Component
public class JWTUtils {

    /**
     * 签名，通过 Spring 配置注入，避免硬编码
     */
    private static String SIGN;

    /**
     * Token 过期天数
     */
    private static int EXPIRY_DAYS;

    @Value("${studyroom.jwt.secret}")
    public void setSign(String secret) {
        JWTUtils.SIGN = secret;
    }

    @Value("${studyroom.jwt.expiry-days}")
    public void setExpiryDays(int days) {
        JWTUtils.EXPIRY_DAYS = days;
    }

    private JWTUtils() {
    }

    /**
     * 生成token      header.payload.signature
     *
     * @param map 用户信息，以 Map<String, String> 类型封装
     * @return token字符串
     */
    public static String getToken(Map<String, String> map) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, EXPIRY_DAYS);

        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);

        return builder
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));
    }

    /**
     * 验证token是否合法，若不合法则会抛出异常
     *
     * @param token token字符串
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

    /**
     * 获取token的信息
     *
     * @param token token字符串
     * @return DecodedJWT
     */
    public static DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}
