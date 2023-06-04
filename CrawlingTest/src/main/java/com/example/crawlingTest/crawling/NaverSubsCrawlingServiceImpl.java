package com.example.crawlingTest.crawling;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class NaverSubsCrawlingServiceImpl implements CrawlingService{

    @Override
    public List<String> crawling() { // 가져온 검색 기록을 서로 비교하여 공통된 부분만 추려내는 부분

        String apiUrl = makeApiUrl();
        String responseBody = get(apiUrl);
        responseBody.toString();
        return null;
    }
    private String makeApiUrl(){ // 구독 언론사, 카테고리, 키워드에 해당하는 뉴스를 가져오는 부분
        String keyword = null;
        try {
            keyword = URLEncoder.encode("대학생", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }
        
        String query = "?query=" + keyword;
        String display = "&display=100";
        String start = "&start=1";
        String sort = "&sort=sim"; //sim -> 검색어 정확도순서 정렬(내림차순), date -> 검색어 날자순 정렬(내림차순)
        String apiUrl = "https://openapi.naver.com/v1/search/news.json?query=" + query + display + start + sort;

        return apiUrl;
    }

    private String get(String apiUrl) {

        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET"); // API를 GET방식으로 호출하는것으로 등록
            con.setRequestProperty("X-Naver-Client-Id","q7T37qBLF0PdgAjo97uG"); // API 아이디를 등록
            con.setRequestProperty("X-Naver-Client-Secret","******"); // API Secret넘버를 등록

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }


}
