package com.example.expensemate.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.expensemate.model.Record;
import com.example.expensemate.model.User;
import com.example.expensemate.repository.RecordRepository;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecordsViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private RecordRepository mockRecordRepository;
    @Mock
    private Observer<List<Object>> mockObserver;
    private RecordsViewModel viewModel;
    private static Calendar calendar;
    private User user;

    @BeforeClass
    public static void setUpBeforeClass() {
        calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 8);
    }

    @Before
    public void setUp() {
        user = new User();
        MockitoAnnotations.openMocks(this);
        viewModel = new RecordsViewModel(mockRecordRepository, user);
        viewModel.getRecordList().observeForever(mockObserver);
    }

    @Test
    public void testLoadRecords() {

        Record record1 = new Record();
        record1.setDate(calendar.getTime());
        Record record2 = new Record();
        record2.setDate(calendar.getTime());
        List<Record> mockRecords = Arrays.asList(record1, record2);

        when(mockRecordRepository.getAllRecords()).thenReturn(mockRecords);

        viewModel.loadRecords(2025, Calendar.JUNE);

        verify(mockObserver).onChanged(anyList());
    }

    @Test
    public void testCalculateTotals() {
        user.addTag("food");
        user.createRecord();
        user.enterName("測試一");
        user.selectDate(calendar.getTime());
        user.selectTag("food");
        user.enterPrice(1000);
        user.selectType("Income");
        user.saveRecord();
        user.createRecord();
        user.enterName("測試二");
        user.selectDate(calendar.getTime());
        user.selectTag("food");
        user.enterPrice(200);
        user.selectType("Expense");
        user.saveRecord();

        viewModel.loadRecords(2025, Calendar.JUNE);

        Map<String, Float> totals = viewModel.calculateTotals();

        assertEquals(1000, totals.get("Income"), 0.01);
        assertEquals(200, totals.get("Expense"), 0.01);
        assertEquals(800, totals.get("Balance"), 0.01);
    }
}
