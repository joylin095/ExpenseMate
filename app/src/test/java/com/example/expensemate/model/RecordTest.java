package com.example.expensemate.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;

public class RecordTest {

    private Record record;

    @Before
    public void setUp() {
        record = new Record();
    }

    @Test
    public void constructor_shouldInitializeFields() {
        assertNotNull(record.getId());
        assertNotNull(record.getDate());
        assertNotNull(record.getTags());
        assertTrue(record.getTags().isEmpty());
    }

    @Test
    public void copyConstructor_shouldCopyFieldsCorrectly() {
        record.setName("Food");
        record.setType("Expense");
        record.setPrice(500);
        record.setTag(new Tag("Food"));
        Record copy = new Record(record);

        assertEquals(record.getId(), copy.getId());
        assertEquals(record.getName(), copy.getName());
        assertEquals(record.getType(), copy.getType());
        assertEquals(record.getPrice(), copy.getPrice(), 0.001f);
        assertEquals(record.getDate(), copy.getDate());
        assertEquals(record.getTags(), copy.getTags());
    }

    @Test
    public void setName_shouldAcceptValidName() {
        record.setName("Dinner");
        assertEquals("Dinner", record.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setName_shouldThrowException_onInvalidName() {
        record.setName(""); // Invalid name
    }

    @Test
    public void setPrice_shouldCapAtMaxPrice() {
        record.setPrice(999999999);
        assertEquals(100000000.0f, record.getPrice(), 0.001f);
    }

    @Test
    public void setPrice_shouldAcceptNormalPrice() {
        record.setPrice(123.45f);
        assertEquals(123.45f, record.getPrice(), 0.001f);
    }

    @Test
    public void deleteTag_shouldRemoveTagIfNotLast() {
        Tag tag1 = new Tag("Food");
        Tag tag2 = new Tag("Drink");
        record.setTag(tag1);
        record.setTag(tag2);

        record.deleteTag(tag1);
        Set<Tag> tags = record.getTags();
        assertFalse(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTag_shouldThrowIfLastTag() {
        Tag tag = new Tag("OnlyTag");
        record.setTag(tag);
        record.deleteTag(tag); // should throw
    }

    @Test
    public void isValid_shouldReturnTrue_whenAllFieldsValid() {
        record.setName("Dinner");
        record.setType("Expense");
        record.setPrice(200);
        record.setDate(new Date());
        record.setTag(new Tag("Food"));

        assertTrue(record.isValid());
    }

    @Test
    public void isValid_shouldReturnFalse_whenMissingFields() {
        assertFalse(record.isValid()); // No name, type, price, tag
    }
}
