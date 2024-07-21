package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoPriceDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface CsvParsingService {
    List<CryptoPriceDTO> parseCsvFile(Path filePath) throws IOException;
}
