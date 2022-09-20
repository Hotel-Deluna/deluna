package com.hotel.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class KaKaoUtil {

    @Value("${kakao.secretKey}")
    static String kakaoKey;

    public static JSONArray getKeywordMapData(String text) throws Exception{
        JSONArray result = null;
        URL url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?size=3&query="+ URLEncoder.encode(text, StandardCharsets.UTF_8));

        // GET 전송
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-type", "application/json");
        con.setRequestProperty("Authorization", kakaoKey);
        con.setDoOutput(true);
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        StringBuilder sb = new StringBuilder();
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            // 받아온 response 데이터 json 형식으로 파싱 -> 필요한 결과값 추출
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(sb.toString());
            JSONObject jsonObj = (JSONObject) obj;
            result = (JSONArray) jsonObj.get("documents");
        }
        return result;
    }
}
