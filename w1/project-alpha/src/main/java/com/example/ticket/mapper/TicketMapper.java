package com.example.ticket.mapper;

import com.example.ticket.model.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * Ticket Mapper 接口
 * 
 * @author Project Alpha Team
 */
@Mapper
public interface TicketMapper {
    /**
     * 插入 Ticket
     * @param ticket Ticket 对象
     * @return 插入的行数
     */
    Long insert(Ticket ticket);

    /**
     * 更新 Ticket
     * @param ticket Ticket 对象
     * @return 更新的行数
     */
    int update(Ticket ticket);

    /**
     * 根据 ID 删除 Ticket
     * @param id Ticket ID
     * @return 删除的行数
     */
    int deleteById(Long id);

    /**
     * 根据 ID 查询 Ticket
     * @param id Ticket ID
     * @return Ticket 对象
     */
    Ticket findById(Long id);

    /**
     * 分页查询 Ticket 列表
     * @param keyword 搜索关键字（标题模糊匹配）
     * @param tagId 标签 ID（可选）
     * @param offset 偏移量
     * @param limit 每页数量
     * @return Ticket 列表
     */
    List<Ticket> findPage(@Param("keyword") String keyword,
                         @Param("tagId") Long tagId,
                         @Param("offset") int offset,
                         @Param("limit") int limit);

    /**
     * 统计 Ticket 数量
     * @param keyword 搜索关键字（标题模糊匹配）
     * @param tagId 标签 ID（可选）
     * @return 总数
     */
    int count(@Param("keyword") String keyword, @Param("tagId") Long tagId);

    /**
     * 更新 Ticket 状态
     * @param id Ticket ID
     * @param status 状态（0=未完成，1=已完成）
     * @param completedAt 完成时间
     * @return 更新的行数
     */
    int updateStatus(@Param("id") Long id,
                     @Param("status") int status,
                     @Param("completedAt") Timestamp completedAt);
}

