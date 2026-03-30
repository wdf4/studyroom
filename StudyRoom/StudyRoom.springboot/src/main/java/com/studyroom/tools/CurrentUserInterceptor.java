package com.studyroom.tools;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.studyroom.SysConst;
import com.studyroom.enums.RoleTypeEnum;
import com.studyroom.tools.dto.CurrentUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 解析当前token的用户信息 存到线程中
 */
public class CurrentUserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // jwt不建议作为请求参数，而应该在请求的请求头中
        // 获取请求头中的令牌（token）
        String token = request.getHeader("Authorization");
        if(token!=null) {

            token=token.replace("Bearer ","");
            DecodedJWT jwt=JWTUtils.getTokenInfo(token);
            String userId =jwt.getClaims().get(SysConst.UserIdClaim).asString();
            String roleType =jwt.getClaims().get(SysConst.RoleTypeClaim).asString();
            CurrentUserDto currentUserDto =new CurrentUserDto();
            currentUserDto.setUserId(Integer.valueOf(userId));
            currentUserDto.setRoleType(RoleTypeEnum.GetEnum(Integer.valueOf(roleType)));
            BaseContext.setCurrentUserDto(currentUserDto);
        }
        else{
            CurrentUserDto currentUserDto =new CurrentUserDto();
            currentUserDto.setUserId(0);
            BaseContext.setCurrentUserDto(currentUserDto);
        }
         return true;

    }
}
