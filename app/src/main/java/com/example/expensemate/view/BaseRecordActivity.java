package com.example.expensemate.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemate.R;
import com.example.expensemate.model.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

public abstract class BaseRecordActivity extends AppCompatActivity {
    protected EditText editTextName, editTextPrice, editTextDate;
    protected RadioGroup radioGroupType;
    protected ChipGroup chipGroupSelectedTags, chipGroupAvailableTags;
    protected User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        initViews();
        setupSpecificLogic();
    }

    /**
     * 設定LayoutID
     */
    protected abstract int getLayoutResourceId();

    /**
     * 設定特定邏輯
     */
    protected abstract void setupSpecificLogic();

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        radioGroupType = findViewById(R.id.radioGroupType);
        editTextDate = findViewById(R.id.editTextDate);
        chipGroupSelectedTags = findViewById(R.id.chipGroupSelectedTags);
        chipGroupAvailableTags = findViewById(R.id.chipGroupAvailableTags);

        initEditTextName();
        initEditTextPrice();
        initEditRadioGroup();
        initEditTextDate();
        populateAvailableTags();
    }

    private void initEditTextName() {
        editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    String name = editTextName.getText().toString().trim();
                    user.enterName(name);
                    editTextName.setText(user.getRecordName());
                } catch (Exception e) {
                    editTextName.setError("名稱不可為空");
                }
            }
        });
    }

    private void initEditTextPrice() {
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
            } else {
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
                    Date parseDate = dateFormat.parse(selectedDate);
                    if (parseDate == null) {
                        throw new Exception("日期格式錯誤");
                    }
                    calendar.setTime(parseDate);
                    user.selectDate(calendar.getTime());
                } catch (Exception e) {
                    Toast.makeText(this, "日期格式錯誤", Toast.LENGTH_SHORT).show();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    /**
     * 顯示可用標籤
     */
    protected void populateAvailableTags() {
        chipGroupAvailableTags.removeAllViews();

        setAddTagChip();
        for (String tagName : user.getAvailableTags()) {
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

    /**
     * 顯示已選擇標籤
     */
    protected void updateSelectedTags() {
        chipGroupSelectedTags.removeAllViews();

        for (String tagName : user.getSelectedTags()) {
            Chip chip = new Chip(this);
            chip.setText(tagName);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                try {
                    user.unselectTag(tagName);
                } catch (Exception e) {
                    Toast.makeText(this, "至少需一個標籤", Toast.LENGTH_SHORT).show();
                }
                updateSelectedTags();
            });
            chipGroupSelectedTags.addView(chip);
        }
    }

    /**
     * 新增標籤標籤chip
     */
    private void setAddTagChip() {
        Chip chip = new Chip(this);
        chip.setText("+");
        chip.setCheckable(false);
        chip.setClickable(true);

        chip.setOnClickListener(v -> showAddTagDialog());
        chipGroupAvailableTags.addView(chip);
    }

    /**
     * 顯示新增標籤對話框
     */
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
}