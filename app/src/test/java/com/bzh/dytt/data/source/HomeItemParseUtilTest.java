package com.bzh.dytt.data.source;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class HomeItemParseUtilTest {

    private String mHomePage;
    private IParse<List<HomeArea>, List<HomeItem>> mNewest168Parse;
    private IParse<List<HomeArea>, List<HomeItem>> mNewestParse;
    private IParse<List<HomeArea>, List<HomeItem>> mThunderParse;
    private IParse<List<HomeArea>, List<HomeItem>> mChinaTvParse;
    private IParse<List<HomeArea>, List<HomeItem>> mJSKParse;
    private IParse<List<HomeArea>, List<HomeItem>> mEAParse;


    private IParse<List<HomeArea>, List<HomeItem>> mHomePageParse;

    @Before
    public void setUp() throws Exception {
        mHomePage = TestUtils.getResource(getClass(), "index.html");
        mHomePageParse = HomeItemParseUtil.getInstance();
        mNewest168Parse = new HomeItemParseUtil.Newest168Parse();
        mNewestParse = new HomeItemParseUtil.NewestParse();
        mThunderParse = new HomeItemParseUtil.ThunderParse();
        mChinaTvParse = new HomeItemParseUtil.ChinaTVParse();
        mJSKParse = new HomeItemParseUtil.JSKTVParse();
        mEAParse = new HomeItemParseUtil.EATVParse();
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseHomePageItem() {
        List<HomeItem> result = mHomePageParse.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2017主打美剧《犯罪心理 第十三季》更新第14集[中英双字", result.get(result.size() - 1).getTitle());
        assertEquals("/html/tv/oumeitv/20170929/55148.html", result.get(result.size() - 1).getDetailLink());
        assertEquals("2018-02-02", result.get(result.size() - 1).getTime());
    }


    @Test
    public void parseNewest168Item() {
        List<HomeItem> result = mNewest168Parse.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("IMDB评分8分以上影片200部", result.get(0).getTitle());
        assertEquals("/html/gndy/jddy/20160320/50523.html", result.get(0).getDetailLink());

        assertEquals("2017年传记剧情《马歇尔》BD", result.get(result.size() - 1).getTitle());
        assertEquals("/html/gndy/jddy/20180103/55966.html", result.get(result.size() - 1).getDetailLink());
    }


    @Test
    public void parseNewest168Area() {
        List<HomeArea> result = mNewest168Parse.parseAreas(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() == 1);
        assertEquals("最新发布168部影视", result.get(0).getTitle());
        assertEquals("", result.get(0).getDetailLink());
    }

    @Test
    public void parseNewestItem() {
        List<HomeItem> result = mNewestParse.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2017年高分获奖剧情《每分钟120击》BD法语中字", result.get(0).getTitle());
        assertEquals("/html/gndy/dyzz/20180212/56315.html", result.get(0).getDetailLink());
        assertEquals("2018-02-12", result.get(0).getTime());
    }

    @Test
    public void parseNewestArea() {
        List<HomeArea> result = mNewestParse.parseAreas(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() == 1);
        assertEquals("2018新片精品", result.get(0).getTitle());
        assertEquals("/html/gndy/dyzz/index.html", result.get(0).getDetailLink());
    }


    @Test
    public void parseThunderItem() {
        List<HomeItem> result = mThunderParse.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2017年科幻动作《阴影效应》BD中英双字幕", result.get(0).getTitle());
        assertEquals("/html/gndy/jddy/20180213/56318.html", result.get(0).getDetailLink());
        assertEquals("2018-02-13", result.get(0).getTime());
    }

    @Test
    public void parseThunderArea() {
        List<HomeArea> result = mThunderParse.parseAreas(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() == 1);
        assertEquals("迅雷电影资源", result.get(0).getTitle());
        assertEquals("/html/gndy/index.html", result.get(0).getDetailLink());
    }


    @Test
    public void parseChinaTVItem() {
        List<HomeItem> result = mChinaTvParse.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2018年香港电视剧《无间道2018》粤15集[国语字幕]", result.get(0).getTitle());
        assertEquals("/html/tv/hytv/20180127/56207.html", result.get(0).getDetailLink());
        assertEquals("2018-02-12", result.get(0).getTime());
    }


    @Test
    public void parseChinaTVArea() {
        List<HomeArea> result = mChinaTvParse.parseAreas(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() == 1);
        assertEquals("华语电视剧", result.get(0).getTitle());
        assertEquals("/html/tv/hytv/", result.get(0).getDetailLink());
    }

    @Test
    public void parseJSKTVItem() {
        List<HomeItem> result = mJSKParse.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2018韩国JTBC金土剧《Misty》第04集[韩语中字]", result.get(0).getTitle());
        assertEquals("/html/tv/rihantv/20180204/56246.html", result.get(0).getDetailLink());
        assertEquals("2018-02-13", result.get(0).getTime());
    }

    @Test
    public void parseJSKTVArea() {
        List<HomeArea> result = mJSKParse.parseAreas(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("日韩电视剧", result.get(0).getTitle());
        assertEquals("http://www.dytt8.net/html/tv/rihantv/index.html", result.get(0).getDetailLink());
    }

    @Test
    public void parseEATVItem() {
        List<HomeItem> result = mEAParse.parseItems(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2018主打美剧《国土安全/反恐危机 第七季》更新第01集[", result.get(0).getTitle());
        assertEquals("/html/tv/oumeitv/20180213/56316.html", result.get(0).getDetailLink());
        assertEquals("2018-02-13", result.get(0).getTime());
    }


    @Test
    public void parseEATVArea() {
        List<HomeArea> result = mEAParse.parseAreas(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("欧美电视剧", result.get(0).getTitle());
        assertEquals("/html/tv/oumeitv/index.html", result.get(0).getDetailLink());
    }


}