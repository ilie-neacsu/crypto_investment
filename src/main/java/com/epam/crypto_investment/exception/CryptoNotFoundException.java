package com.epam.crypto_investment.exception;

public class CryptoNotFoundException extends RuntimeException {
    public CryptoNotFoundException(String message) {
        super(message);
    }
}
