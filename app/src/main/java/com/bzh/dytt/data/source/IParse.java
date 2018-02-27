package com.bzh.dytt.data.source;


public interface IParse<T> {

    T parse(String html);

}