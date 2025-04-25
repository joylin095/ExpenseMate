package com.example.expensemate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemate.controller.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;

public class AddRecordActivity extends AppCompatActivity {
    private EditText editTextName, editTextPrice, editTextDate;
    private RadioGroup radioGroupType;
    private RadioButton radioButtonExpense;
    private ChipGroup chipGroupSelectedTags, chipGroupAvailableTags;
    private String type = "Expense";
    private User user = User.getInstance();
    private List<String> selectedTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        user.createRecord();
        initViews();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        radioGroupType = findViewById(R.id.radioGroupType);
        radioButtonExpense = findViewById(R.id.radioButtonExpense);
        editTextDate = findViewById(R.id.editTextDate);
        chipGroupSelectedTags = findViewById(R.id.chipGroupSelectedTags);
        chipGroupAvailableTags = findViewById(R.id.chipGroupAvailableTags);

        radioButtonExpense.setChecked(true);

        radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonIncome) {
                type = "Income";
            } else {
                type = "Expense";
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(v -> saveRecord());

        initEditTextDate();
        populateAvailableTags();
    }

    private void initEditTextDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateString = dateFormat.format(calendar.getTime());
        editTextDate.setText(currentDateString);

        editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                editTextDate.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void populateAvailableTags() {
        chipGroupAvailableTags.removeAllViews();

        for (String tagName : user.getAvaliableTags()){
            Chip chip = new Chip(this);
            chip.setText(tagName);
            chip.setCheckable(false);
            chip.setClickable(true);

            chip.setOnClickListener(v -> {
                user.selectTag(tagName);
                updateSelectedTags();
            });
            chipGroupAvailableTags.addView(chip);
        }
    }

    private void updateSelectedTags() {
        chipGroupSelectedTags.removeAllViews();

        for (String tagName : user.getSelectedTags()) {
            Chip chip = new Chip(this);
            chip.setText(tagName);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                user.deleteSelectedTag(tagName);
                updateSelectedTags();
            });
            chipGroupSelectedTags.addView(chip);
        }
    }

    private void saveRecord() {
        String name = editTextName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String dateStr = editTextDate.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try{
            float price = Float.parseFloat(priceStr);
            calendar.setTime(dateFormat.parse(dateStr));

            user.selectType(type);
            user.enterName(name);
            user.enterPrice(price);
            user.selectDate(calendar.getTime());
            user.saveRecord();

            setResult(RESULT_OK);
            finish();
        }
        catch (Exception e){
            Toast.makeText(this, "格式錯誤", Toast.LENGTH_SHORT).show();
        }
    }
}
