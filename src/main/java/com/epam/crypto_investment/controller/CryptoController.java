package com.epam.crypto_investment.controller;

import com.epam.crypto_investment.dto.CryptoStatsDTO;
import com.epam.crypto_investment.service.CryptoService;
import com.epam.crypto_investment.dto.CryptoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/normalized-range")
    public List<CryptoDTO> getCryptosSortedByNormalizedRange() {
        return cryptoService.getCryptosSortedByNormalizedRange();
    }

    @GetMapping("/{symbol}/stats")
    public CryptoStatsDTO getCryptoStats(@PathVariable String symbol) {
        return cryptoService.getCryptoStats(symbol);
    }


}
