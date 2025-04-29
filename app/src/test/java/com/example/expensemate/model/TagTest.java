package com.example.expensemate.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TagTest {
    private Tag tag;
    final private String tagName = "forTest";

    @Before
    public void setUp() {
        tag = new Tag(tagName);
    }

    @Test
    public void getName() {
        assertEquals(tagName, tag.getName());
    }

    @Test
    public void matchName() {
        assertTrue(tag.matchName(tagName));
    }
}