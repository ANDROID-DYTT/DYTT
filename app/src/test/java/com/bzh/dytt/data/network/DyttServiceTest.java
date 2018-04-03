package com.bzh.dytt.data.network;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.util.LiveDataCallAdapterFactory;
import com.bzh.dytt.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class DyttServiceTest {

    public static final String BASE_URL = "";
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private DyttService service;

    private MockWebServer mockWebServer;

    @Before
    public void createService() {
        mockWebServer = new MockWebServer();
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url(BASE_URL))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(DyttService.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getHomePage() throws IOException, InterruptedException {
        enqueueResponse("home.html");
        ApiResponse<ResponseBody> apiResponse = LiveDataTestUtil.getValue(service.getMovieListByCategory("/"));
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(URLDecoder.decode(request.getPath(), "UTF-8"), is("//"));
        assertThat(apiResponse.isSuccessful(), is(true));
        assertThat(apiResponse.errorMessage, nullValue());
        assertThat(apiResponse.body.contentLength(), is(not(0l)));
    }

    @Test
    public void getMovieListByCategory() throws IOException, InterruptedException {
        enqueueResponse("new_movie.html");
        ApiResponse<ResponseBody> apiResponse = LiveDataTestUtil.getValue(
                service.getMovieListByCategory(MovieCategory.NEW_MOVIE.getDefaultUrl()));
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(URLDecoder.decode(request.getPath(), "UTF-8"), is("//html/gndy/dyzz/list_23_1.html"));
        assertThat(apiResponse.isSuccessful(), is(true));
        assertThat(apiResponse.errorMessage, nullValue());
        assertThat(apiResponse.body.contentLength(), is(not(0l)));
    }

    @Test
    public void getVideoDetail() throws IOException, InterruptedException {
        enqueueResponse("movie_detail.html");
        Response<ResponseBody> response = service.getVideoDetail("/html/gndy/dyzz/20180330/56601.html").execute();
        ApiResponse<ResponseBody> apiResponse = new ApiResponse<>(response);
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(URLDecoder.decode(request.getPath(), "UTF-8"), is("//html/gndy/dyzz/20180330/56601.html"));
        assertThat(apiResponse.isSuccessful(), is(true));
        assertThat(apiResponse.body.contentLength(), is(not(0l)));
    }


    private void enqueueResponse(String fileName) throws IOException {
        enqueueResponse(fileName, Collections.<String, String>emptyMap());
    }

    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)));
    }

}
