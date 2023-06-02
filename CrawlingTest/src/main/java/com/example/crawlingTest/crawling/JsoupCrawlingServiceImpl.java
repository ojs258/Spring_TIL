package com.example.crawlingTest.crawling;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class JsoupCrawlingServiceImpl implements CrawlingService{
    URL url = new URL();
    @Override
    public List<String> crawling() {
        List<String> crawlingBody = new ArrayList<>();
        Document doc = null;
        Connection conn = Jsoup.connect(url.getUrl());
        try {
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        crawlingBody.add(doc.getElementsByClass("media_end_head_title").html());
        crawlingBody.add(doc.getElementsByClass("go_trans _article_content").html());


        return crawlingBody;
    }

//
//    private WebDriver driver;
//    public List<WebElement> detailCrawling2() {
//        System.setProperty("webdriver.chrome.driver","C:\\dev\\chromedriver_win32\\chromedriver.exe");
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//
//
//        try {
//            driver = new ChromeDriver();
//            Thread.sleep(1000);
//            driver.get(url.getUrl());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        driver.close();
//
//        return driver.findElements(By.className("media_end_head_title"));
//    }

}

