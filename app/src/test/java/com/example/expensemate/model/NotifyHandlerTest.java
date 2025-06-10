package com.example.expensemate.model;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotifyHandlerTest {
    MockLocationProvider mockLocationProvider;
    NotifyHandler notifyHandler;
    @Before
    public void setUp() {
        mockLocationProvider = new MockLocationProvider();
        notifyHandler = new NotifyHandler(mockLocationProvider, new Location());
    }

    @Test
    public void testSamePlace_shouldNotify() {
        notifyHandler.checkLocationAndNotify(Assert::assertFalse);
        notifyHandler.checkLocationAndNotify(Assert::assertTrue);
    }

    @Test
    public void testDifferentPlace_shouldNotNotify() {
        notifyHandler.checkLocationAndNotify(Assert::assertFalse);
        mockLocationProvider.changeToSecond();
        notifyHandler.checkLocationAndNotify(Assert::assertFalse);
    }
}
