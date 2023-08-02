package com.example.bankingApp.account.service.exchange;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    private static final Map<String, BigDecimal> exchangeRates = new HashMap<>();

    static {
        // USD to other currencies
        exchangeRates.put("USD-TRY", new BigDecimal("26.95"));
        exchangeRates.put("USD-EUR", new BigDecimal("0.91"));
        exchangeRates.put("USD-GBP", new BigDecimal("0.78"));

        // EUR to other currencies
        exchangeRates.put("EUR-TRY", new BigDecimal("29.83"));
        exchangeRates.put("EUR-USD", new BigDecimal("1.10"));
        exchangeRates.put("EUR-GBP", new BigDecimal("0.86"));

        // GBP to other currencies
        exchangeRates.put("GBP-TRY", new BigDecimal("34.75"));
        exchangeRates.put("GBP-USD", new BigDecimal("1.29"));
        exchangeRates.put("GBP-EUR", new BigDecimal("1.17"));

        // TRY to other currencies
        exchangeRates.put("TRY-USD", new BigDecimal("0.037"));
        exchangeRates.put("TRY-EUR", new BigDecimal("0.034"));
        exchangeRates.put("TRY-GBP", new BigDecimal("0.029"));
    }

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String key = fromCurrency.toUpperCase() + "-" + toCurrency.toUpperCase();
        return exchangeRates.get(key);
    }

    @Override
    public BigDecimal convertAmount(String fromCurrency, String toCurrency, BigDecimal amount) {
        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        if (exchangeRate == null) {
            throw new IllegalArgumentException("Invalid currency pair: " + fromCurrency + " to " + toCurrency);
        }
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN);
    }
}
