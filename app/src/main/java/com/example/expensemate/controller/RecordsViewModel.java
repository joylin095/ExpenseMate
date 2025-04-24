package com.example.expensemate.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemate.model.DateRecord;
import com.example.expensemate.model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordsViewModel extends ViewModel {
    private final MutableLiveData<List<Object>> recordList = new MutableLiveData<>();
    private List<Record> records = new ArrayList<>();
    private User user = User.getInstance();

    public LiveData<List<Object>> getRecordList() {
        return recordList;
    }

    public void loadRecords(int year, int month) {
        records = getRecordsForMonth(year, month);
        List <Object> groupedRecords = groupRecordsByDate(records);
        recordList.setValue(groupedRecords);
    }

    // This method is used to group records by date
    private List<Object> groupRecordsByDate(List<Record> records) {
        Map<String, List<Record>> dateGroupedRecords = new HashMap<>();
        List<Object> groupedRecords = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Group records by date
        for (Record record : records) {
            String dateString = dateFormat.format(record.getDate());
            if (!dateGroupedRecords.containsKey(dateString)) {
                dateGroupedRecords.put(dateString, new ArrayList<>());
            }
            dateGroupedRecords.get(dateString).add(record);
        }

        List<String> sortedDates = new ArrayList<>(dateGroupedRecords.keySet());
        sortedDates.sort((date1, date2) -> {
            try {
                return dateFormat.parse(date2).compareTo(dateFormat.parse(date1));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });

        for (String date : sortedDates) {
            // Add the date header
            try{
                DateRecord dateRecord = new DateRecord(dateFormat.parse(date));
                groupedRecords.add(dateRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Add the records for that date
            groupedRecords.addAll(dateGroupedRecords.get(date));
        }

        return groupedRecords;
    }

    // This method is used to calculate the total income and expense
    public Map<String, Float> calculateTotals() {
        float incomeTotal = 0;
        float expenseTotal = 0;

        for (Record record : records) {
            if (record.getType().equals("Income")) {
                incomeTotal += record.getPrice();
            } else if (record.getType().equals("Expense")) {
                expenseTotal += record.getPrice();
            }
        }

        Map<String, Float> totals = new HashMap<>();
        totals.put("Income", incomeTotal);
        totals.put("Expense", expenseTotal);
        totals.put("Balance", incomeTotal - expenseTotal);

        return totals;
    }

    // This method is used to filter records by month
    public List<Record> getRecordsForMonth(int year, int month) {
        Map<String, Record> allRecordsMap = user.getRecordList();
        List<Record> allRecords = new ArrayList<>(allRecordsMap.values());

        List<Record> filteredRecords = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (Record record : allRecords) {
            calendar.setTime(record.getDate());
            int recordYear = calendar.get(Calendar.YEAR);
            int recordMonth = calendar.get(Calendar.MONTH);

            if (recordYear == year && recordMonth == month) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }
}
