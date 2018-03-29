package com.bzh.dytt.util;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;
import com.bzh.dytt.data.MovieCategory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomePageParser {

    private InternalHomeParser mNewestParse, mThunderParse, mChinaTvParse, mJSKParse, mEAParse, mNewest168Parse;

    @Inject
    public HomePageParser() {
        mNewest168Parse = new HomePageParser.Newest168Parse();
        mNewestParse = new HomePageParser.NewestParse();
        mThunderParse = new HomePageParser.ThunderParse();
        mChinaTvParse = new HomePageParser.ChinaTVParse();
        mJSKParse = new HomePageParser.JSKTVParse();
        mEAParse = new HomePageParser.EATVParse();
    }

    public List<HomeArea> parseAreas(String html) {
        List<HomeArea> result = new ArrayList<>();

        if (html != null && html.length() != 0) {
            result.addAll(mNewestParse.parseAreas(html));
//            result.addAll(mNewest168Parse.parseAreas(html));
//            result.addAll(mThunderParse.parseAreas(html));
//            result.addAll(mChinaTvParse.parseAreas(html));
//            result.addAll(mJSKParse.parseAreas(html));
//            result.addAll(mEAParse.parseAreas(html));
        }

        return result;
    }

    public List<HomeItem> parseItems(String html) {

        List<HomeItem> result = new ArrayList<>();

        if (html != null && html.length() != 0) {
//            List<HomeItem> l1 = mNewest168Parse.parseItems(html);
//            result.addAll(l1);
            List<HomeItem> l2 = mNewestParse.parseItems(html);
            result.addAll(l2);
//            List<HomeItem> l3 = mThunderParse.parseItems(html);
//            result.addAll(l3);
//            List<HomeItem> l4 = mChinaTvParse.parseItems(html);
//            result.addAll(l4);
//            List<HomeItem> l5 = mJSKParse.parseItems(html);
//            result.addAll(l5);
//            List<HomeItem> l6 = mEAParse.parseItems(html);
//            result.addAll(l6);
        }

        return result;
    }


    public List<CategoryMap> parseLatestMovieCategoryMap(String html) {

        List<CategoryMap> result = new ArrayList<>();

        if (html != null && html.length() != 0) {

            List<HomeItem> homeItems = mNewestParse.parseItems(html);
            for (HomeItem homeItem : homeItems) {
                String link = homeItem.getDetailLink();
                if (link.contains("gndy")) {
                    CategoryMap categoryMap = new CategoryMap();
                    categoryMap.setLink(link);
                    categoryMap.setSN(Integer.parseInt(link.substring(link.lastIndexOf('/') + 1, link.lastIndexOf('.'))));
                    categoryMap.setCategory(MovieCategory.HOME_LATEST_MOVIE);
                    result.add(categoryMap);
                }
            }
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

    private static abstract class PageCenterContentParse extends InternalHomeParser {

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

    public static class Newest168Parse extends InternalHomeParser {

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

    private static abstract class InternalHomeParser {


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

        protected abstract Element getAreaElement(Element area);

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
