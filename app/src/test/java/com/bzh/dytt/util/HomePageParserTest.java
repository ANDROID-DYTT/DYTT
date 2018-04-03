package com.bzh.dytt.util;

import com.bzh.dytt.data.CategoryMap;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;

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
        String homeHtml = getResource("home.html");

        List<CategoryMap> result = mHomePageParser.parseLatestMovieCategoryMap(homeHtml);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("/html/gndy/dyzz/20180403/56620.html", result.get(0).getLink());
    }

    private String getResource(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        return source.readString(StandardCharsets.UTF_8);
    }
}