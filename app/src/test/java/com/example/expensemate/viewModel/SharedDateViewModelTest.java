package com.example.expensemate.viewModel;

import android.util.Pair;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SharedDateViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SharedDateViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new SharedDateViewModel();
    }

    @Test
    public void testSetAndGetCurrentDate() {
        // Arrange
        int year = 2025;
        int month = 5;
        Observer<Pair<Integer, Integer>> observer = mock(Observer.class);
        viewModel.getCurrentDate().observeForever(observer);

        // Act
        viewModel.setCurrentDate(year, month);

        // Assert
        Pair<Integer, Integer> expected = new Pair<>(year, month);
        assertEquals(expected.first, viewModel.getCurrentDate().getValue().first);
        assertEquals(expected.second, viewModel.getCurrentDate().getValue().second);
    }
}
