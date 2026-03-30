package com.studyroom.tools;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * 扩展方法
 */
@Slf4j
public class Extension {

    /**
     * 判断字符串是否为空或者空串
     */
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * 判断字符串是否是空或者空串
     */
    public static boolean isNullOrEmpty(String str) {
        return !isNotNullOrEmpty(str);
    }

    /**
     * 判断Id是否存在
     */
    public static boolean isNullOrZero(Integer value) {
        return value == null || value == 0;
    }

    /**
     * 拷贝一个列表到另外一个列表
     */
    public static <T> List<T> copyBeanList(List<?> resourceList, Class<T> target) {
        List<T> targetList = new LinkedList<>();
        if (resourceList.isEmpty()) {
            return targetList;
        }
        resourceList.forEach(e -> {
            T o = null;
            try {
                o = target.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                log.error("copyBeanList 实例化 {} 失败: {}", target.getName(), ex.getMessage(), ex);
            }
            org.springframework.beans.BeanUtils.copyProperties(e, o);
            targetList.add(o);
        });
        return targetList;
    }

    /**
     * 获取当前的用户信息 根据token
     */
    public static String getTokenInfo(String token, String key) {
        token = token.replace("Bearer ", "");
        DecodedJWT jwt = JWTUtils.getTokenInfo(token);
        return jwt.getClaims().get(key).asString();
    }

    /**
     * LocalDateTime戳转换成字符串
     */
    public static String LocalDateTimeConvertString(LocalDateTime localDateTime, String format) {
        if (localDateTime == null) {
            return "";
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }
}
