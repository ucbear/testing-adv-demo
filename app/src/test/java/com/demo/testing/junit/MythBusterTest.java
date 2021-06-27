package com.demo.testing.junit;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MythBusterTest {

    private List<String> xs;

    @Before
    public void init() {
        xs = new ArrayList<>();
    }

    @Test
    public void test_add() {
        // Arrange (Given)

        // Act (When)
        xs.add("Mellow");

        // Assert (Then)
        assertEquals(1, xs.size());
    }

    @Test
    public void test_remove() {
        // Arrange (Given)
        xs.add("Yellow");
        xs.add("Hello");

        // Act (When)
        xs.remove("Yellow");

        // Assert (Then)
        assertEquals(1, xs.size());
    }

}
