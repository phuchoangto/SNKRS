package com.example.snkrs.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CurrencyConverter {
    @Value("${openexchangerates.app_id}")
    private String appId;
    private final String baseURL = "https://openexchangerates.org/api/latest.json";

    public double convertVNDToUSD(double amount) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
        urlBuilder.addQueryParameter("app_id", appId);
        urlBuilder.addQueryParameter("symbols", "VND");
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        double rate = jsonNode.get("rates").get("VND").asDouble();
        return amount / rate;
    }
}
