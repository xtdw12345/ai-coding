package com.example.ticket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 全局异常处理器
 * 
 * @author Project Alpha Team
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理 IllegalArgumentException（参数校验异常）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, 
                                                RedirectAttributes redirectAttributes) {
        logger.warn("参数校验失败: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/tickets";
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, RedirectAttributes redirectAttributes) {
        logger.error("系统异常: ", e);
        String errorMessage = "系统发生错误，请稍后重试";
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            errorMessage = e.getMessage();
        }
        redirectAttributes.addFlashAttribute("error", errorMessage);
        return "redirect:/tickets";
    }
}

