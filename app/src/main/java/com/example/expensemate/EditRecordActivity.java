package com.example.expensemate;

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

public class EditRecordActivity extends AppCompatActivity {
    private EditText editTextName, editTextPrice;
    private RadioGroup radioGroupType;
    private User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        radioGroupType = findViewById(R.id.radioGroupType);

        Intent intent = getIntent();
        SetRecordData(intent);

        findViewById(R.id.buttonEdit).setOnClickListener(v -> editRecord());
    }

    private void SetRecordData(Intent intent) {
        String recordId = intent.getStringExtra("recordId");
        String name = intent.getStringExtra("recordName");
        float price = intent.getFloatExtra("recordPrice", 0.0f);
        String recordType = intent.getStringExtra("recordType");

        editTextName.setText(name);
        editTextPrice.setText(String.valueOf(price));
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

        user.enterName(name);
        user.enterPrice(price);
        user.selectType(type);
        user.editRecord();

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}