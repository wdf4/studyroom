package com.studyroom.controller;

import com.studyroom.tools.dto.ResponseData;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 验证码控制器（数学题形式，Session存储，无需JWT）
 */
@Slf4j
@RestController
@RequestMapping("/Captcha")
public class CaptchaController {

    private static final String SESSION_KEY = "captcha_code";

    /**
     * 生成验证码（数学加法题）
     */
    @RequestMapping(value = "/Generate", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseData<Map<String, String>> generate(HttpSession session) {
        int a = new Random().nextInt(9) + 1;
        int b = new Random().nextInt(9) + 1;
        String question = a + " + " + b + " = ?";
        String answer = String.valueOf(a + b);
        session.setAttribute(SESSION_KEY, answer);
        session.setMaxInactiveInterval(300); // 5分钟过期
        Map<String, String> data = new HashMap<>();
        data.put("Question", question);
        log.debug("验证码已生成，sessionId={}", session.getId());
        return ResponseData.GetResponseDataInstance(data, "验证码已生成", true);
    }

    /**
     * 校验验证码（一次性消费）
     */
    @RequestMapping(value = "/Verify", method = RequestMethod.POST)
    public ResponseData<Boolean> verify(@RequestBody Map<String, String> body, HttpSession session) {
        String submitted = body.get("Code");
        String stored = (String) session.getAttribute(SESSION_KEY);
        session.removeAttribute(SESSION_KEY); // 一次性消费
        boolean valid = stored != null && submitted != null && stored.equalsIgnoreCase(submitted.trim());
        return ResponseData.GetResponseDataInstance(valid, valid ? "验证码正确" : "验证码错误或已过期", valid);
    }
}
