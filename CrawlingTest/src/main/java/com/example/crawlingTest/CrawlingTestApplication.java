package com.example.crawlingTest;


import com.example.crawlingTest.crawling.CrawlingServiceImpl;
import com.example.crawlingTest.crawling.NewsSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlingTestApplication {
	public static void main(String[] args) {
		CrawlingServiceImpl crawlingServiceImpl = new CrawlingServiceImpl();
		NewsSource newsSource = new NewsSource();
		newsSource.settingUrl("https://www.hani.co.kr/arti/area/capital/1094185.html");
		crawlingServiceImpl.detailCrawling(newsSource);
	}

}
