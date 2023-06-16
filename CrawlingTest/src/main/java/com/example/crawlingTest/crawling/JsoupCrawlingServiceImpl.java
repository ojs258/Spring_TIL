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
        Connection conn = Jsoup.connect(url.getUrl()); // Naver검색 API로 얻은 News URL로 커넥션 생성
        try {
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        crawlingBody.add(doc.getElementsByClass("media_end_head_title").html()); // 기사 제목 태그를 크롤링
        crawlingBody.add(doc.getElementsByClass("go_trans _article_content").html()); // 기사 본문 태그를 크롤링


        return crawlingBody;
    }
}

