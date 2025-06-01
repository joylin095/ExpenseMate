package com.example.expensemate.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.expensemate.repository.RecordRepository;
import com.example.expensemate.repository.TagRepository;

import java.util.List;

public class TagsViewModel extends ViewModel {
    private TagRepository tagRepository;

    public TagsViewModel(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void insertTag(String tagName) {
        tagRepository.insertTag(tagName);
    }

    public List<String> getTagsFromDB(){
        return tagRepository.getAllTags();
    }
}
