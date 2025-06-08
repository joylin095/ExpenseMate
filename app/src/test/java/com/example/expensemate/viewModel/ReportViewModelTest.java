package com.example.expensemate.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.expensemate.model.ChartData;
import com.example.expensemate.model.ChartFilter;
import com.example.expensemate.model.ChartType;
import com.example.expensemate.model.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ReportViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private User mockUser;

    @Mock
    private Observer<Map<String, Float>> tagCombinationObserver;

    @Mock
    private Observer<Map<String, Float>> pieChartObserver;

    private ReportViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject mockUser directly into the ViewModel
        viewModel = new ReportViewModel(mockUser);
        viewModel.getTagCombinationLiveData().observeForever(tagCombinationObserver);
        viewModel.getPieChartLiveData().observeForever(pieChartObserver);
    }

    @Test
    public void testUpdateSelectedTags_updatesTagCombinationLiveData() {
        // Arrange
        int year = 2025;
        int month = 5;
        Set<String> selectedTags = new HashSet<>();
        selectedTags.add("Food");
        selectedTags.add("Transport");

        Map<String, Float> mockTagCombinationSums = new HashMap<>();
        mockTagCombinationSums.put("Food", 100f);
        mockTagCombinationSums.put("Transport", 50f);

        Map<String, Float> mockOtherTagSums = new HashMap<>();
        mockOtherTagSums.put("Food", 100f);
        ChartData mockChartData = new ChartData(mockOtherTagSums);

        when(mockUser.getTagCombinationSums(year, month, new ArrayList<>(selectedTags)))
                .thenReturn(mockTagCombinationSums);
        when(mockUser.getChartData(any(ChartFilter.class))).thenReturn(mockChartData);

        // Act
        viewModel.updateSelectedTags(selectedTags, year, month);

        // Assert
        verify(tagCombinationObserver).onChanged(mockTagCombinationSums);
    }
}
