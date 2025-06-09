package com.example.expensemate.Factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemate.MyApplication;
import com.example.expensemate.repository.RecordRepository;
import com.example.expensemate.repository.TagRepository;
import com.example.expensemate.viewModel.RecordsViewModel;
import com.example.expensemate.viewModel.TagsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Context context = null;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecordsViewModel.class)) {
            return (T) new RecordsViewModel(new RecordRepository(context), MyApplication.getInstance().getUser());
        }
        else if (modelClass.isAssignableFrom(TagsViewModel.class)) {
            return (T) new TagsViewModel(new TagRepository(context));
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
