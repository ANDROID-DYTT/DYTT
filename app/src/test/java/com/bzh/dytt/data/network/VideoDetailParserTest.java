package com.bzh.dytt.data.network;


import android.text.TextUtils;

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

    @Test
    public void parseGrade(){
        String grade = getGrade("6.5/10from10,748users");
        assertEquals("6.5", grade);

        String users = getGradeUsers("6.5/10from10,748users");
        assertEquals("10,748", users);
    }

    private String getGrade(String str){
        String result = "0";
        if(TextUtils.isEmpty(str)) {
            return result;
        }
        try {
            result= str.split("/")[0];
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    private String getGradeUsers(String str){
        String result = "0";

        if(TextUtils.isEmpty(str)) {
            return "0";
        }
        try {
            result = str.split("from")[1].split("users")[0];

        } catch (Exception e) {
            return result;
        }
        return result;
    }
}
