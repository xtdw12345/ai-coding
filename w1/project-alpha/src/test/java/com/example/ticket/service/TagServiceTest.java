package com.example.ticket.service;

import com.example.ticket.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TagService 测试类
 * 
 * @author Project Alpha Team
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional // 测试后回滚，不影响数据库
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    public void testListAllTags() {
        // 先创建一些标签
        tagService.findOrCreateTagsByNames(Arrays.asList("标签1", "标签2", "标签3"));

        // 查询所有标签
        List<Tag> tags = tagService.listAllTags();
        assertNotNull(tags);
        assertTrue(tags.size() >= 3);
    }

    @Test
    public void testFindOrCreateTagsByNames() {
        // 创建新标签
        List<Tag> tags = tagService.findOrCreateTagsByNames(
            Arrays.asList("新标签1", "新标签2")
        );

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.stream().allMatch(t -> t.getId() != null));

        // 再次调用，应该返回已存在的标签
        List<Tag> existingTags = tagService.findOrCreateTagsByNames(
            Arrays.asList("新标签1", "新标签2")
        );

        assertNotNull(existingTags);
        assertEquals(2, existingTags.size());
        // 验证返回的是同一个标签（ID相同）
        assertEquals(tags.get(0).getId(), existingTags.get(0).getId());
    }

    @Test
    public void testFindOrCreateTagsByNamesWithDuplicates() {
        // 测试去重功能
        List<Tag> tags = tagService.findOrCreateTagsByNames(
            Arrays.asList("重复标签", "重复标签", "重复标签")
        );

        assertNotNull(tags);
        assertEquals(1, tags.size()); // 应该去重
    }

    @Test
    public void testFindOrCreateTagsByNamesWithEmptyStrings() {
        // 测试空字符串过滤
        List<Tag> tags = tagService.findOrCreateTagsByNames(
            Arrays.asList("有效标签", "", "   ", null, "另一个标签")
        );

        assertNotNull(tags);
        assertEquals(2, tags.size()); // 只保留有效标签
    }

    @Test
    public void testFindOrCreateTagsByNamesWithNull() {
        // 测试 null 列表
        List<Tag> tags = tagService.findOrCreateTagsByNames(null);
        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }

    @Test
    public void testFindOrCreateTagsByNamesWithEmptyList() {
        // 测试空列表
        List<Tag> tags = tagService.findOrCreateTagsByNames(Arrays.asList());
        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }
}

