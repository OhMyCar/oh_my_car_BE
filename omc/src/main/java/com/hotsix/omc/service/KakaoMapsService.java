package com.hotsix.omc.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoMapsService {

    @Value("${apikey.kakao.rest.api.key}")
    private String apiKey;


    public Map<String, Object> getLatLnt(String fullAddress) throws IOException, ParseException {
        String apiURL = "https://dapi.kakao.com/v2/local/search/address.json?query=";
        String query = "서울특별시 강남구 역삼동 823-3";

        URL url = new URL(apiURL + URLEncoder.encode(query, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        jsonObject = (JSONObject) jsonParser.parse(response.toString());
        JSONArray documents = (JSONArray) jsonObject.get("documents");
        JSONObject document = (JSONObject) documents.get(0);
        JSONObject address = (JSONObject) document.get("address");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("longitude", address.get("x"));
        resultMap.put("latitude", address.get("y"));

        return resultMap;

    }
}

