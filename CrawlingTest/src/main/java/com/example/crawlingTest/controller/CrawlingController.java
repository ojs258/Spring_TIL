package com.example.crawlingTest.controller;

import com.example.crawlingTest.crawling.CrawlingService;
import com.example.crawlingTest.crawling.JsoupCrawlingServiceImpl;
import com.example.crawlingTest.crawling.NaverCrawlingServiceImpl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlingController {
    CrawlingService source = new NaverCrawlingServiceImpl();
    CrawlingService detail = new JsoupCrawlingServiceImpl();

    public void sourceCrawling() {

    }
    public void detailCrawling(){

        List<String> elements = detail.crawling();
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>"); //img 태그 src 추출 정규표현식
        Matcher matcher = pattern.matcher(elements.get(1));
        while(matcher.find()){
            System.out.println(matcher.group(1));
        }
        for (String body : elements) {
            System.out.println("---------------------");
            System.out.println(body);
        }
    }
}
