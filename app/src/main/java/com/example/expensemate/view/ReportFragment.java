package com.example.expensemate.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemate.R;
import com.example.expensemate.model.User;
import com.example.expensemate.viewModel.ReportViewModel;
import com.example.expensemate.viewModel.SharedDateViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportFragment extends Fragment {
    private ChipGroup chipGroupAvailableTags;
    private RecyclerView recyclerTagSummary;
    private TagCombinationAdapter tagCombinationAdapter;
    private PieChart pieChart;
    private static final int MAX_VISIBLE_TAGS = 5;
    private boolean tagsExpanded = false;
    private int currentYear, currentMonth;
    private User user = User.getInstance();
    private Set<String> selectedTags = new HashSet<>();
    private ReportViewModel reportViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        initViews(view);
        setObserve();

        return view;
    }

    private void initViews(View view) {
        chipGroupAvailableTags = view.findViewById(R.id.chipGroupAvailableTags);
        pieChart = view.findViewById(R.id.pie_chart);

        recyclerTagSummary = view.findViewById(R.id.recycler_tag_summary);
        recyclerTagSummary.setLayoutManager(new LinearLayoutManager(getContext()));

        tagCombinationAdapter = new TagCombinationAdapter(reportViewModel.getTagCombinationLiveData().getValue());
        recyclerTagSummary.setAdapter(tagCombinationAdapter);

        populateAvailableTags();
    }

    private void setObserve() {
        // 取得當前日期
        SharedDateViewModel sharedDateViewModel = new ViewModelProvider(requireActivity()).get(SharedDateViewModel.class);
        sharedDateViewModel.getCurrentDate().observe(getViewLifecycleOwner(), pair -> {
            currentYear = pair.first;
            currentMonth = pair.second;
            updateView();
        });

        // 更新下方recyclerView的資料
        reportViewModel.getTagCombinationLiveData().observe(getViewLifecycleOwner(), tagCombinations -> {
            tagCombinationAdapter.setTagCombinations(tagCombinations);
            tagCombinationAdapter.notifyDataSetChanged();
        });

        // 更新餅圖資料
        reportViewModel.getPieChartLiveData().observe(getViewLifecycleOwner(), pieChartData -> {
            updatePieChart(pieChartData);
        });
    }

    private void updateView() {
        selectedTags.clear();
        reportViewModel.updateSelectedTags(selectedTags, currentYear, currentMonth);
        populateAvailableTags();
    }

    private void updatePieChart(Map<String, Float> data) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : data.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Other Tags");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieChartData = new PieData(dataSet);

        pieChart.setData(pieChartData);
        pieChart.invalidate(); // refresh
    }

    private void populateAvailableTags() {
        chipGroupAvailableTags.removeAllViews();

        List<String> tags = user.getTagsForMonth(currentYear, currentMonth); // 使用當前月份的標籤
        int displayCount = tagsExpanded ? tags.size() : Math.min(tags.size(), MAX_VISIBLE_TAGS);

        for (int i = 0; i < displayCount; i++) {
            String tagName = tags.get(i);
            Chip chip = new Chip(getContext());
            chip.setText(tagName);
            chip.setCheckable(true);
            chip.setChecked(selectedTags.contains(tagName));

            chip.setOnClickListener(v -> {
                if (chip.isChecked()) {
                    selectedTags.add(tagName);
                } else {
                    selectedTags.remove(tagName);
                }
                reportViewModel.updateSelectedTags(selectedTags, currentYear, currentMonth);
            });

            chipGroupAvailableTags.addView(chip);
        }

        if (tags.size() > MAX_VISIBLE_TAGS) {
            setToggleChip();
        }
    }

    private void setToggleChip() {
        Chip chip = new Chip(getContext());
        chip.setText(tagsExpanded ? "▲" : "▼");
        chip.setCheckable(false);

        chip.setOnClickListener(v -> {
            tagsExpanded = !tagsExpanded;
            populateAvailableTags();
        });
        chipGroupAvailableTags.addView(chip);
    }
}
