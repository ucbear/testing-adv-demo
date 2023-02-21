package com.demo.testing.mockito;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class SpyTest {

    @Test
    public void testMethod1() {
        // Arrange (Given)
        List<String> list = new ArrayList<>();
        List<String> listSpy = spy(list);

        // Act (When)
        listSpy.add("first-element");
        System.out.println(listSpy.size());

        // Assert (Then)
        assertEquals("first-element", listSpy.get(0));
    }

    @Test
    public void testMethod2() {
        // Arrange (Given)
        List<String> list = new ArrayList<>();
        List<String> listSpy = spy(list);

        // Act (When)
        listSpy.add("first-element");

        // Assert (Then)
        assertEquals("first-element", listSpy.get(0));

        // Act (When) -- be careful!
        when(listSpy.get(0)).thenReturn("second-element");

        // Assert (Then)
        assertEquals("second-element", listSpy.get(0));
    }

    @Test
    public void testMethod3() {
        // Arrange (Given)
        List<String> list = new ArrayList<>();
        List<String> listSpy = spy(list);

        // Act (When) -- be careful!
//        when(listSpy.get(0)).thenReturn("second-element");
        doReturn("second-element").when(listSpy).get(0);
        // Assert (Then)
        assertEquals("second-element", listSpy.get(0));
    }

    @Test
    public void testMethod4() {
        // Arrange (Given)
        List<String> list = mock(List.class);

        // Act (When) -- be careful!
        when(list.get(0)).thenThrow(IllegalArgumentException.class);
        //...

        try {
            list.get(0);
        } catch (Exception ex) {

        }

        when(list.get(0)).thenReturn("second-element");
        doReturn("second-element").when(list).get(0);
        assertEquals("second-element", list.get(0));
        // Assert (Then)
    }
}
