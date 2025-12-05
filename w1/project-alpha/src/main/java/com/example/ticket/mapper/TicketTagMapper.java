package com.example.ticket.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TicketTag Mapper 接口
 * 
 * @author Project Alpha Team
 */
@Mapper
public interface TicketTagMapper {
    /**
     * 插入 Ticket-Tag 关联
     * @param ticketId Ticket ID
     * @param tagId Tag ID
     * @return 插入的行数
     */
    int insert(@Param("ticketId") Long ticketId, @Param("tagId") Long tagId);

    /**
     * 根据 Ticket ID 删除所有关联
     * @param ticketId Ticket ID
     * @return 删除的行数
     */
    int deleteByTicketId(Long ticketId);

    /**
     * 根据 Ticket ID 查询关联的 Tag ID 列表
     * @param ticketId Ticket ID
     * @return Tag ID 列表
     */
    List<Long> findTagIdsByTicketId(Long ticketId);

    /**
     * 根据 Tag ID 查询关联的 Ticket ID 列表
     * @param tagId Tag ID
     * @return Ticket ID 列表
     */
    List<Long> findTicketIdsByTagId(Long tagId);
}

