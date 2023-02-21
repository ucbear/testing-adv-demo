package com.demo.testing.calculator;

//import static com.google.common.truth.Truth.assertThat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PriceCalculatorTest {
    private static final User GUEST = null;
    private static final Store UNUSED_STORE = null;
    private static final User VIP_USER = new User(100);

    Item TV = new Item(1, 200.0, 1, 10d);
    Item OVEN = new Item(2, 100.0, 1, 5d);

    // SUT
    PriceCalculator priceCalculator;

    Store store;

    @Mock
    MetricFactory mockMetricFactory;
    @Mock
    Timer mockTimer;
    @Mock
    TimerContext mockTimerContext;

    private User returnedUser;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        when(mockMetricFactory.createTimer(anyString(), anyString()))
                .thenReturn(mockTimer);
        when(mockTimer.time()).thenReturn(mockTimerContext);

        priceCalculator = new MyPriceCalculator(mockMetricFactory);
    }


    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_if_user_is_not_a_member() {
        // Arrange (Given)
        returnedUser = GUEST;

        // Act (When)
        // Assert (Then)
        priceCalculator.calculateTotalPrice(-1, UNUSED_STORE);
    }

    @Test
    public void should_return_total_with_app_discount_applied() {
        // Arrange (Given)
        User user = createUser(1);
        store = createStore();

        returnedUser = user;

        // Act (When)
        double total = priceCalculator.calculateTotalPrice(user.getUserId(), store);

        // Assert (Then)
        assertEquals(275.0, total, 0.1);
        assertThat(total, is(closeTo(275, 0.1d)));
        Truth.assertThat(total).isWithin(0.1).of(275);

        verify(mockTimerContext).stop();
    }

    @Test
    public void should_return_total_with_app_discount_and_vip_discount_applied() {
        // Arrange (Given)
        User user = createUser(1);
        store = createStore();
        store.addVip(user);

        returnedUser = user;

        // Act (When)
        double total = priceCalculator.calculateTotalPrice(user.getUserId(), store);

        // Assert (Then)
        Truth.assertThat(total).isWithin(0.1).of(247.5);

        verify(mockTimerContext).stop();
    }

    private Store createStore() {
        store = new Store();
        store.addItem(TV);
        store.addItem(OVEN);
        store.addVip(VIP_USER);
        store.setVipDiscountPercentage(10.0);
        return store;
    }

    @NonNull
    private User createUser(int id) {
        User user = new  User(id);
        user.addItemCode(TV.getSkuCode());
        user.addItemCode(OVEN.getSkuCode());
        return user;
    }

    private class MyPriceCalculator extends PriceCalculator {

        public MyPriceCalculator(MetricFactory factory) {
            super(factory);
        }

        @NonNull
//        @Override
        protected User getUser(int userId) {
            return returnedUser;
        }
    }
}