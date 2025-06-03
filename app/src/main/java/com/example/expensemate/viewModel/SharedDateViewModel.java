package com.example.expensemate.viewModel;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedDateViewModel extends ViewModel {
    private final MutableLiveData<Pair<Integer, Integer>> currentDate = new MutableLiveData<>();

    public LiveData<Pair<Integer, Integer>> getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(int year, int month) {
        currentDate.setValue(new Pair<>(year, month));
    }
}
