package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoDto;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface CsvParsingService {
    List<CryptoDto> parseCsvFile(Path filePath) throws IOException;
}
