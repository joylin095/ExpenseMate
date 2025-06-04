package com.example.expensemate.model;

import java.util.List;

public class ChartFilter {
    private final int year;
    private final int month;
    private final List<String> selectedTags;
    private final ChartType chartType; // 可以用來選擇圓餅、長條、折線等圖表

    public ChartFilter(int year, int month, List<String> selectedTags, ChartType chartType) {
        this.year = year;
        this.month = month;
        this.selectedTags = selectedTags;
        this.chartType = chartType;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public ChartType getChartType() {
        return chartType;
    }
}
