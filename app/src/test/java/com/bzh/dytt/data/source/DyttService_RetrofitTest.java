package com.bzh.dytt.data.source;

import com.bzh.dytt.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

public class DyttService_RetrofitTest {

    private String mIndexHtmlMD5;
    private Retrofit mRetrofit;
    private MockRetrofit mMockRetrofit;
    private BehaviorDelegate<DyttService> mDelegate;
    private String mHomePage;

    @Before
    public void start() throws IOException, NoSuchAlgorithmException {

        mHomePage = TestUtils.getResource(getClass(), "index.html");

        mIndexHtmlMD5 = TestUtils.getMD5(mHomePage);

        mRetrofit = new Retrofit
                .Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://www.dytt8.net")
                .build();

        mMockRetrofit = new MockRetrofit
                .Builder(mRetrofit)
                .build();


        mDelegate = mMockRetrofit.create(DyttService.class);
    }

    @After
    public void shutdown() throws IOException {
    }

    @Test
    public void dytt_getHomePage() throws IOException, NoSuchAlgorithmException {

        DyttService service = mDelegate.returningResponse(mHomePage);

        Response<ResponseBody> response = service.getHomePage().execute();
    }
}
