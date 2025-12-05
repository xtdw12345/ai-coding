package com.example.ticket.service;

import com.example.ticket.model.Ticket;
import com.example.ticket.util.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TicketService 测试类
 * 
 * @author Project Alpha Team
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional // 测试后回滚，不影响数据库
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TagService tagService;

    @Test
    public void testCreateTicket() {
        // 创建 Ticket
        Long ticketId = ticketService.createTicket(
            "测试Ticket",
            "这是一个测试Ticket",
            Arrays.asList("测试", "重要")
        );

        assertNotNull(ticketId);
        assertTrue(ticketId > 0);

        // 验证 Ticket 已创建
        Ticket ticket = ticketService.getTicketWithTags(ticketId);
        assertNotNull(ticket);
        assertEquals("测试Ticket", ticket.getTitle());
        assertEquals("这是一个测试Ticket", ticket.getDescription());
        assertEquals(0, ticket.getStatus()); // 默认未完成
        assertNotNull(ticket.getTags());
        assertTrue(ticket.getTags().size() >= 2);
    }

    @Test
    public void testCreateTicketWithEmptyTitle() {
        // 测试空标题应该抛出异常
        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.createTicket("", "描述", null);
        });
    }

    @Test
    public void testUpdateTicket() {
        // 先创建 Ticket
        Long ticketId = ticketService.createTicket(
            "原始标题",
            "原始描述",
            Arrays.asList("标签1")
        );

        // 更新 Ticket
        ticketService.updateTicket(
            ticketId,
            "更新后的标题",
            "更新后的描述",
            Arrays.asList("标签2", "标签3")
        );

        // 验证更新结果
        Ticket ticket = ticketService.getTicketWithTags(ticketId);
        assertNotNull(ticket);
        assertEquals("更新后的标题", ticket.getTitle());
        assertEquals("更新后的描述", ticket.getDescription());
        assertNotNull(ticket.getTags());
        // 验证标签已更新
        assertEquals(2, ticket.getTags().size());
    }

    @Test
    public void testCompleteTicket() {
        // 创建 Ticket
        Long ticketId = ticketService.createTicket("待完成Ticket", "描述", null);

        // 标记为已完成
        ticketService.completeTicket(ticketId);

        // 验证状态
        Ticket ticket = ticketService.getTicketWithTags(ticketId);
        assertNotNull(ticket);
        assertEquals(1, ticket.getStatus()); // 已完成
        assertNotNull(ticket.getCompletedAt());
    }

    @Test
    public void testReopenTicket() {
        // 创建并完成 Ticket
        Long ticketId = ticketService.createTicket("已完成Ticket", "描述", null);
        ticketService.completeTicket(ticketId);

        // 重新打开
        ticketService.reopenTicket(ticketId);

        // 验证状态
        Ticket ticket = ticketService.getTicketWithTags(ticketId);
        assertNotNull(ticket);
        assertEquals(0, ticket.getStatus()); // 未完成
    }

    @Test
    public void testDeleteTicket() {
        // 创建 Ticket
        Long ticketId = ticketService.createTicket("待删除Ticket", "描述", null);

        // 删除 Ticket
        ticketService.deleteTicket(ticketId);

        // 验证已删除
        Ticket ticket = ticketService.getTicketWithTags(ticketId);
        assertNull(ticket);
    }

    @Test
    public void testListTickets() {
        // 创建几个 Ticket
        ticketService.createTicket("Ticket 1", "描述1", Arrays.asList("标签1"));
        ticketService.createTicket("Ticket 2", "描述2", Arrays.asList("标签2"));
        ticketService.createTicket("Ticket 3", "描述3", null);

        // 查询列表
        PageResult<Ticket> result = ticketService.listTickets(null, null, 1, 10);
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getTotal() >= 3);
    }

    @Test
    public void testListTicketsWithKeyword() {
        // 创建 Ticket
        ticketService.createTicket("搜索测试Ticket", "描述", null);

        // 按关键字搜索
        PageResult<Ticket> result = ticketService.listTickets("搜索", null, 1, 10);
        assertNotNull(result);
        assertTrue(result.getTotal() > 0);
        assertTrue(result.getData().stream()
            .anyMatch(t -> t.getTitle().contains("搜索")));
    }
}

