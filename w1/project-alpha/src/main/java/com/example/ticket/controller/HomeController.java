package com.example.ticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页 Controller
 * 
 * @author Project Alpha Team
 */
@Controller
public class HomeController {

    /**
     * 首页 - 显示 Dashboard（单页面应用布局）
     */
    @GetMapping("/")
    public String index() {
        return "dashboard";
    }
}

