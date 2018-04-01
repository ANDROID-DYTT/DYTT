package com.bzh.dytt.data.network;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(JUnit4.class)
public class ApiResponseTest {

    @Test
    public void exception() {
        Exception exception = new Exception("Exception Message");
        ApiResponse apiResponse = new ApiResponse(exception);
        assertThat(apiResponse.isSuccessful(), is(false));
        assertThat(apiResponse.code, is(500));
        assertThat(apiResponse.body, nullValue());
        assertThat(apiResponse.errorMessage, is("Exception Message"));
    }

    @Test
    public void success() {
        ApiResponse<String> apiResponse = new ApiResponse<String>(Response.success("Body"));
        assertThat(apiResponse.code, is(200));
        assertThat(apiResponse.isSuccessful(), is(true));
        assertThat(apiResponse.errorMessage, nullValue());
        assertThat(apiResponse.body, is("Body"));
    }

    @Test
    public void error() {
        ApiResponse apiResponse = new ApiResponse<>(Response.error(400, ResponseBody.create(MediaType.parse("application/txt"), "Error Message")));
        assertThat(apiResponse.code, is(400));
        assertThat(apiResponse.errorMessage, is("Error Message"));
    }
}