package com.bzh.dytt.task;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.util.VideoDetailPageParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class FetchVideoDetailTaskTest {

    private VideoDetailDAO videoDetailDAO;
    private DyttService dyttService;
    private VideoDetailPageParser parse;
    private Call<ResponseBody> call;

    @Before
    public void init() {
        videoDetailDAO = mock(VideoDetailDAO.class);
        dyttService = mock(DyttService.class);
        parse = new VideoDetailPageParser();
        call = mock(Call.class);
    }

    @Test
    public void fetchTaskS() throws IOException {
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.setDetailLink("/html/gndy/dyzz/20180328/56582.html");

        String detailHtml = TestUtils.getResource(getClass(), "movie_detail.html");
        VideoDetail parseVideoDetail = parse.parseVideoDetail(detailHtml);
        when(dyttService.getVideoDetail(videoDetail.getDetailLink())).thenReturn(call);
        when(dyttService.getVideoDetail(videoDetail.getDetailLink()).execute()).thenReturn(Response.success(ResponseBody.create(MediaType.parse("text/html"), detailHtml)));

        FetchVideoDetailTask task = new FetchVideoDetailTask(videoDetail, videoDetailDAO, dyttService, parse, null);
        task.run();

        verify(dyttService.getVideoDetail(videoDetail.getDetailLink())).execute();
        verify(videoDetailDAO).updateVideoDetail(parseVideoDetail.updateValue(videoDetail));
    }
}