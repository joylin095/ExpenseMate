package com.example.expensemate.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Toast;

import com.example.expensemate.R;
import com.example.expensemate.database.AppDatabase;
import com.example.expensemate.entity.RecordEntity;
import com.example.expensemate.entity.RecordMapper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditRecordActivity extends BaseRecordActivity {
    private String recordId;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_record;
    }

    @Override
    protected void setupSpecificLogic() {
        Intent intent = getIntent();
        setRecordData(intent);
        findViewById(R.id.btnEdit).setOnClickListener(v -> editRecord());
        findViewById(R.id.btnDelete).setOnClickListener(v -> deleteRecord());
    }

    private void setRecordData(Intent intent) {
        recordId = intent.getStringExtra("recordId");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        user.selectRecord(recordId);

        editTextName.setText(user.getRecordName());
        editTextPrice.setText(String.valueOf(user.getRecordPrice()));
        editTextDate.setText(dateFormat.format(user.getRecordDate()));
        if (user.getRecordType().equals("Income")) {
            radioGroupType.check(R.id.radioButtonIncome);
        } else {
            radioGroupType.check(R.id.radioButtonExpense);
        }

        populateAvailableTags();
        updateSelectedTags();
    }

    private void editRecord() {
        editTextName.clearFocus();
        editTextPrice.clearFocus();

        try {
            user.editRecord();

            RecordEntity entity = RecordMapper.toEntity(user.getRecord());
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            new Thread(() -> {
                db.recordDao().update(entity);
                runOnUiThread(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
            }).start();
        }
        catch (Exception e) {
            Toast.makeText(this, "請檢查資料，每項為必填", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRecord() {
        new AlertDialog.Builder(this)
                .setTitle("確認刪除")
                .setMessage("確定要刪除這筆紀錄嗎？")
                .setPositiveButton("確定", (dialog, which) -> {
                    AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                    new Thread(() -> {
                        user.deleteRecord(recordId);
                        db.recordDao().deleteById(recordId);
                        runOnUiThread(() -> {
                            setResult(RESULT_OK);
                            finish();
                        });
                    }).start();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}