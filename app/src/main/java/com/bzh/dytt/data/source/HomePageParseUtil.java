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

    private final IParse<List<HomeItem>> mNewest168Parse;

    private HomePageParseUtil() {
        mNewest168Parse = new Newest168Parse();
    }

    public List<HomeItem> parse(String html) {

        List<HomeItem> newest168 = mNewest168Parse.parse(html);

        return newest168;
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
