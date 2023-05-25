package com.example.crawlingTest.crawling;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class crawlingBean {
    static final String url = "https://ko.wikipedia.org/";

    public void startJsoup() {
        Document doc = null;
        Connection conn = Jsoup.connect(url);
        try {
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements crdiv = doc.getElementsByClass("main-box main-top");

        System.out.println(crdiv);
    }
    public static void main(String[] args) {
        crawlingBean crawlingBean = new crawlingBean();
        crawlingBean.startJsoup();
    }
}

