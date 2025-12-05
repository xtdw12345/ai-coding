package com.example.ticket.controller;

import com.example.ticket.model.Tag;
import com.example.ticket.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Tag Controller - 标签管理
 * 
 * @author Project Alpha Team
 */
@Controller
@RequestMapping("/tags")
public class TagController {

    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    /**
     * 标签列表页面
     */
    @GetMapping
    public String list(@RequestParam(required = false) String content,
                      Model model) {
        List<Tag> tags = tagService.listAllTags();
        model.addAttribute("tags", tags);
        
        // 如果是 AJAX 请求（content 参数），只返回内容部分
        if ("1".equals(content)) {
            return "tag-list :: content";
        }
        
        return "tag-list";
    }

    /**
     * 标签列表内容（用于 AJAX 加载）
     */
    @GetMapping("/content")
    public String listContent(Model model) {
        List<Tag> tags = tagService.listAllTags();
        model.addAttribute("tags", tags);
        return "tag-list";
    }

    /**
     * 新建标签页面
     */
    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) String content,
                         Model model) {
        return "tag-form";
    }

    /**
     * 创建标签
     */
    @PostMapping
    public String create(@RequestParam String name,
                        @RequestParam(required = false) String color,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                        RedirectAttributes redirectAttributes) {
        try {
            tagService.createTag(name, color);
            redirectAttributes.addFlashAttribute("message", "标签创建成功");
            
            // 如果是 AJAX 请求，重定向到列表内容
            if ("XMLHttpRequest".equals(requestedWith)) {
                return "redirect:/tags/content";
            }
            
            return "redirect:/tags";
        } catch (Exception e) {
            logger.error("创建标签失败", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            
            // 如果是 AJAX 请求，返回表单页面
            if ("XMLHttpRequest".equals(requestedWith)) {
                return "redirect:/tags/new?content=1";
            }
            
            return "redirect:/tags/new";
        }
    }

    /**
     * 编辑标签页面
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                          @RequestParam(required = false) String content,
                          Model model) {
        Tag tag = tagService.getTagById(id);
        if (tag == null) {
            // 标签不存在，返回列表页面
            if ("1".equals(content)) {
                // AJAX 请求，返回列表内容
                return "redirect:/tags/content";
            }
            return "redirect:/tags";
        }
        model.addAttribute("tag", tag);
        
        return "tag-form";
    }

    /**
     * 更新标签
     */
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                        @RequestParam String name,
                        @RequestParam(required = false) String color,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                        RedirectAttributes redirectAttributes) {
        try {
            tagService.updateTag(id, name, color);
            redirectAttributes.addFlashAttribute("message", "标签更新成功");
            
            // 如果是 AJAX 请求，重定向到列表内容
            if ("XMLHttpRequest".equals(requestedWith)) {
                return "redirect:/tags/content";
            }
            
            return "redirect:/tags";
        } catch (Exception e) {
            logger.error("更新标签失败", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            
            // 如果是 AJAX 请求，返回表单页面
            if ("XMLHttpRequest".equals(requestedWith)) {
                Tag tag = tagService.getTagById(id);
                redirectAttributes.addFlashAttribute("tag", tag);
                return "redirect:/tags/" + id + "/edit?content=1";
            }
            
            return "redirect:/tags/" + id + "/edit";
        }
    }

    /**
     * 删除标签
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                        RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTag(id);
            redirectAttributes.addFlashAttribute("message", "标签删除成功");
        } catch (Exception e) {
            logger.error("删除标签失败", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        // 如果是 AJAX 请求，重定向到列表内容
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "redirect:/tags/content";
        }
        
        return "redirect:/tags";
    }
}

