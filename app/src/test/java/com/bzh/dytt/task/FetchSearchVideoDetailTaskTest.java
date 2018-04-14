package com.bzh.dytt.task;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.util.VideoDetailPageParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class FetchSearchVideoDetailTaskTest {

    @Mock
    DyttService dyttService;

    @Mock
    VideoDetailDAO videoDetailDAO;

    @Mock
    Call<ResponseBody> call;

    private VideoDetailPageParser parser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        parser = new VideoDetailPageParser();
    }

    @Test
    public void fetchSuccess() throws IOException {
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.setDetailLink("/html/gndy/jddy/20180302/56407.html");

        String detailHtml = TestUtils.getResource(getClass(), "query_result.html");
        VideoDetail parseVideoDetail = parser.parseVideoDetail(detailHtml);

        when(dyttService.getSearchVideoDetail("http://www.ygdy8.com" + videoDetail.getDetailLink())).thenReturn(call);
        when(dyttService.getSearchVideoDetail("http://www.ygdy8.com" + videoDetail.getDetailLink()).execute()).thenReturn(Response.success(ResponseBody.create(MediaType.parse("text/html"), detailHtml)));

        FetchSearchVideoDetailTask task = new FetchSearchVideoDetailTask(videoDetail, videoDetailDAO, dyttService, parser, null);
        task.run();

        verify(dyttService.getSearchVideoDetail("http://www.ygdy8.com" + videoDetail.getDetailLink())).execute();
        verify(videoDetailDAO).updateVideoDetail(parseVideoDetail.updateValue(videoDetail));
    }

    @Test
    public void fetchFailure() throws IOException {
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.setDetailLink("/html/gndy/jddy/20180302/56407.html");

        when(dyttService.getSearchVideoDetail("http://www.ygdy8.com" + videoDetail.getDetailLink())).thenReturn(call);
        when(dyttService.getSearchVideoDetail("http://www.ygdy8.com" + videoDetail.getDetailLink()).execute()).thenReturn(Response.<ResponseBody>error(500, ResponseBody.create(MediaType.parse("application/txt"), "Error Message")));

        MutableLiveData<Throwable> throwableMLD = new MutableLiveData<>();

        FetchSearchVideoDetailTask task = new FetchSearchVideoDetailTask(videoDetail, videoDetailDAO, dyttService, parser, throwableMLD);
        task.run();

        Observer<Throwable> observer = mock(Observer.class);
        throwableMLD.observeForever(observer);

        Exception errorMessage = new Exception("Error Message");
        throwableMLD.postValue(errorMessage);
    }
}