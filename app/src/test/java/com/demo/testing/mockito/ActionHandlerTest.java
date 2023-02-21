package com.demo.testing.mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ActionHandlerTest {

    // SUT
    ActionHandler actionHandler;

    @Mock
    Service service;

    @Mock
    Response response;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        actionHandler = new ActionHandler(service);
    }

    @Test
    public void getValue_should_return_valid_string_if_doRequest_succeed() {
        // Given
        when(response.isSuccessful()).thenReturn(true);
        when(response.getData()).thenReturn("SUCCESS");

        doAnswer(invocation -> {
             Callback callback = invocation.getArgument(1);
             callback.reply(response);
             return null;
        }).when(service).getResponse(anyString(), isA(Callback.class));

        // When
        actionHandler.doRequest("some query");

        // Then
        String value = actionHandler.getValue();
        assertThat(value, is("SUCCESS"));
    }

    // ArgumentCaptor with doNothing or verify ...

    @Captor
    ArgumentCaptor<Callback> callbackCaptor;

    @Test
    public void getValue_should_return_null_if_doRequest_failed() {
        // Given
        when(response.isSuccessful()).thenReturn(false);

        doNothing().when(service).getResponse(anyString(), callbackCaptor.capture());

        // When
        actionHandler.doRequest("some query");

        // Then
        callbackCaptor.getValue().reply(response);
        String value = actionHandler.getValue();
        assertThat(value, is(nullValue()));
    }

    @Test
    public void getValue_should_return_null_if_doRequest_failed2() {
        // Given
        when(response.isSuccessful()).thenReturn(false);

        // When
        actionHandler.doRequest("some query");

        // Then
        verify(service).getResponse(anyString(), callbackCaptor.capture());
        callbackCaptor.getValue().reply(response);
        String value = actionHandler.getValue();
        assertThat(value, is(nullValue()));
    }
}