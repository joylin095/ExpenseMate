package com.example.expensemate.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemate.MyApplication;
import com.example.expensemate.R;
import com.example.expensemate.model.Chart;
import com.example.expensemate.viewModel.ReportViewModel;
import com.example.expensemate.viewModel.SharedDateViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
    private BarChart barChart;
    private static final int MAX_VISIBLE_TAGS = 5;
    private boolean tagsExpanded = false;
    private int currentYear, currentMonth;
    private final Chart chart = MyApplication.getInstance().getChart();
    private final Set<String> selectedTags = new HashSet<>();
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
        barChart = view.findViewById(R.id.bar_chart);

        recyclerTagSummary = view.findViewById(R.id.recycler_tag_summary);
        recyclerTagSummary.setLayoutManager(new LinearLayoutManager(getContext()));

        tagCombinationAdapter = new TagCombinationAdapter(reportViewModel.getTagCombinationLiveData().getValue());
        recyclerTagSummary.setAdapter(tagCombinationAdapter);

        setupBarChart();
        populateAvailableTags();
    }

    private void setupBarChart() {
        if (barChart == null) return; // 安全檢查

        // 設置長條圖的基本配置
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);

        // 設置 X 軸
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // 設置 Y 軸
        barChart.getAxisLeft().setDrawGridLines(true);
        barChart.getAxisRight().setEnabled(false);

        // 設置圖例
        barChart.getLegend().setEnabled(true);
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
        reportViewModel.getBarChartLiveData().observe(getViewLifecycleOwner(), this::updateBarChart);
    }

    private void updateView() {
        selectedTags.clear();
        reportViewModel.updateSelectedTags(selectedTags, currentYear, currentMonth);
        populateAvailableTags();
    }

    private void updateBarChart(Map<String, Float> data) {
        if (barChart == null || data == null || data.isEmpty()) {
            return; // 安全檢查
        }

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Float> entry : data.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        if (entries.isEmpty()) {
            barChart.clear();
            return;
        }

        BarDataSet dataSet = new BarDataSet(entries, "支出金額");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.8f); // 設置長條寬度

        // 設置 X 軸標籤
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelCount(labels.size());

        barChart.setData(barData);
        barChart.setFitBars(true); // 讓長條圖適應寬度
        barChart.animateY(1000); // 添加動畫效果
        barChart.invalidate(); // 刷新圖表
    }
    private void populateAvailableTags() {
        chipGroupAvailableTags.removeAllViews();

        List<String> tags = chart.getTagsForMonth(currentYear, currentMonth); // 使用當前月份的標籤
        int displayCount = tagsExpanded ? tags.size() : Math.min(tags.size(), MAX_VISIBLE_TAGS);

        for (int i = 0; i < displayCount; i++) {
            Chip chip = getChip(tags, i);

            chipGroupAvailableTags.addView(chip);
        }

        if (tags.size() > MAX_VISIBLE_TAGS) {
            setToggleChip();
        }
    }

    @NonNull
    private Chip getChip(List<String> tags, int i) {
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
        return chip;
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
