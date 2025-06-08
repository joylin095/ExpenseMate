package com.example.expensemate.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

public class ChartFilterTest {
    ChartFilter chartFilter;

    @Test
    public void getYear_Test() {
        chartFilter = new ChartFilter(2025, 5, List.of("Food", "Transport"), ChartType.PIE);
        assertEquals(2025, chartFilter.getYear());
    }

    @Test
    public void getMonth_Test() {
        chartFilter = new ChartFilter(2025, 5, List.of("Food", "Transport"), ChartType.PIE);
        assertEquals(5, chartFilter.getMonth());
    }

    @Test
    public void getSelectedTags_Test() {
        chartFilter = new ChartFilter(2025, 5, List.of("Food", "Transport"), ChartType.PIE);
        assertEquals(List.of("Food", "Transport"), chartFilter.getSelectedTags());
    }

    @Test
    public void getChartType_Test() {
        chartFilter = new ChartFilter(2025, 5, List.of("Food", "Transport"), ChartType.PIE);
        assertEquals(ChartType.PIE, chartFilter.getChartType());
    }
}
