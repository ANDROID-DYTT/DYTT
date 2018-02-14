package com.bzh.dytt.data.source;


public interface IParse<T> {
    public T parse(String html);
}