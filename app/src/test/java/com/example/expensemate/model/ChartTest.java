package com.example.expensemate.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ChartTest {
    private Chart chart = null;
    private RecordManager recordManager = null;
    @Before
    public void setUp() {
        recordManager = new RecordManager();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 8);
        Date date = calendar.getTime();
        List<String> tags = new ArrayList<>();
        tags.add("食物");

        recordManager.createRecord();
        recordManager.enterName("午餐");
        recordManager.selectType("支出");
        recordManager.enterPrice(150f);
        recordManager.selectDate(date);
        recordManager.setTag(new Tag("食物"));
        recordManager.setTag(new Tag("需要"));
        recordManager.saveRecord();

        recordManager.createRecord();
        recordManager.enterName("晚餐");
        recordManager.selectType("支出");
        recordManager.enterPrice(200f);
        recordManager.selectDate(date);
        recordManager.setTag(new Tag("食物"));
        recordManager.setTag(new Tag("需要"));
        recordManager.saveRecord();

        recordManager.createRecord();
        recordManager.enterName("咖啡");
        recordManager.selectType("支出");
        recordManager.enterPrice(200f);
        recordManager.selectDate(date);
        recordManager.setTag(new Tag("食物"));
        recordManager.setTag(new Tag("想要"));
        recordManager.saveRecord();

        chart = new Chart(recordManager);
    }

    @Test
    public void generateChart_shouldReturnChartData() {
        final float expectedValue = 350f;
        List<String> selectedTags = new ArrayList<>();
        selectedTags.add("食物");
        ChartFilter filter = new ChartFilter(2025, Calendar.JUNE, selectedTags, ChartType.BAR);
        ChartData data = chart.generateChartData(filter);
        Map<String, Float> tags = data.getTagSums();
        assertNotNull(tags.get("需要"));
        assertEquals(expectedValue, tags.get("需要"), 0.01);
    }
}
