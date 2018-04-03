package com.bzh.dytt.util;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.CategoryMap;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class HomePageParserTest {

    private HomePageParser mHomePageParser;

    @Before
    public void setUp() throws Exception {
        mHomePageParser = new HomePageParser();
    }

    @Test
    public void parseLatestMovieCategoryMap() throws IOException {
        String homeHtml = TestUtils.getResource(getClass(), "home.html");

        List<CategoryMap> result = mHomePageParser.parse(homeHtml);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("/html/gndy/dyzz/20180403/56620.html", result.get(0).getLink());
    }

}