package com.example.crawlingTest.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class crawlingBean {
    private WebDriver driver;

    private static final String url = "https://biz.chosun.com/policy/policy_sub/2023/04/30/FFSZOBHG6VGDNJSQT4ZS43HMUM";

    public void process() {
        System.setProperty("webdriver.chrome.driver", "C:\\dev\\chromedriver_win32\\chromedriver.exe");

        driver = new ChromeDriver();

        try {
            getDataList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();
        driver.quit();
    }

    private List<String> getDataList() throws InterruptedException {
        List<String> list = new ArrayList<>();

        driver.get(url);
        Thread.sleep(1000);

        List<WebElement> elements = driver.findElements(By.cssSelector("div"));
        for (WebElement element : elements) {
            System.out.println("-------------------");
            System.out.println(element);
        }
        return list;
    }
}
