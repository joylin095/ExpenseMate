package com.example.expensemate;

import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditRecordActivity extends BaseRecordActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_record;
    }

    @Override
    protected void setupSpecificLogic() {
        Intent intent = getIntent();
        SetRecordData(intent);
        findViewById(R.id.btnEdit).setOnClickListener(v -> editRecord());
    }

    private void SetRecordData(Intent intent) {
        String recordId = intent.getStringExtra("recordId");
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
            setResult(RESULT_OK);
            finish();
        }
        catch (Exception e) {
            Toast.makeText(this, "請檢查資料，每項為必填", Toast.LENGTH_SHORT).show();
        }
    }
}