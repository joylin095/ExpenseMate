package com.example.expensemate.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TagManagerTest {
    private TagManager tagManager;

    @Before
    public void setUp() {
        tagManager = new TagManager();
    }

    @Test
    public void constructor_shouldContainDefaultTags() {
        List<String> tags = tagManager.getTagList();
        assertTrue(tags.contains("食物"));
        assertTrue(tags.contains("娛樂"));
        assertTrue(tags.contains("交通"));
        assertEquals(3, tags.size());
    }

    @Test
    public void createTag_shouldAddNewTag_whenNotExists() {
        tagManager.createTag("醫療");
        List<String> tags = tagManager.getTagList();
        assertTrue(tags.contains("醫療"));
        assertEquals(4, tags.size());
    }

    @Test
    public void createTag_shouldNotAddDuplicateTag() {
        tagManager.createTag("娛樂"); // already exists
        List<String> tags = tagManager.getTagList();
        assertEquals(3, tags.size()); // should not add again
    }

    @Test
    public void selectTag_shouldReturnCorrectTag() {
        Tag tag = tagManager.selectTag("交通");
        assertNotNull(tag);
        assertEquals("交通", tag.getName());
    }

    @Test
    public void selectTag_shouldReturnNull_ifNotFound() {
        Tag tag = tagManager.selectTag("不存在");
        assertNull(tag);
    }
}