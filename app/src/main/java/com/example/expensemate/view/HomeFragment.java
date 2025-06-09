package com.example.expensemate.view;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemate.Factory.ViewModelFactory;
import com.example.expensemate.MyApplication;
import com.example.expensemate.R;
import com.example.expensemate.model.Record;
import com.example.expensemate.model.User;
import com.example.expensemate.viewModel.RecordsViewModel;
import com.example.expensemate.viewModel.SharedDateViewModel;
import com.example.expensemate.viewModel.TagsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kal.rackmonthpicker.RackMonthPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewRecords;
    private RecordsAdapter recordsAdapter;
    private RecordsViewModel recordsViewModel;
    private TagsViewModel tagsViewModel;
    private TextView textViewBalance, textViewIncome, textViewExpense;
    private int currentYear, currentMonth;
    private final User user = MyApplication.getInstance().getUser();

    private ActivityResultLauncher<Intent> recordLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelFactory factory = new ViewModelFactory(requireContext());
        recordsViewModel = new ViewModelProvider(this, factory).get(RecordsViewModel.class);
        tagsViewModel = new ViewModelProvider(this, factory).get(TagsViewModel.class);

        // 新增或修改紀錄完成後更新畫面
        recordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        updateViews();
                    }
                }
        );

        // 取得當前日期
        SharedDateViewModel sharedDateViewModel = new ViewModelProvider(requireActivity()).get(SharedDateViewModel.class);
        sharedDateViewModel.getCurrentDate().observe(this, pair -> {
            currentYear = pair.first;
            currentMonth = pair.second;
            updateViews();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupObservers();

        loadRecords();
        updateViews();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupObservers() {
        recordsViewModel.getRecordList().observe(getViewLifecycleOwner(), records -> {
            recordsAdapter.setItemList(records);
            recordsAdapter.notifyDataSetChanged();
        });
    }

    private void initViews(View view) {
        recyclerViewRecords = view.findViewById(R.id.recyclerViewRecords);
        recyclerViewRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        recordsAdapter = new RecordsAdapter(recordsViewModel.getRecordList().getValue());
        recyclerViewRecords.setAdapter(recordsAdapter);

        textViewBalance = view.findViewById(R.id.textViewBalance);
        textViewIncome = view.findViewById(R.id.textViewIncome);
        textViewExpense = view.findViewById(R.id.textViewExpense);

        // 點選新增紀錄
        FloatingActionButton btnAddRecord = view.findViewById(R.id.btnAddRecord);
        btnAddRecord.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddRecordActivity.class);
            recordLauncher.launch(intent);
        });

        // 點選紀錄修改
        recordsAdapter.setOnItemClickListener((recordId) -> {
            Intent intent = new Intent(getContext(), EditRecordActivity.class);
            intent.putExtra("recordId", recordId);
            recordLauncher.launch(intent);
        });
    }

    private void loadRecords() {
        new Thread(() -> {
            List<Record> records = recordsViewModel.getRecordsFromDB();
            List<String> tags = tagsViewModel.getTagsFromDB();
            user.setRecords(records);
            user.setTags(tags);

            Activity activity = getActivity();
            if (activity != null && isAdded()) {
                activity.runOnUiThread(this::updateViews);
            }
        }).start();
    }


    private void updateViews() {
        loadRecordsForCurrentMonth();
        updateTotal();
    }

    private void loadRecordsForCurrentMonth() {
        recordsViewModel.loadRecords(currentYear, currentMonth);
    }

    private void updateTotal() {
        Map<String, Float> totals = recordsViewModel.calculateTotals();
        textViewIncome.setText(String.valueOf(totals.get("Income")));
        textViewExpense.setText(String.valueOf(totals.get("Expense")));
        textViewBalance.setText(String.valueOf(totals.get("Balance")));
    }
}

