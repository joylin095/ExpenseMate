package com.example.expensemate.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Chart {
    private final RecordManager recordManager;
    public Chart(RecordManager recordManager) {
        this.recordManager = recordManager;
    }

    public List<String> getTagsForMonth(int year, int month) {
        return this.recordManager.getTagsForMonth(year, month);
    }

    public ChartData generateChartData(ChartFilter filter) {
        Map<String, Float> tagSums = new HashMap<>();
        int year = filter.getYear();
        int month = filter.getMonth();
        List<String> selectedTags = filter.getSelectedTags();

        List<Record> selectedRecords = recordManager.getRecordByTags(year, month, selectedTags);
        for (Record r : selectedRecords) {
            if (r.getType().equals("Income")){
                continue;
            }
            List<String> recordTags = r.getTags().stream().map(Tag::getName)
                    .collect(Collectors.toList());
            for (String tag : recordTags) {
                if (!selectedTags.contains(tag)) {
                    Float value = tagSums.getOrDefault(tag, 0f);
                    float price = (value != null) ? value : 0f;
                    tagSums.put(tag, price + r.getPrice());
                }
            }
        }

        return new ChartData(tagSums);
    }
}
