package com.example.expensemate.manager;

import com.example.expensemate.model.Record;
import com.example.expensemate.model.Tag;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordManagerTest {

    private RecordManager manager;

    @Before
    public void setUp() {
        manager = new RecordManager();
    }

    @Test
    public void createRecord_shouldInitializeCurrentRecord() {
        manager.createRecord();
        assertNull(manager.getRecordName()); // 預設為 null
    }

    @Test
    public void enterAndGetName_shouldWork() {
        manager.createRecord();
        manager.enterName("午餐");
        assertEquals("午餐", manager.getRecordName());
    }

    @Test
    public void enterAndGetPrice_shouldWork() {
        manager.createRecord();
        manager.enterPrice(100f);
        assertEquals(100f, manager.getRecordPrice(), 0.001f);
    }

    @Test
    public void selectAndGetDate_shouldWork() {
        manager.createRecord();
        Date date = new Date();
        manager.selectDate(date);
        assertEquals(date, manager.getRecordDate());
    }

    @Test
    public void selectAndGetType_shouldWork() {
        manager.createRecord();
        manager.selectType("支出");
        assertEquals("支出", manager.getRecordType());
    }

    @Test
    public void setAndDeleteTag_shouldModifyTagsCorrectly() {
        manager.createRecord();
        Tag tag1 = new Tag("食物");
        Tag tag2 = new Tag("交通");

        manager.setTag(tag1);
        manager.setTag(tag2);
        assertEquals(2, manager.getSelectedTags().size());

        manager.deleteTag(tag1);
        assertEquals(1, manager.getSelectedTags().size());
        assertTrue(manager.getSelectedTags().contains("交通"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteLastTag_shouldThrow() {
        manager.createRecord();
        Tag tag = new Tag("唯一");
        manager.setTag(tag);
        manager.deleteTag(tag); // 應該拋出例外
    }

    @Test
    public void saveRecord_shouldStoreValidRecord() {
        manager.createRecord();
        manager.enterName("測試");
        manager.selectType("收入");
        manager.enterPrice(100f);
        manager.selectDate(new Date());
        manager.setTag(new Tag("測試標籤"));

        manager.saveRecord();

        Map<String, Record> saved = manager.getRecordList();
        assertEquals(1, saved.size());
        Record savedRecord = saved.values().iterator().next();
        assertEquals("測試", savedRecord.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveRecord_shouldFailIfInvalid() {
        // 沒設 name/type/price/tag 都會不合法
        manager.createRecord();
        manager.saveRecord(); // 應該拋出錯誤
    }

    @Test
    public void selectRecord_shouldCloneExistingRecord() {
        // 先建立與儲存一筆
        manager.createRecord();
        manager.enterName("原始");
        manager.selectType("收入");
        manager.enterPrice(100f);
        manager.selectDate(new Date());
        manager.setTag(new Tag("餐飲"));
        manager.saveRecord();

        String id = manager.getRecordList().keySet().iterator().next();

        manager.selectRecord(id); // 使用複製建構子

        assertEquals("原始", manager.getRecordName());
    }

    @Test
    public void editRecord_shouldUpdateExistingRecord() {
        manager.createRecord();
        // 建立初始紀錄
        manager.enterName("原始名稱");
        manager.selectType("收入");
        manager.enterPrice(100f);
        manager.selectDate(new Date());
        manager.setTag(new Tag("初始標籤"));
        manager.saveRecord();

        String id = manager.getRecordList().keySet().iterator().next();

        // 選取這筆紀錄並修改
        manager.selectRecord(id); // 用 copy constructor 建立 currentRecord
        manager.enterName("更新後名稱");
        manager.editRecord();     // 編輯更新進 recordList

        // 驗證紀錄已被覆蓋
        Record updated = manager.getRecordList().get(id);
        assert updated != null;
        assertEquals("更新後名稱", updated.getName());
    }

    @Test
    public void getRecordList_shouldReturnAllSavedRecords() {
        manager.createRecord();
        manager.enterName("紀錄一");
        manager.selectType("支出");
        manager.enterPrice(123f);
        manager.selectDate(new Date());
        manager.setTag(new Tag("食物"));
        manager.saveRecord();

        assertEquals(1, manager.getRecordList().size());
    }

    @Test
    public void getSelectedTags_shouldReturnTagNames() {
        manager.createRecord();

        Tag tag1 = new Tag("生活");
        Tag tag2 = new Tag("交通");

        manager.setTag(tag1);
        manager.setTag(tag2);

        assertEquals(2, manager.getSelectedTags().size());
        assertTrue(manager.getSelectedTags().contains("生活"));
        assertTrue(manager.getSelectedTags().contains("交通"));
    }

}
