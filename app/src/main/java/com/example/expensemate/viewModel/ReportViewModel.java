package com.example.expensemate.viewModel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemate.model.Record;
import com.example.expensemate.model.Tag;
import com.example.expensemate.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportViewModel extends ViewModel {
    private List<String> selectedTags;
    private MutableLiveData<Map<String, Float>> tagCombinationLiveData = new MutableLiveData<>();
    private User user = User.getInstance();

    public LiveData<Map<String, Float>> getTagCombinationLiveData() {
        return tagCombinationLiveData;
    }

    public void updateSelectedTags(Set<String> tags, int year, int month) {
        selectedTags = new ArrayList<>(tags);
        processData(year, month);
    }

    private void processData(int year, int month) {
        List<Record> monthRecords = user.getRecordsForMonth(year, month);
        Map<String, Float> otherTagSums = new HashMap<>();
        Map<String, Float> comboTagSums = new HashMap<>();

        for (Record r : monthRecords) {
            List<String> recordTags = r.getTags().stream().map(Tag::getName)
                    .collect(Collectors.toList());
            if (recordTags.containsAll(selectedTags)) {
                // 所有 tag 組合 key
                List<String> sortedTags = new ArrayList<>(recordTags);
                Collections.sort(sortedTags);
                String key = TextUtils.join(",", sortedTags);
                comboTagSums.put(key, comboTagSums.getOrDefault(key, 0f) + r.getPrice());

                for (String tag : recordTags) {
                    if (!selectedTags.contains(tag)) {
                        otherTagSums.put(tag, otherTagSums.getOrDefault(tag, 0f) + r.getPrice());
                    }
                }
            }
        }

        tagCombinationLiveData.postValue(comboTagSums);
    }
}
