package com.epam.crypto_investment.controller;

import com.epam.crypto_investment.dto.CryptoNormRangeDTO;
import com.epam.crypto_investment.dto.CryptoStatsDTO;
import com.epam.crypto_investment.service.CryptoService;
import com.epam.crypto_investment.dto.CryptoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Operation(summary = "Get cryptos sorted by normalized range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cryptos",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CryptoDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/normalized-range")
    public List<CryptoDTO> getCryptosSortedByNormalizedRange() {
        return cryptoService.getCryptosSortedByNormalizedRange();
    }

    @Operation(summary = "Get crypto statistics by symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the crypto stats",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CryptoStatsDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Crypto not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{symbol}/stats")
    public CryptoStatsDTO getCryptoStats(@PathVariable String symbol) {
        return cryptoService.getCryptoStats(symbol);
    }

    @Operation(summary = "Get crypto with the highest normalized range on a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the crypto with the highest normalized range",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CryptoNormRangeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid date format",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/highest-normalized-range/{date}")
    public CryptoNormRangeDTO getCryptoWithHighestNormalizedRange(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return cryptoService.getCryptoWithHighestNormalizedRange(date);
    }

    @Operation(summary = "Get crypto statistics for the last six months by symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the crypto stats for six months",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CryptoStatsDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Crypto not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{symbol}/stats/six-months")
    public CryptoStatsDTO getCryptoStatsSixMonths(@PathVariable String symbol) {
        return cryptoService.getCryptoStatsSixMonths(symbol);
    }

    @Operation(summary = "Get crypto statistics for the last year by symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the crypto stats for the last year",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CryptoStatsDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Crypto not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{symbol}/stats/yearly")
    public CryptoStatsDTO getCryptoStatsYearly(@PathVariable String symbol) {
        return cryptoService.getCryptoStatsYearly(symbol);
    }
}