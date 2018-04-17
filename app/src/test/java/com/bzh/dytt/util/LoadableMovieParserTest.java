package com.bzh.dytt.util;

import com.bzh.dytt.TestUtils;
import com.bzh.dytt.data.entity.CategoryMap;
import com.bzh.dytt.data.entity.MovieCategory;

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
    public void setUp() {
        loadableMovieParser = new LoadableMovieParser();
    }

    @Test
    public void getMovieList() throws IOException {
        String newMovie = TestUtils.getResource(getClass(), "new_movie.html");
        List<CategoryMap> result = loadableMovieParser.parse(newMovie, MovieCategory.NEW_MOVIE);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals("/html/gndy/jddy/20180328/56586.html", result.get(0).getLink());
    }
}