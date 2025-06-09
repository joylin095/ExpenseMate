package com.example.expensemate.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import com.example.expensemate.MyApplication;

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

    @Test
    public void getRecord_shouldReturnCorrectRecord() {
        user.createRecord();
        user.enterName("晚餐");
        user.enterPrice(200f);
        user.selectType("支出");
        user.selectDate(new Date());
        user.addTag("食物");
        user.selectTag("食物");
        user.saveRecord();

        Record record = user.getRecord();

        assertNotNull(record);
        assertEquals("晚餐", record.getName());
        assertEquals(200f, record.getPrice(), 0.01);
        assertEquals("支出", record.getType());
    }

    @Test
    public void setRecords_shouldUpdateRecords() {
        List<Record> records = user.getRecordList().values().stream().toList();
        assertTrue(records.isEmpty());

        Record record1 = new Record();
        Record record2 = new Record();

        user.setRecords(List.of(record1, record2));

        Map<String, Record> updatedRecords = user.getRecordList();
        assertEquals(2, updatedRecords.size());
    }

    @Test
    public void setTags_shouldUpdateTags() {
        List<String> newTags = List.of("食物", "娛樂", "交通");
        user.setTags(newTags);

        List<String> updatedTags = user.getAvailableTags();
        assertEquals(3, updatedTags.size());
        assertTrue(updatedTags.containsAll(newTags));
    }

    @Test
    public void deleteRecord_shouldRemoveRecord() {
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

        String recordId = records.keySet().iterator().next();
        user.deleteRecord(recordId);

        records = user.getRecordList();
        assertTrue(records.isEmpty());
    }

    @Test
    public void getRecordType_shouldReturnCorrectType() {
        user.createRecord();
        user.selectType("收入");
        String type = user.getRecordType();
        assertEquals("收入", type);
    }

    @Test
    public void getRecordName_shouldReturnCorrectName() {
        user.createRecord();
        user.enterName("午餐");
        String name = user.getRecordName();
        assertEquals("午餐", name);
    }

    @Test
    public void getRecordPrice_shouldReturnCorrectPrice() {
        user.createRecord();
        user.enterPrice(100f);
        float price = user.getRecordPrice();
        assertEquals(100f, price, 0.01);
    }

    @Test
    public void getRecordDate_shouldReturnCorrectDate() {
        user.createRecord();
        Date date = new Date();
        user.selectDate(date);
        Date recordDate = user.getRecordDate();
        assertEquals(date, recordDate);
    }

    @Test
    public void getReocrdsForMonth_shouldReturnRecordsForGivenMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 8);
        Date date = calendar.getTime();

        user.createRecord();
        user.enterName("午餐");
        user.enterPrice(150f);
        user.selectType("支出");
        user.selectDate(date);
        user.addTag("食物");
        user.selectTag("食物");
        user.saveRecord();

        List<Record> records = user.getRecordsForMonth(2025, Calendar.JUNE);
        assertEquals(1, records.size());
        assertEquals("午餐", records.get(0).getName());
    }

    @Test
    public void getTagsForMonth_shouldReturnTagsForGivenMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 8);
        Date date = calendar.getTime();

        user.createRecord();
        user.enterName("午餐");
        user.enterPrice(150f);
        user.selectType("支出");
        user.selectDate(date);
        user.addTag("食物");
        user.selectTag("食物");
        user.saveRecord();

        List<String> tags = user.getTagsForMonth(2025, Calendar.JUNE);
        assertEquals(1, tags.size());
        assertTrue(tags.contains("食物"));
    }

    @Test
    public void getTagCombinationSums_shouldReturnCorrectSums() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 8);
        Date date = calendar.getTime();

        user.createRecord();
        user.enterName("午餐");
        user.enterPrice(150f);
        user.selectType("支出");
        user.selectDate(date);
        user.addTag("食物");
        user.selectTag("食物");
        user.saveRecord();

        Map<String, Float> tagSums = user.getTagCombinationSums(2025, Calendar.JUNE, List.of("食物"));
        assertEquals(1, tagSums.size());
        assertTrue(tagSums.containsKey("食物"));
        assertEquals(150f, tagSums.get("食物"), 0.01);
    }

    @Test
    public void getChartData_shouldReturnCorrectChartData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 8);
        Date date = calendar.getTime();

        user.createRecord();
        user.enterName("午餐");
        user.enterPrice(150f);
        user.selectType("支出");
        user.selectDate(date);
        user.addTag("食物");
        user.selectTag("食物");
        user.saveRecord();

        ChartFilter filter = new ChartFilter(2025, Calendar.JUNE, List.of("食物"), ChartType.PIE);
        ChartData chartData = user.getChartData(filter);

        assertNotNull(chartData);
        assertTrue(chartData.getOtherTagSums().isEmpty());
    }
}
