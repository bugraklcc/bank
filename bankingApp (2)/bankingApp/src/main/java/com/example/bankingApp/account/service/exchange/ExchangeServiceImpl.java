package com.example.bankingApp.account.service.exchange;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {

        if (fromCurrency.equalsIgnoreCase("USD") && toCurrency.equalsIgnoreCase("TRY")) {
            return new BigDecimal("26.95");
        } else if (fromCurrency.equalsIgnoreCase("EUR") && toCurrency.equalsIgnoreCase("TRY")) {
            return new BigDecimal("29.83");
        } else if (fromCurrency.equalsIgnoreCase("GBP") && toCurrency.equalsIgnoreCase("TRY")) {
            return new BigDecimal("34.75");
        } else {

            return null;
        }
    }
}