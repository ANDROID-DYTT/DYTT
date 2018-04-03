package com.bzh.dytt.util;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.MovieCategory;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class LoadableMovieParserTest {

    private LoadableMovieParser loadableMovieParser;

    @Before
    public void setUp() throws Exception {
        loadableMovieParser = new LoadableMovieParser();
    }

    @Test
    public void getMovieList() throws IOException {
        String newMovie = TestUtils.getResource(getClass(), "new_movie.html");
        List<CategoryMap> result = loadableMovieParser.parseCategoryMap(newMovie, MovieCategory.NEW_MOVIE);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("/html/gndy/dyzz/20180328/56582.html", result.get(0).getLink());
    }
}