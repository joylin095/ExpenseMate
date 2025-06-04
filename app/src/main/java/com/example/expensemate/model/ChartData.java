package com.example.expensemate.model;

import java.util.Map;

public class ChartData {
    private final Map<String, Float> otherTagSums;

    public ChartData(Map<String, Float> otherTagSums) {
        this.otherTagSums = otherTagSums;
    }

    public Map<String, Float> getOtherTagSums() {
        return otherTagSums;
    }
}
