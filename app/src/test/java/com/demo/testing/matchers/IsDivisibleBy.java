package com.demo.testing.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsDivisibleBy extends TypeSafeMatcher<Integer> {
    private int divisor;

    public IsDivisibleBy(int divisor) {
        this.divisor = divisor;
    }

    @Override
    protected boolean matchesSafely(Integer value) {
        return value % divisor == 0;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("divisible by " + divisor);
    }

    public static Matcher<Integer> divisibleBy(Integer divisor) {
        return new IsDivisibleBy(divisor);
    }
}