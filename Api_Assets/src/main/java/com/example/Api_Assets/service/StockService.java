package com.example.Api_Assets.service;

import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class StockService {
    private static final BigDecimal FALLBACK_PRICE = BigDecimal.valueOf(100.0);

    public BigDecimal getCurrentPrice(String symbol) {
        try {
            StockQuote quote = YahooFinance.get(symbol).getQuote();
            return quote.getPrice();
        } catch (IOException e) {
            if (e.getMessage().contains("429")) {
                System.out.println("Yahoo rate limited - using fallback price for " + symbol);
            }
            return FALLBACK_PRICE;  // Returns $100 instead of crashing
        }
    }
}
