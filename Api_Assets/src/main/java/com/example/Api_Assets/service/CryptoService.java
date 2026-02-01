package com.example.Api_Assets.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CryptoService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String COINGECKO_URL = "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd";

    public BigDecimal getCryptoPrice(String coinId) {  // bitcoin, ethereum, solana
        try {
            String url = String.format(COINGECKO_URL, coinId);
            String response = restTemplate.getForObject(url, String.class);

            // Parse JSON response: {"bitcoin":{"usd":95000}}
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response);
            return node.get(coinId).get("usd").decimalValue();
        } catch (Exception e) {
            System.out.println("CoinGecko error for " + coinId + ": " + e.getMessage());
            return BigDecimal.valueOf(100.0);  // Fallback
        }
    }
}
