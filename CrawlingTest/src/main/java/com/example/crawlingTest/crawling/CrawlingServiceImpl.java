package com.example.crawlingTest.crawling;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class CrawlingServiceImpl {
    public void detailCrawling(NewsSource newsSource) {
        String url = newsSource.getUrl();
        Document doc = null;
        Connection conn = Jsoup.connect(url);
        try {
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements crawlingDiv = doc.getElementsByClass("no-js");

        System.out.println(crawlingDiv);
    }
}

