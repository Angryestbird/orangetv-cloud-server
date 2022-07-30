package com.orangetv.cloud.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.RedirectView;

@Controller
@RequestMapping("orange/cloud/tv/login")
public class LoginController {

    /**
     * 1. 已登录用户访问这个页面重定向到指定的URL
     * 2. 未登录访问这个页面会自动重定向到oauth2登录页
     * 3. 用户完成认证后由Gateway重定向到之前访问的页面
     *
     * @return redirect URL
     */
    @GetMapping
    public RedirectView login(@RequestParam String location) {
        return new RedirectView(location, HttpStatus.FOUND);
    }
}
