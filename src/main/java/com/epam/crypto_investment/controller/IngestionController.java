package com.epam.crypto_investment.controller;

import com.epam.crypto_investment.dto.CryptoPriceDTO;
import com.epam.crypto_investment.service.mapper.CryptoPriceMapper;
import com.epam.crypto_investment.service.CryptoPriceService;
import com.epam.crypto_investment.service.CsvParsingService;
import com.epam.crypto_investment.entity.CryptoPrice;
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
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class IngestionController {

    public static final int SLEEP_TIME = 300;

    private static final Logger logger = Logger.getLogger(IngestionController.class.getName());

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

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestCsv(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + ".csv";
            Path tempFile = Paths.get("/tmp", fileName);

            Files.copy(file.getInputStream(), tempFile);

            List<CryptoPriceDTO> cryptoPriceDTOs = csvParsingService.parseCsvFile(tempFile);

            List<CryptoPrice> validCryptoPrices = cryptoPriceDTOs.stream()
                    .filter(this::isValidCryptoDto)
                    .map(cryptoPriceMapper::toEntity)
                    .toList();

            cryptoPriceService.saveCryptoPrices(validCryptoPrices);

            Files.delete(tempFile);

            return new ResponseEntity<>("CSV file ingested successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to ingest CSV file", HttpStatus.INTERNAL_SERVER_ERROR);
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
