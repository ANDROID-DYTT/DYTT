package com.bzh.dytt.data.source;

import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeItemType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HomePageParseUtil implements IParse<List<HomeItem>> {

    private static IParse<List<HomeItem>> mInstance = new HomePageParseUtil();


    public static IParse<List<HomeItem>> getInstance() {
        return mInstance;
    }

    private IParse<List<HomeItem>> mNewestParse;
    private IParse<List<HomeItem>> mThunderParse;
    private IParse<List<HomeItem>> mChinaTvParse;
    private IParse<List<HomeItem>> mJSKParse;
    private IParse<List<HomeItem>> mEAParse;
    private IParse<List<HomeItem>> mNewest168Parse;

    private HomePageParseUtil() {
        mNewest168Parse = new HomePageParseUtil.Newest168Parse();
        mNewestParse = new HomePageParseUtil.NewestParse();
        mThunderParse = new HomePageParseUtil.ThunderParse();
        mChinaTvParse = new HomePageParseUtil.ChinaTVParse();
        mJSKParse = new HomePageParseUtil.JSKTVParse();
        mEAParse = new HomePageParseUtil.EATVParse();
    }

    public List<HomeItem> parse(String html) {

        List<HomeItem> result = new ArrayList<>();

        result.addAll(mNewest168Parse.parse(html));
        result.addAll(mNewestParse.parse(html));
        result.addAll(mThunderParse.parse(html));
        result.addAll(mChinaTvParse.parse(html));
        result.addAll(mJSKParse.parse(html));
        result.addAll(mEAParse.parse(html));

        return result;
    }

    public static class EATVParse implements IParse<List<HomeItem>> {

        private static final String START_ANNOTATION = "<!--{start:最新欧美剧集下载-->";
        private static final String END_ANNOTATION = "<!--}end:最新欧美剧集下载--->";

        @Override
        public List<HomeItem> parse(String html) {

            List<HomeItem> result = new ArrayList<>();

            try {
                int startIndex = html.indexOf(START_ANNOTATION);
                int endIndex = html.indexOf(END_ANNOTATION);
                String content = html.substring(startIndex, endIndex);
                Document document = Jsoup.parse(content);

                Elements trs = document.select("div.co_content3").select("tr");
                for (Element tr : trs) {

                    Element a = tr.select("a").last();
                    String title = a.text();
                    String link = a.attr("href");
                    String time = tr.select("td").select("font").text();

                    HomeItem homeItem = new HomeItem();
                    homeItem.setType(HomeItemType.EA_TV);
                    homeItem.setTitle(title);
                    homeItem.setDetailLink(link);
                    homeItem.setTime(time);

                    result.add(homeItem);
                }

            } catch (Exception e) {
                return result;
            }
            return result;
        }
    }

    public static class JSKTVParse implements IParse<List<HomeItem>> {

        private static final String START_ANNOTATION = "<!--{start:最新TV下载-->";
        private static final String END_ANNOTATION = "<!--}end:最新TV下载--->";

        @Override
        public List<HomeItem> parse(String html) {

            List<HomeItem> result = new ArrayList<>();

            try {
                int startIndex = html.lastIndexOf(START_ANNOTATION);
                int endIndex = html.lastIndexOf(END_ANNOTATION);
                String content = html.substring(startIndex, endIndex);
                Document document = Jsoup.parse(content);

                Elements trs = document.select("div.co_content3").select("tr");
                for (Element tr : trs) {

                    Element a = tr.select("a").last();
                    String title = a.text();
                    String link = a.attr("href");
                    String time = tr.select("td").select("font").text();

                    HomeItem homeItem = new HomeItem();
                    homeItem.setType(HomeItemType.JSK_TV);
                    homeItem.setTitle(title);
                    homeItem.setDetailLink(link);
                    homeItem.setTime(time);

                    result.add(homeItem);
                }

            } catch (Exception e) {
                return result;
            }
            return result;
        }
    }

    public static class ChinaTVParse implements IParse<List<HomeItem>> {

        private static final String START_ANNOTATION = " <!--{start:最新TV下载-->";
        private static final String END_ANNOTATION = "<!--}end:最新TV下载--->";

        @Override
        public List<HomeItem> parse(String html) {

            List<HomeItem> result = new ArrayList<>();

            try {
                int startIndex = html.indexOf(START_ANNOTATION);
                int endIndex = html.indexOf(END_ANNOTATION);
                String content = html.substring(startIndex, endIndex);
                Document document = Jsoup.parse(content);

                Elements trs = document.select("div.co_content3").select("tr");
                for (Element tr : trs) {

                    Element a = tr.select("a").last();
                    String title = a.text();
                    String link = a.attr("href");
                    String time = tr.select("td").select("font").text();

                    HomeItem homeItem = new HomeItem();
                    homeItem.setType(HomeItemType.CHINA_TV);
                    homeItem.setTitle(title);
                    homeItem.setDetailLink(link);
                    homeItem.setTime(time);

                    result.add(homeItem);
                }

            } catch (Exception e) {
                return result;
            }
            return result;
        }
    }

    public static class ThunderParse implements IParse<List<HomeItem>> {

        private static final String START_ANNOTATION = "<!--{start:最新影视下载-->";
        private static final String END_ANNOTATION = "<!--}end:最新下载--->";

        @Override
        public List<HomeItem> parse(String html) {

            List<HomeItem> result = new ArrayList<>();

            try {
                int startIndex = html.lastIndexOf(START_ANNOTATION);
                int endIndex = html.lastIndexOf(END_ANNOTATION);
                String content = html.substring(startIndex, endIndex);
                Document document = Jsoup.parse(content);

                Elements trs = document.select("div.co_content8").select("tr");
                for (Element tr : trs) {

                    Element a = tr.select("a").last();
                    String title = a.text();
                    String link = a.attr("href");
                    String time = tr.select("td").select("font").text();

                    HomeItem homeItem = new HomeItem();
                    homeItem.setType(HomeItemType.THUNDER);
                    homeItem.setTitle(title);
                    homeItem.setDetailLink(link);
                    homeItem.setTime(time);

                    result.add(homeItem);
                }

            } catch (Exception e) {
                return result;
            }
            return result;
        }
    }

    public static class NewestParse implements IParse<List<HomeItem>> {

        private static final String START_ANNOTATION = "<!--{start:最新影视下载-->";
        private static final String END_ANNOTATION = "<!--}end:最新下载--->";

        @Override
        public List<HomeItem> parse(String html) {

            List<HomeItem> result = new ArrayList<>();

            try {
                int startIndex = html.indexOf(START_ANNOTATION);
                int endIndex = html.indexOf(END_ANNOTATION);
                String content = html.substring(startIndex, endIndex);
                Document document = Jsoup.parse(content);

                Elements trs = document.select("div.co_content8").select("tr");
                for (Element tr : trs) {

                    Element a = tr.select("a").last();
                    String title = a.text();
                    String link = a.attr("href");
                    String time = tr.select("td").select("font").text();

                    HomeItem homeItem = new HomeItem();
                    homeItem.setType(HomeItemType.NEWEST);
                    homeItem.setTitle(title);
                    homeItem.setDetailLink(link);
                    homeItem.setTime(time);

                    result.add(homeItem);
                }

            } catch (Exception e) {
                return result;
            }
            return result;
        }
    }

    public static class Newest168Parse implements IParse<List<HomeItem>> {

        private static final String START_ANNOTATION = "<!--{start:最新-->";
        private static final String END_ANNOTATION = "<!--}end:最新-->";

        @Override
        public List<HomeItem> parse(String html) {

            List<HomeItem> result = new ArrayList<>();

            try {
                int startIndex = html.indexOf(START_ANNOTATION);
                int endIndex = html.indexOf(END_ANNOTATION);
                String htmlContent = html.substring(startIndex, endIndex);
                Document document = Jsoup.parse(htmlContent);
                Elements elements = document.select("div.co_content2").select("ul");
                Elements hrefs = elements.select("a[href]");

                for (Element element : hrefs) {
                    String title = element.text();
                    String link = element.attr("href");

                    HomeItem homeItem = new HomeItem();
                    homeItem.setType(HomeItemType.NEWEST_168);
                    homeItem.setTitle(title);
                    homeItem.setDetailLink(link);

                    result.add(homeItem);
                }

            } catch (Exception e) {
                return result;
            }
            return result;
        }
    }

}
