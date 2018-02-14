package com.bzh.dytt.data.source;

import com.bzh.dytt.TestUtils;

import org.junit.After;
import org.junit.Before;

public class HomePageParseUtilTest {

    private String mHomePage;

    @Before
    public void setUp() throws Exception {
        mHomePage = TestUtils.getResource(getClass(), "index.html");
    }



    @After
    public void tearDown() throws Exception {
    }

}