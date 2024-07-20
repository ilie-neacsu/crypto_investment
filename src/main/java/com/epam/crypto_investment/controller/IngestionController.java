package com.epam.crypto_investment.controller;

import com.epam.crypto_investment.dto.CryptoDto;
import com.epam.crypto_investment.service.CryptoMapper;
import com.epam.crypto_investment.service.CsvParsingService;
import com.epam.crypto_investment.service.ProcessingService;
import com.epam.crypto_investment.entity.Crypto;
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

    private static final Logger logger = Logger.getLogger(IngestionController.class.getName());

    private final CsvParsingService csvParsingService;
    private final ProcessingService processingService;
    private final CryptoMapper cryptoMapper;

    public IngestionController(
            CryptoMapper cryptoMapper,
            CsvParsingService csvParsingService,
            ProcessingService processingService) {
        this.cryptoMapper = cryptoMapper;
        this.csvParsingService = csvParsingService;
        this.processingService = processingService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestCsv(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + ".csv";
            Path tempFile = Paths.get("/tmp", fileName);

            Files.copy(file.getInputStream(), tempFile);

            List<CryptoDto> cryptoEntriesDto = csvParsingService.parseCsvFile(tempFile);

            List<Crypto> validCryptos = cryptoEntriesDto.stream()
                    .filter(this::isValidCryptoDto)
                    .map(cryptoMapper::toEntity)
                    .toList();

            processingService.processCryptos(validCryptos);

            Files.delete(tempFile);

            return new ResponseEntity<>("CSV file ingested successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to ingest CSV file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidCryptoDto(CryptoDto entry) {
        DataBinder binder = new DataBinder(entry);
        binder.validate();
        BindingResult result = binder.getBindingResult();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> logger.warning("Invalid entry: " + error.getDefaultMessage()));
            return false;
        }
        return true;
    }
}
