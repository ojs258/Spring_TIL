package com.example.crawlingTest;


import com.example.crawlingTest.controller.CrawlingController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlingTestApplication {
	public static void main(String[] args) {
		CrawlingController cc = new CrawlingController();
		cc.sourceCrawling();
	}
}
