package com.demo.testing.calculator;

//import static com.google.common.truth.Truth.assertThat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import androidx.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

public class PriceCalculatorTest {

    private static final User ANOTHER_VIP_USER = ;
    // SUT
    Item TV = new Item(1, 1000, 1, 10.0);
    Item OVEN = new Item(2, 100, 1, 5.0);
    private static final Store UNUSED_STORE = null;
    private static final User GUEST = null;
    private static final int GUEST_ID = -1;

    PriceCalculator priceCalculator;
    MockMetricFactory metricFactory;
    MockTimer timer;
    MockTimerContext timerContext;
    private User returnedUser;
    private Store store = getStore();

    @Before
    public void init() {
        metricFactory = new MockMetricFactory();
        timer = new MockTimer();
        timerContext = new MockTimerContext();

        priceCalculator = new MyPriceCalculator(metricFactory);
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_if_user_is_not_member_of_store() {
        // Arrange (Given)
        returnedUser = GUEST;

        // Act (When)
        priceCalculator.calculateTotalPrice(GUEST_ID, UNUSED_STORE);

        // Assert (Then)
    }

    @Test
    public void should_return_total_with_regular_discount_applied() {
        // Arrange (Given)

        User user = getUser();
        user.addItemCode(TV.getSkuCode());
        user.addItemCode(OVEN.getSkuCode());

        returnedUser = user;

        // Act (When)
        double total = priceCalculator.calculateTotalPrice(1, null);

        // Assert (Then)
        assertThat(total, is(closeTo(995.0, 0.1)));
        assertThat(timerContext.isStopped, is(true));
    }

    @NonNull
    private User getUser() {
        return new User(1);
    }

    private Store getStore() {
        Store store = new Store();
        store.addItem(TV);
        store.addItem(OVEN);
        store.setVipDiscountPercentage(10.0);
        store.addVip(ANOTHER_VIP_USER);
        return store;
    }

    @Test
    public void should_return_total_with_both_regular_and_vip_discounts_applied() {
        User user = getUser();
        store.addVip(user);

        returnedUser = user;

        // Act (When)
        double total = priceCalculator.calculateTotalPrice(user.getUserId(), store);

        // Assert (Then)
        assertThat(total, is(closeTo(895.5, 0.1)));
        assertThat(timerContext.isStopped, is(true));
    }

    private class MockMetricFactory extends MetricFactory {
        @Override
        public Timer createTimer(String name, String method) {
            return timer;
        }
    }

    private  class MockTimer extends Timer {
        @Override
        public TimerContext time() {
            return timerContext;
        }
    }

    private class MockTimerContext extends TimerContext {
        boolean isStopped = false;

        @Override
        public void stop() {
            super.stop();
        }
    }

    private class MyPriceCalculator extends PriceCalculator {
        public MyPriceCalculator(MetricFactory factory) {
            super(factory);
        }

        @NonNull
        @Override
        public User getUser() {
            return returnedUser;
        }
    }
}