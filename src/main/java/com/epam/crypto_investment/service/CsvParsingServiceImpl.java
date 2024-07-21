package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoPriceDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParsingServiceImpl implements CsvParsingService {

    @Override
    public List<CryptoPriceDTO> parseCsvFile(Path filePath) throws IOException {
        List<CryptoPriceDTO> cryptoEntries = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath.toFile())) {
            CSVFormat format = CSVFormat.Builder.create()
                    .setHeader("timestamp", "symbol", "price")
                    .setSkipHeaderRecord(true)
                    .build();

            CSVParser parser = new CSVParser(reader, format);
            for (CSVRecord record : parser) {
                long timestamp = Long.parseLong(record.get("timestamp"));
                String symbol = record.get("symbol");
                double price = Double.parseDouble(record.get("price"));
                cryptoEntries.add(new CryptoPriceDTO(timestamp, symbol, price));
            }
        }
        return cryptoEntries;
    }
}
