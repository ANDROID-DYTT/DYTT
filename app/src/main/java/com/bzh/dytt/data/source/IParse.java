package com.bzh.dytt.data.source;


public interface IParse<A, I> {

    public A parseAreas(String html);

    public I parseItems(String html);
}