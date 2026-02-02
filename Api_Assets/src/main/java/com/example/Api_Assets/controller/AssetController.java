package com.example.Api_Assets.controller;

import com.example.Api_Assets.dto.DashboardAsset;
import com.example.Api_Assets.entity.UserAsset;
import com.example.Api_Assets.repository.UserAssetRepository;
import com.example.Api_Assets.service.CryptoService;
import com.example.Api_Assets.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    @Autowired private UserAssetRepository repo;
    @Autowired private StockService stockService;
    @Autowired private CryptoService cryptoService;

    @PostMapping
    public UserAsset addAsset(@RequestBody UserAsset asset) {

        asset.setCurrentUpdated(LocalDateTime.now());
        asset.setLastUpdated(LocalDateTime.now());

        // 👇 ensure sell fields exist in response
        asset.setSellingPrice(null);
        asset.setSellingDate(null);

        if ("CRYPTO".equals(asset.getAssetType())) {
            asset.setCurrentPrice(cryptoService.getCryptoPrice(asset.getSymbol()));
        } else {
            asset.setCurrentPrice(stockService.getCurrentPrice(asset.getSymbol()));
        }

        return repo.save(asset);
    }


    @PostMapping("/sell/{id}")
    public UserAsset sellAsset(@PathVariable Long id) {
        UserAsset asset = repo.findById(id).orElseThrow();

        // When SOLD: current → selling
        asset.setSellingPrice(asset.getCurrentPrice());
        asset.setSellingDate(LocalDateTime.now());
        asset.setQty(0);
        asset.setLastUpdated(LocalDateTime.now());

        return repo.save(asset);
    }

    @GetMapping
    public List<UserAsset> getAllAssets() {
        return repo.findAll();
    }

    @GetMapping("/stocks")
    public List<UserAsset> getAllStocks() {
        return repo.findAllStocks();
    }

    @GetMapping("/crypto")
    public List<UserAsset> getAllCrypto() {
        return repo.findAllCrypto();
    }

    @GetMapping("/stock/{symbol}")
    public UserAsset getStock(@PathVariable String symbol) {
        return repo.findStockBySymbol(symbol)
                .orElseThrow(() -> new RuntimeException("Stock not found: " + symbol));
    }

    @GetMapping("/crypto/{symbol}")
    public UserAsset getCrypto(@PathVariable String symbol) {
        return repo.findCryptoBySymbol(symbol)
                .orElseThrow(() -> new RuntimeException("Crypto not found: " + symbol));
    }

    @GetMapping("/dashboard")
    public List<DashboardAsset> getDashboard() {
        return repo.findAll().stream()
                .map(asset -> {
                    BigDecimal livePrice = "CRYPTO".equals(asset.getAssetType())
                            ? cryptoService.getCryptoPrice(asset.getSymbol())
                            : stockService.getCurrentPrice(asset.getSymbol());

                    asset.setCurrentPrice(livePrice);
                    asset.setCurrentUpdated(LocalDateTime.now());
                    repo.save(asset);

                    return new DashboardAsset(
                            asset.getId(),
                            asset.getAssetType(),
                            asset.getSymbol(),
                            asset.getName(),
                            asset.getBuyPrice(),
                            asset.getQty(),
                            livePrice,
                            LocalDateTime.now()
                    );
                })
                .collect(Collectors.toList());
    }
}
