// com.example.bankingApp.account.service.exchange.ExchangeServiceImpl

package com.example.bankingApp.account.service.exchange;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {

        if (fromCurrency.equalsIgnoreCase("USD") && toCurrency.equalsIgnoreCase("TRY")) {
            return new BigDecimal("8.50"); // 1 USD = 8.50 TRY
        } else if (fromCurrency.equalsIgnoreCase("EUR") && toCurrency.equalsIgnoreCase("TRY")) {
            return new BigDecimal("10.00"); // 1 EUR = 10.00 TRY
        } else if (fromCurrency.equalsIgnoreCase("GBP") && toCurrency.equalsIgnoreCase("TRY")) {
            return new BigDecimal("12.00"); // 1 GBP = 12.00 TRY
        } else {

            return null;
        }
    }
}
