package com.bzh.dytt.data.source;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HomeItemParseUtil implements IParse<List<HomeArea>, List<HomeItem>> {

    private static IParse<List<HomeArea>, List<HomeItem>> mInstance = new HomeItemParseUtil();


    public static IParse<List<HomeArea>, List<HomeItem>> getInstance() {
        return mInstance;
    }

    private IParse<List<HomeArea>, List<HomeItem>> mNewestParse;
    private IParse<List<HomeArea>, List<HomeItem>> mThunderParse;
    private IParse<List<HomeArea>, List<HomeItem>> mChinaTvParse;
    private IParse<List<HomeArea>, List<HomeItem>> mJSKParse;
    private IParse<List<HomeArea>, List<HomeItem>> mEAParse;
    private IParse<List<HomeArea>, List<HomeItem>> mNewest168Parse;

    private HomeItemParseUtil() {
        mNewest168Parse = new HomeItemParseUtil.Newest168Parse();
        mNewestParse = new HomeItemParseUtil.NewestParse();
        mThunderParse = new HomeItemParseUtil.ThunderParse();
        mChinaTvParse = new HomeItemParseUtil.ChinaTVParse();
        mJSKParse = new HomeItemParseUtil.JSKTVParse();
        mEAParse = new HomeItemParseUtil.EATVParse();
    }

    @Override
    public List<HomeArea> parseAreas(String html) {
        List<HomeArea> result = new ArrayList<>();

        if (html != null && html.length() != 0) {
            result.addAll(mNewest168Parse.parseAreas(html));
            result.addAll(mNewestParse.parseAreas(html));
            result.addAll(mThunderParse.parseAreas(html));
            result.addAll(mChinaTvParse.parseAreas(html));
            result.addAll(mJSKParse.parseAreas(html));
            result.addAll(mEAParse.parseAreas(html));
        }

        return result;
    }

    @Override
    public List<HomeItem> parseItems(String html) {

        List<HomeItem> result = new ArrayList<>();

        if (html != null && html.length() != 0) {
            result.addAll(mNewest168Parse.parseItems(html));
            result.addAll(mNewestParse.parseItems(html));
            result.addAll(mThunderParse.parseItems(html));
            result.addAll(mChinaTvParse.parseItems(html));
            result.addAll(mJSKParse.parseItems(html));
            result.addAll(mEAParse.parseItems(html));
        }

        return result;
    }

    public static class EATVParse extends PageCenterContentParse {

        @Override
        protected Element getRootAreaElement(Document document) {
            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").last();
        }

        @Override
        protected int getType() {
            return HomeType.EA_TV;
        }
    }

    public static class JSKTVParse extends PageCenterContentParse {

        @Override
        protected Element getRootAreaElement(Document document) {
            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").get(3);
        }

        @Override
        protected int getType() {
            return HomeType.JSK_TV;
        }
    }

    public static class ChinaTVParse extends PageCenterContentParse {

        @Override
        protected Element getRootAreaElement(Document document) {
            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").get(2);
        }

        @Override
        protected int getType() {
            return HomeType.CHINA_TV;
        }
    }

    public static class ThunderParse extends PageCenterContentParse {

        @Override
        protected Element getRootAreaElement(Document document) {
            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").get(1);
        }

        @Override
        protected int getType() {
            return HomeType.THUNDER;
        }
    }

    public static class NewestParse extends PageCenterContentParse {

        @Override
        protected Element getRootAreaElement(Document document) {
            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").first();
        }

        @Override
        protected int getType() {
            return HomeType.NEWEST;
        }
    }

    private static abstract class PageCenterContentParse extends BaseParse {

        @Override
        protected Elements getItemElements(Element element) {
            return element.select("div").last().select("tr");
        }

        @Override
        protected String getItemTitle(Element element) {
            return element.select("a").last().text();
        }

        @Override
        protected String getItemLink(Element element) {
            return element.select("a").last().attr("href");
        }

        @Override
        protected String getItemTime(Element element) {
            return element.select("td").select("font").text();
        }

        @Override
        protected Element getAreaElement(Element area) {
            return area.select("div.title_all").first();
        }

        @Override
        protected String getAreaTitle(Element element) {
            return element.select("strong").text();
        }

        @Override
        protected String getAreaDetailLink(Element element) {
            return element.select("a").attr("href");
        }
    }

    public static class Newest168Parse extends BaseParse {

        @Override
        protected Element getRootAreaElement(Document document) {
            return document.select("div.bd3l").select("div.co_area2").last();
        }

        @Override
        protected Element getAreaElement(Element area) {
            return area.select("div.title_all").first();
        }

        @Override
        protected String getItemTime(Element element) {
            return "";
        }

        @Override
        protected String getItemLink(Element element) {
            return element.attr("href");
        }

        @Override
        protected String getItemTitle(Element element) {
            return element.text();
        }

        @Override
        protected Elements getItemElements(Element element) {
            return element.select("div.co_content2").select("ul").select("a");
        }

        @Override
        protected String getAreaDetailLink(Element element) {
            return "";
        }

        @Override
        protected String getAreaTitle(Element element) {
            return element.text();
        }

        @Override
        protected int getType() {
            return HomeType.NEWEST_168;
        }
    }

    private static abstract class BaseParse implements IParse<List<HomeArea>, List<HomeItem>> {

        @Override
        public List<HomeArea> parseAreas(String html) {
            List<HomeArea> result = new ArrayList<>();

            try {

                Document document = Jsoup.parse(html);

                Element area = getRootAreaElement(document);

                Element element = getAreaElement(area);

                String title = getAreaTitle(element);

                String detailLink = getAreaDetailLink(element);

                HomeArea homeArea = new HomeArea();
                homeArea.setTitle(title);
                homeArea.setDetailLink(detailLink);
                homeArea.setType(getType());

                result.add(homeArea);

            } catch (Exception e) {
                return result;
            }
            return result;
        }

        protected abstract Element getAreaElement(Element area);


        @Override
        public List<HomeItem> parseItems(String html) {
            List<HomeItem> result = new ArrayList<>();

            try {
                Document document = Jsoup.parse(html);

                Element area = getRootAreaElement(document);

                Elements elements = getItemElements(area);

                for (Element element : elements) {

                    String title = getItemTitle(element);
                    String link = getItemLink(element);
                    String time = getItemTime(element);

                    HomeItem homeItem = new HomeItem();
                    homeItem.setType(getType());
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

        protected abstract Element getRootAreaElement(Document document);

        protected abstract String getItemTime(Element element);

        protected abstract String getItemLink(Element element);

        protected abstract String getItemTitle(Element element);

        protected abstract Elements getItemElements(Element element);

        protected abstract String getAreaDetailLink(Element element);

        protected abstract String getAreaTitle(Element element);

        protected abstract int getType();

    }

}
