package com.example.expensemate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemate.controller.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditRecordActivity extends AppCompatActivity {
    private EditText editTextName, editTextPrice, editTextDate;
    private RadioGroup radioGroupType;
    private ChipGroup chipGroupSelectedTags, chipGroupAvailableTags;
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
        chipGroupSelectedTags = findViewById(R.id.chipGroupSelectedTags);
        chipGroupAvailableTags = findViewById(R.id.chipGroupAvailableTags);

        Intent intent = getIntent();
        SetRecordData(intent);

        initEditTextName();
        initEditTextPrice();
        initEditRadioGroup();
        initEditTextDate();

        findViewById(R.id.btnEdit).setOnClickListener(v -> editRecord());

    }

    private void initEditTextName() {
        editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try{
                    String name = editTextName.getText().toString().trim();
                    user.enterName(name);
                    editTextName.setText(user.getRecordName());
                } catch (Exception e) {
                    editTextName.setError("名稱不可為空");
                }
            }
        });
    }

    private void initEditTextPrice(){
        editTextPrice.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String priceStr = editTextPrice.getText().toString().trim();
                try {
                    float price = Float.parseFloat(priceStr);
                    user.enterPrice(price);
                    editTextPrice.setText(String.valueOf(user.getRecordPrice()));
                } catch (Exception e) {
                    editTextPrice.setError("價格格式錯誤或不為空");
                    user.enterPrice(0);
                }
            }
        });
    }

    private void initEditRadioGroup() {
        radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonIncome) {
                user.selectType("Income");
            } else if (checkedId == R.id.radioButtonExpense) {
                user.selectType("Expense");
            }
        });
    }

    private void initEditTextDate() {
        editTextDate.setFocusableInTouchMode(false);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                editTextDate.setText(selectedDate);
                try {
                    calendar.setTime(dateFormat.parse(selectedDate));
                    user.selectDate(calendar.getTime());
                } catch (Exception e) {
                    Toast.makeText(this, "日期格式錯誤", Toast.LENGTH_SHORT).show();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
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

    private void populateAvailableTags() {
        chipGroupAvailableTags.removeAllViews();

        setAddTagChip();
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
                try{
                    user.deleteSelectedTag(tagName);
                } catch (Exception e) {
                    Toast.makeText(this, "至少需一個標籤", Toast.LENGTH_SHORT).show();
                }
                updateSelectedTags();
            });
            chipGroupSelectedTags.addView(chip);
        }
    }

    private void setAddTagChip() {
        Chip chip = new Chip(this);
        chip.setText("+");
        chip.setCheckable(false);
        chip.setClickable(true);

        chip.setOnClickListener(v -> {
            showAddTagDialog();
        });
        chipGroupAvailableTags.addView(chip);
    }

    private void showAddTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新增標籤");

        EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("新增", (dialog, which) -> {
            String tagName = input.getText().toString().trim();
            if (!tagName.isEmpty()) {
                user.addTag(tagName);
                user.selectTag(tagName);
                populateAvailableTags();
                updateSelectedTags();
            } else {
                Toast.makeText(this, "標籤名稱不可為空", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        builder.show();
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