package com.bzh.dytt.util;

import com.bzh.dytt.data.CategoryMap;
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

    private InternalHomeParser mNewestParse;

    @Inject
    public HomePageParser() {
//        mNewest168Parse = new HomePageParser.Newest168Parse();
        mNewestParse = new HomePageParser.NewestParse();
//        mThunderParse = new HomePageParser.ThunderParse();
//        mChinaTvParse = new HomePageParser.ChinaTVParse();
//        mJSKParse = new HomePageParser.JSKTVParse();
//        mEAParse = new HomePageParser.EATVParse();
    }

    public List<CategoryMap> parse(String html) {
        List<CategoryMap> result = new ArrayList<>();

        if (html != null && html.length() != 0) {

            List<CategoryMap> categoryMaps =  mNewestParse.parseCategoryMapItems(html);
            for (CategoryMap category : categoryMaps) {
                String link = category.getLink();
                if (link.contains("gndy")) {
                    result.add(category);
                }
            }
        }
        return result;
    }

//    public static class EATVParse extends PageCenterContentParse {
//
//        @Override
//        protected Element getRootAreaElement(Document document) {
//            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").last();
//        }
//
//        @Override
//        protected MovieCategory getType() {
//            return MovieCategory.EA_TV;
//        }
//    }
//
//    public static class JSKTVParse extends PageCenterContentParse {
//
//        @Override
//        protected Element getRootAreaElement(Document document) {
//            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").get(3);
//        }
//
//        @Override
//        protected int getType() {
//            return HomeType.JSK_TV;
//        }
//    }
//
//    public static class ChinaTVParse extends PageCenterContentParse {
//
//        @Override
//        protected Element getRootAreaElement(Document document) {
//            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").get(2);
//        }
//
//        @Override
//        protected int getType() {
//            return HomeType.CHINA_TV;
//        }
//    }
//
//    public static class ThunderParse extends PageCenterContentParse {
//
//        @Override
//        protected Element getRootAreaElement(Document document) {
//            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").get(1);
//        }
//
//        @Override
//        protected int getType() {
//            return HomeType.THUNDER;
//        }
//    }

    public static class NewestParse extends PageCenterContentParse {

        @Override
        protected Element getRootAreaElement(Document document) {
            return document.select("div.bd3r").first().select("div.bd3rl").select("div.co_area2").first();
        }

        @Override
        protected MovieCategory getType() {
            return MovieCategory.HOME_LATEST_MOVIE;
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

//    public static class Newest168Parse extends InternalHomeParser {
//
//        @Override
//        protected Element getRootAreaElement(Document document) {
//            return document.select("div.bd3l").select("div.co_area2").last();
//        }
//
//        @Override
//        protected Element getAreaElement(Element area) {
//            return area.select("div.title_all").first();
//        }
//
//        @Override
//        protected String getItemTime(Element element) {
//            return "";
//        }
//
//        @Override
//        protected String getItemLink(Element element) {
//            return element.attr("href");
//        }
//
//        @Override
//        protected String getItemTitle(Element element) {
//            return element.text();
//        }
//
//        @Override
//        protected Elements getItemElements(Element element) {
//            return element.select("div.co_content2").select("ul").select("a");
//        }
//
//        @Override
//        protected String getAreaDetailLink(Element element) {
//            return "";
//        }
//
//        @Override
//        protected String getAreaTitle(Element element) {
//            return element.text();
//        }
//
//        @Override
//        protected int getType() {
//            return HomeType.NEWEST_168;
//        }
//    }

    private static abstract class InternalHomeParser {

        public List<CategoryMap> parseCategoryMapItems(String html) {
            List<CategoryMap> result = new ArrayList<>();

            try {
                Document document = Jsoup.parse(html);

                Element area = getRootAreaElement(document);

                Elements elements = getItemElements(area);

                for (Element element : elements) {

                    String title = getItemTitle(element);
                    String link = getItemLink(element);
                    String time = getItemTime(element);

                    CategoryMap categoryMap = new CategoryMap();
                    categoryMap.setLink(link);
                    categoryMap.setSN(Integer.parseInt(link.substring(link.lastIndexOf('/') + 1, link.lastIndexOf('.'))));
                    categoryMap.setCategory(getType());
                    categoryMap.setName(title);
                    result.add(categoryMap);
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

        protected abstract MovieCategory getType();

    }

}
