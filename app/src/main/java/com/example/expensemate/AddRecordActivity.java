package com.example.expensemate;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemate.controller.RecordsViewModel;
import com.example.expensemate.controller.User;

public class AddRecordActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextPrice;
    private RadioGroup radioGroupType;
    private RadioButton radioButtonExpense;
    private String type = "Expense";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        initViews();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        radioGroupType = findViewById(R.id.radioGroupType);
        radioButtonExpense = findViewById(R.id.radioButtonExpense);

        radioButtonExpense.setChecked(true);

        radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonIncome) {
                type = "Income";
            } else {
                type = "Expense";
            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(v -> saveRecord());
    }

    private void saveRecord() {
        String name = editTextName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        float price = Float.parseFloat(priceStr);

        user = User.getInstance();
        user.createRecord();
        user.selectType(type);
        user.enterName(name);
        user.enterPrice(price);
        user.saveRecord();

        setResult(RESULT_OK);
        finish();
    }
}
