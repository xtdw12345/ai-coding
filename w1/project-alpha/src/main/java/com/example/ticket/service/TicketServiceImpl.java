package com.example.ticket.service;

import com.example.ticket.mapper.TagMapper;
import com.example.ticket.mapper.TicketMapper;
import com.example.ticket.mapper.TicketTagMapper;
import com.example.ticket.model.Tag;
import com.example.ticket.model.Ticket;
import com.example.ticket.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Ticket Service 实现类
 * 
 * @author Project Alpha Team
 */
@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TicketTagMapper ticketTagMapper;

    @Autowired
    private TagService tagService;

    @Override
    @Transactional
    public Long createTicket(String title, String description, List<String> tagNames) {
        logger.info("创建Ticket: title={}", title);
        // 参数校验
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("标题长度不能超过200个字符");
        }
        if (description != null && description.length() > 2000) {
            throw new IllegalArgumentException("描述长度不能超过2000个字符");
        }

        // 创建 Ticket
        Ticket ticket = new Ticket(title.trim(), description != null ? description.trim() : null);
        ticket.setStatus(0); // 默认未完成
        ticketMapper.insert(ticket);
        logger.debug("Ticket创建成功: id={}", ticket.getId());

        // 处理标签关联
        if (tagNames != null && !tagNames.isEmpty()) {
            List<Tag> tags = tagService.findOrCreateTagsByNames(tagNames);
            logger.debug("为Ticket关联{}个标签", tags.size());
            for (Tag tag : tags) {
                ticketTagMapper.insert(ticket.getId(), tag.getId());
            }
        }

        logger.info("Ticket创建完成: id={}, title={}", ticket.getId(), title);
        return ticket.getId();
    }

    @Override
    @Transactional
    public void updateTicket(Long id, String title, String description, List<String> tagNames) {
        logger.info("更新Ticket: id={}, title={}", id, title);
        // 参数校验
        if (id == null) {
            throw new IllegalArgumentException("Ticket ID 不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("标题长度不能超过200个字符");
        }
        if (description != null && description.length() > 2000) {
            throw new IllegalArgumentException("描述长度不能超过2000个字符");
        }

        // 检查 Ticket 是否存在
        Ticket existingTicket = ticketMapper.findById(id);
        if (existingTicket == null) {
            logger.warn("Ticket不存在: id={}", id);
            throw new IllegalArgumentException("Ticket 不存在，ID: " + id);
        }

        // 更新 Ticket 基本信息
        existingTicket.setTitle(title.trim());
        existingTicket.setDescription(description != null ? description.trim() : null);
        ticketMapper.update(existingTicket);

        // 更新标签关联：先删除旧关联，再插入新关联
        ticketTagMapper.deleteByTicketId(id);
        if (tagNames != null && !tagNames.isEmpty()) {
            List<Tag> tags = tagService.findOrCreateTagsByNames(tagNames);
            logger.debug("为Ticket更新{}个标签", tags.size());
            for (Tag tag : tags) {
                ticketTagMapper.insert(id, tag.getId());
            }
        }
        logger.info("Ticket更新完成: id={}", id);
    }

    @Override
    @Transactional
    public void deleteTicket(Long id) {
        logger.info("删除Ticket: id={}", id);
        if (id == null) {
            throw new IllegalArgumentException("Ticket ID 不能为空");
        }

        // 删除 Ticket（由于外键约束，关联的 ticket_tag 记录会自动删除）
        int deleted = ticketMapper.deleteById(id);
        if (deleted == 0) {
            logger.warn("Ticket不存在，无法删除: id={}", id);
            throw new IllegalArgumentException("Ticket 不存在，ID: " + id);
        }
        logger.info("Ticket删除成功: id={}", id);
    }

    @Override
    @Transactional
    public void completeTicket(Long id) {
        logger.info("标记Ticket为已完成: id={}", id);
        if (id == null) {
            throw new IllegalArgumentException("Ticket ID 不能为空");
        }

        Ticket ticket = ticketMapper.findById(id);
        if (ticket == null) {
            logger.warn("Ticket不存在: id={}", id);
            throw new IllegalArgumentException("Ticket 不存在，ID: " + id);
        }

        ticketMapper.updateStatus(id, 1, new Timestamp(System.currentTimeMillis()));
        logger.info("Ticket标记完成成功: id={}", id);
    }

    @Override
    @Transactional
    public void reopenTicket(Long id) {
        logger.info("取消完成Ticket: id={}", id);
        if (id == null) {
            throw new IllegalArgumentException("Ticket ID 不能为空");
        }

        Ticket ticket = ticketMapper.findById(id);
        if (ticket == null) {
            logger.warn("Ticket不存在: id={}", id);
            throw new IllegalArgumentException("Ticket 不存在，ID: " + id);
        }

        ticketMapper.updateStatus(id, 0, null);
        logger.info("Ticket取消完成成功: id={}", id);
    }

    @Override
    public Ticket getTicketWithTags(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Ticket ID 不能为空");
        }

        Ticket ticket = ticketMapper.findById(id);
        if (ticket == null) {
            return null;
        }

        // 查询关联的标签
        List<Tag> tags = tagMapper.findByTicketId(id);
        ticket.setTags(tags);

        return ticket;
    }

    @Override
    public PageResult<Ticket> listTickets(String keyword, Long tagId, int page, int pageSize) {
        // 参数校验和默认值
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询数据
        List<Ticket> tickets = ticketMapper.findPage(keyword, tagId, offset, pageSize);
        
        // 为每个 Ticket 加载标签
        for (Ticket ticket : tickets) {
            List<Tag> tags = tagMapper.findByTicketId(ticket.getId());
            ticket.setTags(tags);
        }

        // 查询总数
        int total = ticketMapper.count(keyword, tagId);

        return new PageResult<>(tickets, total, page, pageSize);
    }
}

