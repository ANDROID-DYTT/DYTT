package com.bzh.dytt.util;


import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.MovieCategory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CategoryPageParser {

    private HomePageParser mHomePageParser;
    private LoadableMovieParser mLoadableMovieParser;

    @Inject
    public CategoryPageParser(HomePageParser homePageParser, LoadableMovieParser loadableMovieParser) {
        mHomePageParser = homePageParser;
        mLoadableMovieParser = loadableMovieParser;
    }

    public List<CategoryMap> parse(String html, MovieCategory category) {
        switch (category) {
            case HOME_LATEST_MOVIE:
                return mHomePageParser.parse(html);
            case NEW_MOVIE:
            case CHINA_MOVIE:
            case OUMEI_MOVIE:
            case RIHAN_MOVIE:
            case SEARCH_MOVIE:
                return mLoadableMovieParser.parse(html, category);
        }
        return Collections.emptyList();
    }
}
