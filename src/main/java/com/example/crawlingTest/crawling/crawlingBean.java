package com.example.crawlingTest.crawling;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class crawlingBean {
    static final String url = "https://ko.wikipedia.org/wiki/%EC%9B%B9_%ED%81%AC%EB%A1%A4%EB%9F%AC";

    public void startJsoup() {
        Document doc = null;
        Connection conn = Jsoup.connect(url);
        try {
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements selects = doc.select("#bodyContent");

        for(Element select : selects){
            System.out.println(select.text());
        }
    }
    private WebDriver driver;
    public void startSelenium(){
        System.setProperty("webdriver.chrome.driver","C:\\dev\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get(url);
        List<WebElement> elements = driver.findElements(By.cssSelector(".thumbinner"));

        for(WebElement element : elements){
            System.out.println("---------------");
            System.out.println(element.getText());
        }
        //driver.quit();
    }
}
