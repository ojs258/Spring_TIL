package com.example.crawlingTest.crawling;

public class NewsSource {

    private String url;

    public String getUrl() {
        return url;
    }

    public void settingUrl(String url) {
        NewsSource newsSource = new NewsSource();
        newsSource.url = url;
    }
}
