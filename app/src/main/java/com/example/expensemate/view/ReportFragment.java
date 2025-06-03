package com.example.expensemate.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemate.R;
import com.example.expensemate.model.User;
import com.example.expensemate.viewModel.SharedDateViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportFragment extends Fragment {
    private ChipGroup chipGroupAvailableTags;
    private static final int MAX_VISIBLE_TAGS = 5;
    private boolean tagsExpanded = false;
    private int currentYear, currentMonth;
    private User user = User.getInstance();
    private Set<String> selectedTags = new HashSet<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 取得當前日期
        SharedDateViewModel sharedDateViewModel = new ViewModelProvider(requireActivity()).get(SharedDateViewModel.class);
        sharedDateViewModel.getCurrentDate().observe(this, pair -> {
            currentYear = pair.first;
            currentMonth = pair.second;
            updateView();
        });
    }

    private void updateView() {
        selectedTags.clear();
        populateAvailableTags();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        chipGroupAvailableTags = view.findViewById(R.id.chipGroupAvailableTags);

        populateAvailableTags();
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
