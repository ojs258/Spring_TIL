package com.example.crawlingTest;


import com.example.crawlingTest.controller.CrawlingController;
import com.example.crawlingTest.crawling.CrawlingService;
import com.example.crawlingTest.crawling.JsoupCrawlingServiceImpl;
import com.example.crawlingTest.crawling.NaverCrawlingServiceImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class CrawlingTestApplication {
	public static void main(String[] args) {
		CrawlingController cc = new CrawlingController();
		cc.detailCrawling();
	}
}
