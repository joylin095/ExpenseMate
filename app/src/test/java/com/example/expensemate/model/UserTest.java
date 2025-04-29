package com.example.expensemate.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void createAndSaveRecord_shouldSaveCorrectRecord() {
        user.createRecord();
        user.enterName("午餐");
        user.enterPrice(150f);
        user.selectType("支出");
        user.selectDate(new Date());

        user.addTag("食物");
        user.selectTag("食物");

        user.saveRecord();

        Map<String, Record> records = user.getRecordList();
        assertEquals(1, records.size());

        Record record = records.values().iterator().next();
        assertEquals("午餐", record.getName());
        assertEquals(150f, record.getPrice(), 0.01);
        assertEquals("支出", record.getType());
        assertFalse(record.getTags().isEmpty());
    }

    @Test
    public void editRecord_shouldUpdateRecordSuccessfully() {
        user.createRecord();
        user.enterName("午餐");
        user.enterPrice(100f);
        user.selectType("支出");
        user.selectDate(new Date());
        user.addTag("食物");
        user.selectTag("食物");
        user.saveRecord();

        String recordId = user.getRecordList().keySet().iterator().next();
        user.selectRecord(recordId);

        user.enterPrice(200f);
        user.editRecord();

        Record updatedRecord = user.getRecordList().get(recordId);
        assertEquals(200f, updatedRecord.getPrice(), 0.01);
    }

    @Test
    public void addAndDeleteTag_shouldWorkAsExpected() {
        user.createRecord();
        user.addTag("交通");
        user.selectTag("食物");
        user.selectTag("交通");
        List<String> selectedTags = user.getSelectedTags();
        assertTrue(selectedTags.contains("交通"));

        user.unselectTag("交通");
        selectedTags = user.getSelectedTags();
        assertFalse(selectedTags.contains("交通"));
    }

    @Test
    public void deleteLastTag_shouldThrowException() {
        user.createRecord();
        user.addTag("娛樂");
        user.selectTag("娛樂");

        List<String> selectedTags = user.getSelectedTags();
        assertEquals(1, selectedTags.size());
        assertTrue(selectedTags.contains("娛樂"));

        try {
            user.unselectTag("娛樂");
            fail("Should throw exception when deleting the last tag");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot delete the last tag", e.getMessage());
        }
    }


    @Test
    public void getAvailableTags_shouldReturnDefaultTags() {
        List<String> tags = user.getAvailableTags();
        assertTrue(tags.contains("食物"));
        assertTrue(tags.contains("娛樂"));
        assertTrue(tags.contains("交通"));
    }

    @Test
    public void saveRecord_withoutTag_shouldThrowException() {
        user.createRecord();
        user.enterName("測試");
        user.enterPrice(100f);
        user.selectType("支出");
        user.selectDate(new Date());

        try {
            user.saveRecord();
            fail("Expected exception for missing tag");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid record", e.getMessage());
        }
    }
}
