package com.example.bankingApp.account.service.exchange;

import java.math.BigDecimal;

public interface ExchangeService {
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);
    BigDecimal convertAmount(String fromCurrency, String toCurrency, BigDecimal amount);
}
