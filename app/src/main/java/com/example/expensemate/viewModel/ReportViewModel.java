package com.example.expensemate.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemate.model.ChartData;
import com.example.expensemate.model.ChartFilter;
import com.example.expensemate.model.ChartType;
import com.example.expensemate.model.User;

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
    private MutableLiveData<Map<String, Float>> pieChartLiveData = new MutableLiveData<>();
    private User user = User.getInstance();

    public LiveData<Map<String, Float>> getTagCombinationLiveData() {
        return tagCombinationLiveData;
    }

    public LiveData<Map<String, Float>> getPieChartLiveData() {
        return pieChartLiveData;
    }

    public void updateSelectedTags(Set<String> tags, int year, int month) {
        selectedTags = new ArrayList<>(tags);
        updateTagCombinationLiveData(year, month);

        ChartFilter filter = new ChartFilter(year, month, selectedTags, ChartType.PIE);
        updatePieChartLiveData(filter);
    }

    private void updateTagCombinationLiveData(int year, int month) {
        Map<String, Float> comboTagSums = user.getTagCombinationSums(year, month, selectedTags);

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

    private void updatePieChartLiveData(ChartFilter filter) {
        ChartData chartData = user.getChartData(filter);

        switch (filter.getChartType()) {
            case PIE:
                pieChartLiveData.setValue(chartData.getOtherTagSums());
        }
    }
}
