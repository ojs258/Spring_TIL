package com.example.crawlingTest;


import com.example.crawlingTest.crawling.crawlingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlingTestApplication {
	public static void main(String[] args) {
		crawlingBean crawlingBean = new crawlingBean();
		crawlingBean.startSelenium();
		//crawlingBean.startSelenium();
		//SpringApplication.run(CrawlingTestApplication.class, args);
	}

}
