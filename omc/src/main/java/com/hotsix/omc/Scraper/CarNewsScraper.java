package com.hotsix.omc.Scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarNewsScraper {

    public List<Map<String, String>> getCarNews() {
        String url = "https://auto.danawa.com/";
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements div = doc.select("ul.type_txt");
            Elements news = div.select("li");

            for (Element newsElement : news) {
                String title = newsElement.select("a strong.subj").text();
                String link = newsElement.select("a").attr("href");

                // 링크에 접속하여 이미지 가져오기
                Document newsDoc = Jsoup.connect(link).get();
                String image = newsDoc.select("div.board_exp img").attr("src");

                Map<String, String> newsMap = new HashMap<>();
                newsMap.put("title", title);
                newsMap.put("link", link);
                newsMap.put("image", image);

                result.add(newsMap);
            }
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
        return result;
    }
}
