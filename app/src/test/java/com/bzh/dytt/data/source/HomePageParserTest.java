package com.bzh.dytt.data.source;

import android.util.Log;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.util.HomePageParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class HomePageParserTest {

    private String mHomePage;

    private HomePageParser mHomePageParser;

    @Before
    public void setUp() throws Exception {
        mHomePage = TestUtils.getResource(getClass(), "index.html");
        mHomePageParser = new HomePageParser();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 测试 Area 是否可以解析成功
     */
    @Test
    public void parseHomePageArea(){
        List<HomeArea> result = mHomePageParser.parseAreas(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);

        assertEquals("最新发布168部影视", result.get(0).getTitle());
        assertEquals("/html/gndy/dyzz/index.html", result.get(1).getDetailLink());
        assertEquals(300, result.get(2).getType());
    }

    /**
     * 测试 Item 是否可以解析成功
     */
    @Test
    public void parseHomePageItem() {
        List<HomeItem> result = mHomePageParser.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);

        assertEquals("IMDB评分8分以上影片200部", result.get(0).getTitle());
        assertEquals("/html/gndy/jddy/20180213/56318.html", result.get(1).getDetailLink());
        assertEquals(600, result.get(235).getType());
    }

}