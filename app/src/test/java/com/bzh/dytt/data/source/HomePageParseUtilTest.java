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
    private IParse<List<HomeItem>> mNewest168Parse;
    private IParse<List<HomeItem>> mNewestParse;
    private IParse<List<HomeItem>> mThunderParse;
    private IParse<List<HomeItem>> mChinaTvParse;
    private IParse<List<HomeItem>> mJSKParse;
    private IParse<List<HomeItem>> mEAParse;


    private IParse<List<HomeItem>> mHomePageParse;

    @Before
    public void setUp() throws Exception {
        mHomePage = TestUtils.getResource(getClass(), "index.html");
        mHomePageParse = HomePageParseUtil.getInstance();
        mNewest168Parse = new HomePageParseUtil.Newest168Parse();
        mNewestParse = new HomePageParseUtil.NewestParse();
        mThunderParse = new HomePageParseUtil.ThunderParse();
        mChinaTvParse = new HomePageParseUtil.ChinaTVParse();
        mJSKParse = new HomePageParseUtil.JSKTVParse();
        mEAParse = new HomePageParseUtil.EATVParse();
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseHomePage() {
        List<HomeItem> result = mHomePageParse.parse(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2017主打美剧《犯罪心理 第十三季》更新第14集[中英双字", result.get(result.size() - 1).getTitle());
        assertEquals("/html/tv/oumeitv/20170929/55148.html", result.get(result.size() - 1).getDetailLink());
        assertEquals("2018-02-02", result.get(result.size() - 1).getTime());
    }


    @Test
    public void parseNewest168() {
        List<HomeItem> result = mNewest168Parse.parse(mHomePage);

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


    @Test
    public void parseThunder() {
        List<HomeItem> result = mThunderParse.parse(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2017年科幻动作《阴影效应》BD中英双字幕", result.get(0).getTitle());
        assertEquals("/html/gndy/jddy/20180213/56318.html", result.get(0).getDetailLink());
        assertEquals("2018-02-13", result.get(0).getTime());
    }

    @Test
    public void parseChinaTV() {
        List<HomeItem> result = mChinaTvParse.parse(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2018年香港电视剧《无间道2018》粤15集[国语字幕]", result.get(0).getTitle());
        assertEquals("/html/tv/hytv/20180127/56207.html", result.get(0).getDetailLink());
        assertEquals("2018-02-12", result.get(0).getTime());
    }

    @Test
    public void parseJSKTV() {
        List<HomeItem> result = mJSKParse.parse(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2018韩国JTBC金土剧《Misty》第04集[韩语中字]", result.get(0).getTitle());
        assertEquals("/html/tv/rihantv/20180204/56246.html", result.get(0).getDetailLink());
        assertEquals("2018-02-13", result.get(0).getTime());
    }

    @Test
    public void parseEATV() {
        List<HomeItem> result = mEAParse.parse(mHomePage);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("2018主打美剧《国土安全/反恐危机 第七季》更新第01集[", result.get(0).getTitle());
        assertEquals("/html/tv/oumeitv/20180213/56316.html", result.get(0).getDetailLink());
        assertEquals("2018-02-13", result.get(0).getTime());
    }

}