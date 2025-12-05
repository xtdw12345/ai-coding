package com.example.ticket.service;

import com.example.ticket.model.Ticket;
import com.example.ticket.util.PageResult;

import java.util.List;

/**
 * Ticket Service 接口
 * 
 * @author Project Alpha Team
 */
public interface TicketService {
    /**
     * 创建 Ticket
     * @param title 标题
     * @param description 描述
     * @param tagNames 标签名称列表
     * @return Ticket ID
     */
    Long createTicket(String title, String description, List<String> tagNames);

    /**
     * 更新 Ticket
     * @param id Ticket ID
     * @param title 标题
     * @param description 描述
     * @param tagNames 标签名称列表
     */
    void updateTicket(Long id, String title, String description, List<String> tagNames);

    /**
     * 删除 Ticket
     * @param id Ticket ID
     */
    void deleteTicket(Long id);

    /**
     * 标记 Ticket 为已完成
     * @param id Ticket ID
     */
    void completeTicket(Long id);

    /**
     * 取消完成 Ticket（重新打开）
     * @param id Ticket ID
     */
    void reopenTicket(Long id);

    /**
     * 根据 ID 查询 Ticket（包含标签）
     * @param id Ticket ID
     * @return Ticket 对象
     */
    Ticket getTicketWithTags(Long id);

    /**
     * 分页查询 Ticket 列表
     * @param keyword 搜索关键字（标题模糊匹配）
     * @param tagId 标签 ID（可选）
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Ticket> listTickets(String keyword, Long tagId, int page, int pageSize);
}

