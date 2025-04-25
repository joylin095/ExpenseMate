package com.example.expensemate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemate.adapter.RecordsAdapter;
import com.example.expensemate.controller.RecordsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRecords;
    private RecordsAdapter recordsAdapter;
    private RecordsViewModel recordsViewModel;
    private TextView textViewMonth, textViewBalance, textViewIncome, textViewExpense;
    private int currentYear, currentMonth;
    private ActivityResultLauncher<Intent> recordLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        recordsViewModel.getRecordList().observe(this, records -> {
            recordsAdapter.setItemList(records);
            recordsAdapter.notifyDataSetChanged();
        });

        initViews();
        initCurrentDate();
        initMonth();
        updateViews();

        recordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        updateViews();
                    }
                }
        );
    }

    private void initViews() {
        recyclerViewRecords = findViewById(R.id.recyclerViewRecords);
        recyclerViewRecords.setLayoutManager(new LinearLayoutManager(this));

        recordsAdapter = new RecordsAdapter(recordsViewModel.getRecordList().getValue());
        recyclerViewRecords.setAdapter(recordsAdapter);

        textViewMonth = findViewById(R.id.textViewMonth);
        textViewBalance = findViewById(R.id.textViewBalance);
        textViewIncome = findViewById(R.id.textViewIncome);
        textViewExpense = findViewById(R.id.textViewExpense);

        // 新增紀錄跳轉畫面
        FloatingActionButton btnAddRecord = findViewById(R.id.btnAddRecord);
        btnAddRecord.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
            recordLauncher.launch(intent);
        });

        // 點擊紀錄跳轉畫面
        recordsAdapter.setOnItemClickListener((recordId) -> {
            Intent intent = new Intent(MainActivity.this, EditRecordActivity.class);
            intent.putExtra("recordId", recordId);
            recordLauncher.launch(intent);
        });
    }

    private void initCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
    }

    private void initMonth() {
        findViewById(R.id.btnPreviousMonth).setOnClickListener(v -> {
            showPreviousMonth();
        });

        findViewById(R.id.btnNextMonth).setOnClickListener(v -> {
            showNextMonth();
        });
    }

    private void updateViews() {
        loadRecordsForCurrentMonth();
        updateTotal();
        updateMonthDisplay();
    }

    private void loadRecordsForCurrentMonth() {
        recordsViewModel.loadRecords(currentYear, currentMonth);
    }

    @SuppressLint("SetTextI18n")
    private void updateMonthDisplay() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy MM月", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        String monthString = monthFormat.format(calendar.getTime());
        textViewMonth.setText(monthString);
    }

    private void updateTotal() {
        Map<String, Float> totals = recordsViewModel.calculateTotals();
        textViewIncome.setText(String.valueOf(totals.get("Income")));
        textViewExpense.setText(String.valueOf(totals.get("Expense")));
        textViewBalance.setText(String.valueOf(totals.get("Balance")));
    }

    private void showPreviousMonth() {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        updateViews();
    }

    private void showNextMonth() {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        updateViews();
    }
}