package com.example.expensemate.view;
import android.widget.Toast;

import androidx.room.Room;

import com.example.expensemate.R;
import com.example.expensemate.database.AppDatabase;
import com.example.expensemate.entity.RecordEntity;
import com.example.expensemate.entity.RecordMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddRecordActivity extends BaseRecordActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected void setupSpecificLogic() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        user.createRecord();

        // 預設為支出
        radioGroupType.check(R.id.radioButtonExpense);

        // 設定當前日期
        String currentDateString = dateFormat.format(calendar.getTime());
        editTextDate.setText(currentDateString);
        user.selectDate(calendar.getTime());

        findViewById(R.id.btnSave).setOnClickListener(v -> saveRecord());
    }

    private void saveRecord() {
        editTextName.clearFocus();
        editTextPrice.clearFocus();

        try{
            user.saveRecord();

            new Thread(() -> {
                recordsViewModel.insertRecord(user.getRecord());
                runOnUiThread(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
            }).start();
        }
        catch (Exception e){
            Toast.makeText(this, "請檢查資料，每項為必填", Toast.LENGTH_SHORT).show();
        }
    }
}
