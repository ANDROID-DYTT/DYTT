package com.bzh.dytt.data;

public enum MovieCategory {
    NONE(0),
    NEW_MOVIE(1),
    CHINA_MOVIE(2),
    OUMEI_MOVIE(3),
    RIHAN_MOVIE(4),
    HOME_LATEST_MOVIE(5),
    SEARCH_MOVIE(6);

    private int id;

    MovieCategory(int id) {
        this.id = id;
    }

    public String getTitle() {
        switch (id) {
            case 1:
                return "最新电影";
            case 2:
                return "国内电影";
            case 3:
                return "欧美电影";
            case 4:
                return "日韩电影";
            case 5:
                return "主页最新电影";
            case 6:
                return "搜索结果";
            default:
                return "未知";
        }
    }

    public int getId() {
        return id;
    }

    public CategoryPage getDefaultPage() {
        return new CategoryPage(this, 1);
    }

    public String getDefaultUrl() {
        return String.format(getUnformatUrl(), 1);
    }

    public String getNextPageUrl(CategoryPage categoryPage) {
        categoryPage.setNextPage(categoryPage.getNextPage() + 1);
        return String.format(getUnformatUrl(), categoryPage.getNextPage());
    }

    public String getUnformatUrl() {
        String result;
        switch (id) {
            case 1:
                result = "dyzz/list_23_%d.html";
                break;
            case 2:
                result = "china/list_4_%d.html";
                break;
            case 3:
                result = "oumei/list_7_%d.html";
                break;
            case 4:
                result = "rihan/list_6_%d.html";
                break;
            default:
                result = "dyzz/list_23_%d.html";
        }
        return result;
    }
}
