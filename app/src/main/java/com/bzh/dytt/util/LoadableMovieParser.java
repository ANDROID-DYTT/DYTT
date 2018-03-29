package com.bzh.dytt.util;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.MovieCategory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoadableMovieParser {

    private InternalMovieListParser mInternalMovieListParser;

    @Inject
    public LoadableMovieParser() {
        mInternalMovieListParser = new InternalMovieListParser();
    }

    public List<CategoryMap> getMovieList(String html, MovieCategory category) {
        return mInternalMovieListParser.parse(html, category);
    }

    private static class InternalMovieListParser {
        public List<CategoryMap> parse(String html, MovieCategory category) {
            Document document = Jsoup.parse(html);
            Elements elements = document.select("div.co_content8").select("ul");
            Elements hrefs = elements.select("a[href]");

            Pattern pattern = Pattern.compile("^\\[.*\\]$");
            List<CategoryMap> categoryMaps = new ArrayList<>();
            for (Element element : hrefs) {
                String fullName = element.text();
                if (pattern.matcher(fullName).matches()) {
                    continue;
                }
                CategoryMap categoryMap = new CategoryMap();

                String link = element.attr("href");
                categoryMap.setLink(link);
                categoryMap.setSN(Integer.parseInt(link.substring(link.lastIndexOf('/') + 1, link.lastIndexOf('.'))));
                categoryMap.setCategory(category);
                categoryMaps.add(categoryMap);
            }

            return categoryMaps;
        }

        private boolean isFilmType(String fullName) {
            return fullName.contains("》");
        }
    }

}
