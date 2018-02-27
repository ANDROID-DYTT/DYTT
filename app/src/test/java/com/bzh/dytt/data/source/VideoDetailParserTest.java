package com.bzh.dytt.data.source;


import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.util.VideoDetailPageParser;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class VideoDetailParserTest {

    private String mMovieDetail;
    private VideoDetailPageParser mVideoDetailPageParser;

    @Before
    public void setup() throws Exception {
        mMovieDetail = TestUtils.getResource(getClass(), "movie_detail.html", "GB2312");
        mVideoDetailPageParser = new VideoDetailPageParser();
    }

    @Test
    public void parseMovieDetail() {
        VideoDetail videoDetail = mVideoDetailPageParser.parseVideoDetail(mMovieDetail);

        assertNotNull(videoDetail);
        assertEquals("机器之血", videoDetail.getName());

    }
}
