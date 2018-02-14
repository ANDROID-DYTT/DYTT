package com.bzh.dytt.data.source;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.HomeItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class HomePageParseUtilTest {

    private String mHomePage;
    private IParse<List<HomeItem>> mNewest168;
    private IParse<List<HomeItem>> mNewestParse;

    @Before
    public void setUp() throws Exception {
        mHomePage = TestUtils.getResource(getClass(), "index.html");
        mNewest168 = new HomePageParseUtil.Newest168Parse();
        mNewestParse = new HomePageParseUtil.NewestParse();
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseNewest168() {
        List<HomeItem> result = mNewest168.parse(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("IMDB评分8分以上影片200部", result.get(0).getTitle());
        assertEquals("/html/gndy/jddy/20160320/50523.html", result.get(0).getDetailLink());

        assertEquals("2017年传记剧情《马歇尔》BD", result.get(result.size() - 1).getTitle());
        assertEquals("/html/gndy/jddy/20180103/55966.html", result.get(result.size() - 1).getDetailLink());
    }

    @Test
    public void parseNewest() {
        List<HomeItem> result = mNewestParse.parse(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2017年高分获奖剧情《每分钟120击》BD法语中字", result.get(0).getTitle());
        assertEquals("/html/gndy/dyzz/20180212/56315.html", result.get(0).getDetailLink());
        assertEquals("2018-02-12", result.get(0).getTime());
    }

}