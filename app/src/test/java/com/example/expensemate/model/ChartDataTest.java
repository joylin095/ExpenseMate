package com.example.expensemate.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class ChartDataTest {
    ChartData chartData;

    @Before
    public void setUp() {
        chartData = new ChartData(Map.of("Tag1", 100.0f, "Tag2", 200.0f));
    }

    @Test
    public void testGetOtherTagSums() {
        Map<String, Float> otherTagSums = chartData.getOtherTagSums();
        assert otherTagSums.size() == 2;
        assert otherTagSums.get("Tag1") == 100.0f;
        assert otherTagSums.get("Tag2") == 200.0f;
    }
}
