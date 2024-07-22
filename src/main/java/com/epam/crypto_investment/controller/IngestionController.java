package com.epam.crypto_investment.controller;

import com.epam.crypto_investment.dto.CryptoPriceDTO;
import com.epam.crypto_investment.exception.UnsupportedCryptoException;
import com.epam.crypto_investment.service.mapper.CryptoPriceMapper;
import com.epam.crypto_investment.service.CryptoPriceService;
import com.epam.crypto_investment.service.CsvParsingService;
import com.epam.crypto_investment.entity.CryptoPrice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class IngestionController {

    private static final Logger logger = Logger.getLogger(IngestionController.class.getName());

    @Value("#{'${supported-cryptos}'.replaceAll(' ', '').split(',')}")
    private List<String> supportedCryptosList;
    private Set<String> supportedCryptosSet;

    private final CsvParsingService csvParsingService;
    private final CryptoPriceService cryptoPriceService;
    private final CryptoPriceMapper cryptoPriceMapper;

    public IngestionController(
            CryptoPriceMapper cryptoPriceMapper,
            CsvParsingService csvParsingService,
            CryptoPriceService cryptoPriceService) {
        this.cryptoPriceMapper = cryptoPriceMapper;
        this.csvParsingService = csvParsingService;
        this.cryptoPriceService = cryptoPriceService;
    }

    @PostConstruct
    public void init() {
        supportedCryptosSet = new HashSet<>(supportedCryptosList);
    }

    @Operation(
            summary = "Ingest a CSV file of crypto prices",
            description = "Uploads and processes a CSV file containing crypto prices. " +
                    "The file should contain entries for a single crypto within the same month.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV file ingested successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid CSV file format or unsupported crypto", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/ingest")
    public ResponseEntity<String> ingestCsv(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + ".csv";
        Path tempFile = Paths.get("/tmp", fileName);

        try {

            Files.copy(file.getInputStream(), tempFile);

            List<CryptoPriceDTO> cryptoPriceDTOs = csvParsingService.parseCsvFile(tempFile);

            List<CryptoPrice> validCryptoPrices = cryptoPriceDTOs.stream()
                    .peek(this::checkIfSupported)
                    .filter(this::isValidCryptoDto)
                    .map(cryptoPriceMapper::toEntity)
                    .toList();

            cryptoPriceService.saveCryptoPrices(validCryptoPrices);

            Files.delete(tempFile);

            return new ResponseEntity<>("CSV file ingested successfully", HttpStatus.OK);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private void checkIfSupported(CryptoPriceDTO entry) {
        if (!supportedCryptosSet.contains(entry.getSymbol().toUpperCase())) {
            throw new UnsupportedCryptoException("Crypto " + entry.getSymbol() + " is not supported");
        }
    }

    private boolean isValidCryptoDto(CryptoPriceDTO entry) {
        DataBinder binder = new DataBinder(entry);
        binder.validate();
        BindingResult result = binder.getBindingResult();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(
                    error -> logger.warning("Ignoring invalid entry: " + error.getDefaultMessage()));
            return false;
        }
        return true;
    }
}
