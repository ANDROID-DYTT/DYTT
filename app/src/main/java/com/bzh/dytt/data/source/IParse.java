package com.bzh.dytt.data.source;


public interface IParse<A, I> {

    A parseAreas(String html);

    I parseItems(String html);
}