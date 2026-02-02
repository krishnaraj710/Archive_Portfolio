package com.example.Api_Assets.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class StockService {

    @Value("${stockdata.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getCurrentPrice(String symbol) {
        try {
            String url =
                    "https://api.stockdata.org/v1/data/quote"
                            + "?symbols=" + symbol
                            + "&api_token=" + apiKey;

            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response == null || !response.has("data") || response.get("data").isEmpty()) {
                throw new RuntimeException("Invalid API response");
            }

            JsonNode stockNode = response.get("data").get(0);
            BigDecimal price = stockNode.get("price").decimalValue();

            System.out.println("✅ StockData price: " + symbol + " = " + price);
            return price;

        } catch (Exception e) {
            System.err.println("❌ StockData API failed for " + symbol + ": " + e.getMessage());
        }

        // 🔁 fallback if API fails
        return new BigDecimal("150.00");
    }
}
