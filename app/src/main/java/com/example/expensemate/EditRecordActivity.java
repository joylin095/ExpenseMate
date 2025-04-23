package com.example.expensemate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensemate.controller.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditRecordActivity extends AppCompatActivity {
    private EditText editTextName, editTextPrice, editTextDate;
    private RadioGroup radioGroupType;
    private User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        initViews();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        radioGroupType = findViewById(R.id.radioGroupType);
        editTextDate = findViewById(R.id.editTextDate);

        Intent intent = getIntent();
        SetRecordData(intent);

        findViewById(R.id.btnEdit).setOnClickListener(v -> editRecord());
        initEditTextDate();
    }

    private void SetRecordData(Intent intent) {
        String recordId = intent.getStringExtra("recordId");
        String name = intent.getStringExtra("recordName");
        String recordType = intent.getStringExtra("recordType");
        String date = intent.getStringExtra("recordDate");
        float price = intent.getFloatExtra("recordPrice", 0.0f);

        editTextName.setText(name);
        editTextPrice.setText(String.valueOf(price));
        editTextDate.setText(date);
        if (recordType.equals("Income")) {
            radioGroupType.check(R.id.radioButtonIncome);
        } else {
            radioGroupType.check(R.id.radioButtonExpense);
        }

        user.selectRecord(recordId);
    }

    private void editRecord() {
        String name = editTextName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        float price = Float.parseFloat(priceStr);
        String type = radioGroupType.getCheckedRadioButtonId() == R.id.radioButtonIncome ? "Income" : "Expense";
        String dateStr = editTextDate.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(dateFormat.parse(dateStr));

            user.enterName(name);
            user.enterPrice(price);
            user.selectType(type);
            user.selectDate(calendar.getTime());
            user.editRecord();

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEditTextDate() {
        Calendar calendar = Calendar.getInstance();

        editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                editTextDate.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }
}