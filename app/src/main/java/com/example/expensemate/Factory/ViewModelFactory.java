package com.example.expensemate.Factory;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemate.repository.RecordRepository;
import com.example.expensemate.viewModel.RecordsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecordsViewModel.class)) {
            return (T) new RecordsViewModel(new RecordRepository(context));
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
