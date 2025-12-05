package com.example.ticket.controller;

import com.example.ticket.model.Tag;
import com.example.ticket.model.Ticket;
import com.example.ticket.service.TagService;
import com.example.ticket.service.TicketService;
import com.example.ticket.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Ticket Controller
 * 
 * @author Project Alpha Team
 */
@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TagService tagService;

    /**
     * Ticket 列表页面
     */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                      @RequestParam(required = false) Long tagId,
                      @RequestParam(defaultValue = "1") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(required = false) String content,
                      Model model) {
        PageResult<Ticket> pageResult = ticketService.listTickets(keyword, tagId, page, size);
        List<Tag> allTags = tagService.listAllTags();
        
        model.addAttribute("tickets", pageResult.getData());
        model.addAttribute("pageInfo", pageResult);
        model.addAttribute("allTags", allTags);
        model.addAttribute("currentTagId", tagId);
        model.addAttribute("keyword", keyword);
        
        // 如果是 AJAX 请求（content 参数），只返回内容部分
        if ("1".equals(content)) {
            return "ticket-list :: content";
        }
        
        return "ticket-list";
    }

    /**
     * Ticket 列表内容（用于 AJAX 加载）
     */
    @GetMapping("/content")
    public String listContent(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Long tagId,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        PageResult<Ticket> pageResult = ticketService.listTickets(keyword, tagId, page, size);
        List<Tag> allTags = tagService.listAllTags();
        
        model.addAttribute("tickets", pageResult.getData());
        model.addAttribute("pageInfo", pageResult);
        model.addAttribute("allTags", allTags);
        model.addAttribute("currentTagId", tagId);
        model.addAttribute("keyword", keyword);
        
        return "ticket-list";
    }

    /**
     * 新建 Ticket 页面
     */
    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) String content,
                         Model model) {
        List<Tag> allTags = tagService.listAllTags();
        model.addAttribute("allTags", allTags);
        
        return "ticket-form";
    }

    /**
     * 创建 Ticket
     */
    @PostMapping
    public String create(@RequestParam String title,
                        @RequestParam(required = false) String description,
                        @RequestParam(required = false) List<String> tags,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                        RedirectAttributes redirectAttributes) {
        try {
            ticketService.createTicket(title, description, tags);
            redirectAttributes.addFlashAttribute("message", "创建成功");
            
            // 如果是 AJAX 请求，重定向到列表内容
            if ("XMLHttpRequest".equals(requestedWith)) {
                return "redirect:/tickets/content";
            }
            
            return "redirect:/tickets";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            
            // 如果是 AJAX 请求，返回表单页面
            if ("XMLHttpRequest".equals(requestedWith)) {
                List<Tag> allTags = tagService.listAllTags();
                redirectAttributes.addFlashAttribute("allTags", allTags);
                return "redirect:/tickets/new?content=1";
            }
            
            return "redirect:/tickets/new";
        }
    }

    /**
     * 编辑 Ticket 页面
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                          @RequestParam(required = false) String content,
                          Model model) {
        Ticket ticket = ticketService.getTicketWithTags(id);
        if (ticket == null) {
            // Ticket 不存在，返回列表页面
            if ("1".equals(content)) {
                // AJAX 请求，返回列表内容
                return "redirect:/tickets/content";
            }
            return "redirect:/tickets";
        }
        List<Tag> allTags = tagService.listAllTags();
        model.addAttribute("ticket", ticket);
        model.addAttribute("allTags", allTags);
        
        return "ticket-form";
    }

    /**
     * 更新 Ticket
     */
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                        @RequestParam String title,
                        @RequestParam(required = false) String description,
                        @RequestParam(required = false) List<String> tags,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                        RedirectAttributes redirectAttributes) {
        try {
            ticketService.updateTicket(id, title, description, tags);
            redirectAttributes.addFlashAttribute("message", "更新成功");
            
            // 如果是 AJAX 请求，重定向到列表内容
            if ("XMLHttpRequest".equals(requestedWith)) {
                return "redirect:/tickets/content";
            }
            
            return "redirect:/tickets";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            
            // 如果是 AJAX 请求，返回表单页面
            if ("XMLHttpRequest".equals(requestedWith)) {
                Ticket ticket = ticketService.getTicketWithTags(id);
                List<Tag> allTags = tagService.listAllTags();
                redirectAttributes.addFlashAttribute("ticket", ticket);
                redirectAttributes.addFlashAttribute("allTags", allTags);
                return "redirect:/tickets/" + id + "/edit?content=1";
            }
            
            return "redirect:/tickets/" + id + "/edit";
        }
    }

    /**
     * 删除 Ticket
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                        RedirectAttributes redirectAttributes) {
        try {
            ticketService.deleteTicket(id);
            redirectAttributes.addFlashAttribute("message", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        // 如果是 AJAX 请求，重定向到列表内容
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "redirect:/tickets/content";
        }
        
        return "redirect:/tickets";
    }

    /**
     * 标记 Ticket 为已完成
     */
    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id,
                          @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                          RedirectAttributes redirectAttributes) {
        try {
            ticketService.completeTicket(id);
            redirectAttributes.addFlashAttribute("message", "标记完成");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        // 如果是 AJAX 请求，重定向到列表内容
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "redirect:/tickets/content";
        }
        
        return "redirect:/tickets";
    }

    /**
     * 取消完成 Ticket（重新打开）
     */
    @PostMapping("/{id}/reopen")
    public String reopen(@PathVariable Long id,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                        RedirectAttributes redirectAttributes) {
        try {
            ticketService.reopenTicket(id);
            redirectAttributes.addFlashAttribute("message", "取消完成");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        // 如果是 AJAX 请求，重定向到列表内容
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "redirect:/tickets/content";
        }
        
        return "redirect:/tickets";
    }
}

