package com.bzh.dytt.data.network;

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
    private HomePageParser.NewestParse mNewestParse;
    private HomePageParser.ThunderParse mThunderParse;

    @Before
    public void setUp() throws Exception {
        mHomePage = TestUtils.getResource(getClass(), "index.html");
        mHomePageParser = new HomePageParser();
        mNewestParse = new HomePageParser.NewestParse();
        mThunderParse = new HomePageParser.ThunderParse();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 测试 Area 是否可以解析成功
     */
    @Test
    public void parseHomePageArea() {
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
    }

    @Test
    public void parseNewestItems() {
        List<HomeItem> homeItems = mNewestParse.parseItems(mHomePage);
        assertNotNull(homeItems);
        assertEquals("<>推荐下载本站app,绿色小巧,简单实用不占资源,详细说明请点击进入！<>", homeItems.get(0).getTime());
    }

    @Test
    public void parseThunderItems() {
        List<HomeItem> homeItems = mThunderParse.parseItems(mHomePage);
        assertNotNull(homeItems);
    }
}