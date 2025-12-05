package com.example.ticket.service;

import com.example.ticket.model.Tag;

import java.util.List;

/**
 * Tag Service 接口
 * 
 * @author Project Alpha Team
 */
public interface TagService {
    /**
     * 查询所有标签
     * @return 标签列表
     */
    List<Tag> listAllTags();

    /**
     * 根据标签名称列表查找或创建标签
     * @param names 标签名称列表
     * @return 标签列表
     */
    List<Tag> findOrCreateTagsByNames(List<String> names);

    /**
     * 根据 ID 查询标签
     * @param id 标签 ID
     * @return 标签对象
     */
    Tag getTagById(Long id);

    /**
     * 创建标签
     * @param name 标签名称
     * @return 标签 ID
     */
    Long createTag(String name);

    /**
     * 创建标签（带颜色）
     * @param name 标签名称
     * @param color 标签颜色（十六进制颜色代码，如 #0071e3）
     * @return 标签 ID
     */
    Long createTag(String name, String color);

    /**
     * 更新标签
     * @param id 标签 ID
     * @param name 新标签名称
     */
    void updateTag(Long id, String name);

    /**
     * 更新标签（带颜色）
     * @param id 标签 ID
     * @param name 新标签名称
     * @param color 标签颜色（十六进制颜色代码，如 #0071e3）
     */
    void updateTag(Long id, String name, String color);

    /**
     * 删除标签
     * @param id 标签 ID
     */
    void deleteTag(Long id);
}

