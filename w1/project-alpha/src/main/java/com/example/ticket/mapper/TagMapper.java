package com.example.ticket.mapper;

import com.example.ticket.model.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Tag Mapper 接口
 * 
 * @author Project Alpha Team
 */
@Mapper
public interface TagMapper {
    /**
     * 根据名称查询 Tag
     * @param name 标签名称
     * @return Tag 对象
     */
    Tag findByName(String name);

    /**
     * 根据 ID 查询 Tag
     * @param id Tag ID
     * @return Tag 对象
     */
    Tag findById(Long id);

    /**
     * 插入 Tag
     * @param tag Tag 对象
     * @return 插入的行数
     */
    Long insert(Tag tag);

    /**
     * 查询所有 Tag
     * @return Tag 列表
     */
    List<Tag> findAll();

    /**
     * 根据 Ticket ID 查询关联的 Tag 列表
     * @param ticketId Ticket ID
     * @return Tag 列表
     */
    List<Tag> findByTicketId(Long ticketId);

    /**
     * 更新 Tag
     * @param tag Tag 对象
     * @return 更新的行数
     */
    int update(Tag tag);

    /**
     * 根据 ID 删除 Tag
     * @param id Tag ID
     * @return 删除的行数
     */
    int deleteById(Long id);
}

