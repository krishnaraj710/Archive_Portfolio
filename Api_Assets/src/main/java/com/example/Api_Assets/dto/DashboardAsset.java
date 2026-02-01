package com.example.Api_Assets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DashboardAsset {
    private Long id;
    private String type;           // assetType
    private String symbol;
    private String name;
    private BigDecimal buyPrice;   // From table
    private Integer qty;           // From table
    private BigDecimal currentPrice; // LIVE API
    private LocalDateTime currentDate; // LIVE time
}
