package com.example.expensemate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRecords;
    private RecordsAdapter recordsAdapter;
    private RecordsViewModel recordsViewModel;
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
        initViews();

        recordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        recordsViewModel.loadRecords();
                    }
                }
        );

        recordsViewModel.getRecordList().observe(this, records -> {
            recordsAdapter.setRecordList(records);
            recordsAdapter.notifyDataSetChanged();
        });

        recordsViewModel.loadRecords();
    }

    private void initViews() {
        recyclerViewRecords = findViewById(R.id.recyclerViewRecords);
        recyclerViewRecords.setLayoutManager(new LinearLayoutManager(this));

        recordsAdapter = new RecordsAdapter(recordsViewModel.getRecordList().getValue());
        recyclerViewRecords.setAdapter(recordsAdapter);

        // 新增紀錄跳轉畫面
        FloatingActionButton btnAddRecord = findViewById(R.id.btnAddRecord);
        btnAddRecord.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
            recordLauncher.launch(intent);
        });

        // 點擊紀錄跳轉畫面
        recordsAdapter.setOnItemClickListener((record, recordId) -> {
            Intent intent = new Intent(MainActivity.this, EditRecordActivity.class);
            intent.putExtra("recordId", recordId);
            intent.putExtra("recordName", record.getName());
            intent.putExtra("recordPrice", record.getPrice());
            intent.putExtra("recordType", record.getType());
            recordLauncher.launch(intent);
        });
    }
}