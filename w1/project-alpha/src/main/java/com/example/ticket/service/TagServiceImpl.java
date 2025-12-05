package com.example.ticket.service;

import com.example.ticket.mapper.TagMapper;
import com.example.ticket.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tag Service 实现类
 * 
 * @author Project Alpha Team
 */
@Service
public class TagServiceImpl implements TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> listAllTags() {
        return tagMapper.findAll();
    }

    @Override
    @Transactional
    public List<Tag> findOrCreateTagsByNames(List<String> names) {
        if (names == null || names.isEmpty()) {
            return new ArrayList<>();
        }

        // 去重、去掉空白字符串
        List<String> validNames = names.stream()
                .map(String::trim)
                .filter(name -> name != null && !name.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        List<Tag> result = new ArrayList<>();
        for (String name : validNames) {
            // 先查找是否存在
            Tag existingTag = tagMapper.findByName(name);
            if (existingTag != null) {
                logger.debug("标签已存在，复用: name={}, id={}", name, existingTag.getId());
                result.add(existingTag);
            } else {
                // 不存在则创建新标签
                Tag newTag = new Tag(name);
                tagMapper.insert(newTag);
                logger.info("创建新标签: name={}, id={}", name, newTag.getId());
                result.add(newTag);
            }
        }
        logger.debug("处理标签完成，共{}个", result.size());
        return result;
    }

    @Override
    public Tag getTagById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("标签 ID 不能为空");
        }
        return tagMapper.findById(id);
    }

    @Override
    @Transactional
    public Long createTag(String name) {
        return createTag(name, null);
    }

    @Override
    @Transactional
    public Long createTag(String name, String color) {
        logger.info("创建标签: name={}, color={}", name, color);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("标签名称长度不能超过50个字符");
        }

        String trimmedName = name.trim();
        
        // 检查标签是否已存在
        Tag existingTag = tagMapper.findByName(trimmedName);
        if (existingTag != null) {
            logger.warn("标签已存在: name={}, id={}", trimmedName, existingTag.getId());
            throw new IllegalArgumentException("标签名称已存在: " + trimmedName);
        }

        Tag tag = new Tag(trimmedName, color);
        tagMapper.insert(tag);
        logger.info("标签创建成功: id={}, name={}, color={}", tag.getId(), trimmedName, tag.getColor());
        return tag.getId();
    }

    @Override
    @Transactional
    public void updateTag(Long id, String name) {
        updateTag(id, name, null);
    }

    @Override
    @Transactional
    public void updateTag(Long id, String name, String color) {
        logger.info("更新标签: id={}, name={}, color={}", id, name, color);
        if (id == null) {
            throw new IllegalArgumentException("标签 ID 不能为空");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("标签名称长度不能超过50个字符");
        }

        Tag existingTag = tagMapper.findById(id);
        if (existingTag == null) {
            logger.warn("标签不存在: id={}", id);
            throw new IllegalArgumentException("标签不存在，ID: " + id);
        }

        String trimmedName = name.trim();
        
        // 检查新名称是否与其他标签冲突
        Tag tagWithSameName = tagMapper.findByName(trimmedName);
        if (tagWithSameName != null && !tagWithSameName.getId().equals(id)) {
            logger.warn("标签名称冲突: name={}", trimmedName);
            throw new IllegalArgumentException("标签名称已存在: " + trimmedName);
        }

        existingTag.setName(trimmedName);
        if (color != null && !color.trim().isEmpty()) {
            existingTag.setColor(color.trim());
        }
        tagMapper.update(existingTag);
        logger.info("标签更新成功: id={}, name={}, color={}", id, trimmedName, existingTag.getColor());
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        logger.info("删除标签: id={}", id);
        if (id == null) {
            throw new IllegalArgumentException("标签 ID 不能为空");
        }

        Tag tag = tagMapper.findById(id);
        if (tag == null) {
            logger.warn("标签不存在，无法删除: id={}", id);
            throw new IllegalArgumentException("标签不存在，ID: " + id);
        }

        // 注意：由于外键约束，如果标签被 Ticket 使用，删除会失败
        // 这里可以添加检查逻辑，但为了简化，直接删除，让数据库约束来处理
        int deleted = tagMapper.deleteById(id);
        if (deleted == 0) {
            logger.warn("标签删除失败: id={}", id);
            throw new IllegalArgumentException("标签删除失败，可能该标签正在被使用");
        }
        logger.info("标签删除成功: id={}, name={}", id, tag.getName());
    }
}

