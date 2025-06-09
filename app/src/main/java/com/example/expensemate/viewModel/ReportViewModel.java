package com.example.expensemate.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemate.MyApplication;
import com.example.expensemate.model.ChartData;
import com.example.expensemate.model.ChartFilter;
import com.example.expensemate.model.ChartType;
import com.example.expensemate.model.Chart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class ReportViewModel extends ViewModel {
    private List<String> selectedTags;
    private MutableLiveData<Map<String, Float>> tagCombinationLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, Float>> barChartLiveData = new MutableLiveData<>();
    private Chart chart = null;

    // Constructor for dependency injection
    public ReportViewModel(Chart chart) {
        this.chart = chart;
    }

    // Default constructor for production use
    public ReportViewModel() {
        this(MyApplication.getInstance().getChart());
    }

    public LiveData<Map<String, Float>> getTagCombinationLiveData() {
        return tagCombinationLiveData;
    }

    public LiveData<Map<String, Float>> getBarChartLiveData() {
        return barChartLiveData;
    }

    public void updateSelectedTags(Set<String> tags, int year, int month) {
        selectedTags = new ArrayList<>(tags);
        updateTagCombinationLiveData(year, month);

        ChartFilter filter = new ChartFilter(year, month, selectedTags, ChartType.BAR);
        updateChartLiveData(filter);
    }

    private void updateTagCombinationLiveData(int year, int month) {
        Map<String, Float> comboTagSums = chart.getTagCombinationSums(year, month, selectedTags);

        Map<String, Float> sortedComboTagSums = comboTagSums.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().split(",").length))
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new // 保留排序
                ));

        tagCombinationLiveData.setValue(sortedComboTagSums);
    }

    private void updateChartLiveData(ChartFilter filter) {
        ChartData chartData = chart.generateChartData(filter);

        switch (filter.getChartType()) {
            case BAR:
                barChartLiveData.setValue(chartData.getTagSums());
        }
    }
}
