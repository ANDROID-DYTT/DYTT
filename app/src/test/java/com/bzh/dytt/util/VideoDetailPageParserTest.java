package com.bzh.dytt.util;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.VideoDetail;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VideoDetailPageParserTest {

    private VideoDetailPageParser videoDetailPageParser;

    @Before
    public void setUp() throws Exception {
        videoDetailPageParser = new VideoDetailPageParser();
    }

    @Test
    public void parseVideoDetail() throws IOException {
        VideoDetail result = videoDetailPageParser.parseVideoDetail(TestUtils.getResource(getClass(), "movie_detail.html"));
        assertNotNull(result);
        assertEquals("http://www.imageto.org/images/kj9Ny.jpg", result.getCoverUrl());
    }
}