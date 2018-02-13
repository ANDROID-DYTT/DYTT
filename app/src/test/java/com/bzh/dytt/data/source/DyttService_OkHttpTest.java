package com.bzh.dytt.data.source;

import com.bzh.dytt.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static junit.framework.Assert.assertEquals;

public class DyttService_OkHttpTest {

    private MockWebServer mMockWebServer;

    private String mIndexHtmlMD5;

    @Before
    public void startMockServer() throws IOException, NoSuchAlgorithmException {

        final String homePage = TestUtils.getResource(getClass(), "index.html");

        mIndexHtmlMD5 = TestUtils.getMD5(homePage);

        mMockWebServer = new MockWebServer();
        mMockWebServer.setDispatcher(new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                if (request.getPath().equals("/")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(homePage);
                }
                return new MockResponse().setResponseCode(404);
            }
        });
        mMockWebServer.start();
    }

    @After
    public void shutdownMockServer() throws IOException {
        mMockWebServer.shutdown();
    }

    @Test
    public void dytt_getHomePage() throws IOException, InterruptedException, NoSuchAlgorithmException {

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(new Request.Builder()
                .url(mMockWebServer.url("/"))
                .build()).execute();

        assertEquals(mIndexHtmlMD5, TestUtils.getMD5(response.body().string()));
        RecordedRequest request = mMockWebServer.takeRequest();
        assertEquals("/", request.getPath());
    }

}
