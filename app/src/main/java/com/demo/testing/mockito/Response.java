package com.demo.testing.mockito;

public interface Response {
    boolean isSuccessful();

    // returns string or null if failed
    String getData();
}