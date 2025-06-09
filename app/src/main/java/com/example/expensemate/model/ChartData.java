package com.example.expensemate.model;

import java.util.Map;

public class ChartData {
    private final Map<String, Float> tagSums;

    public ChartData(Map<String, Float> tagSums) {
        this.tagSums = tagSums;
    }

    public Map<String, Float> getTagSums() {
        return tagSums;
    }
}
