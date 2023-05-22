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
        Element crdiv = doc.getElementsByClass("main-box main-top").get(0);

        System.out.println(crdiv.text());
    }
//    private WebDriver driver;
//    public void startSelenium(){
//        System.setProperty("webdriver.chrome.driver","C:\\dev\\chromedriver_win32\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        driver = new ChromeDriver(options);
//        driver.get(url);
//        List<WebElement> elements = driver.findElements(By.cssSelector(".thumbinner"));
//
//        for(WebElement element : elements){
//            System.out.println("---------------");
//            System.out.println(element.getText());
//        }
//        //driver.quit();
//    }
}
