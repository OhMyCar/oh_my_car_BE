package com.hotsix.omc.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
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

    public String getGeoJson(String fullAddress) {
        String apiURL = "https://dapi.kakao.com/v2/local/search/address.json?query=";

        try {
            URL url = new URL(apiURL + URLEncoder.encode(fullAddress, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve geocode string for address: " + fullAddress, e);
        }
    }

    public Map<String, Object> parseGeocode(String jsonString) {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse geocode JSON string: " + jsonString, e);
        }
        JSONArray documents = (JSONArray) jsonObject.get("documents");
        JSONObject document = (JSONObject) documents.get(0);
        JSONObject address = (JSONObject) document.get("address");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("longitude", address.get("x"));
        resultMap.put("latitude", address.get("y"));

        return resultMap;

    }

}



