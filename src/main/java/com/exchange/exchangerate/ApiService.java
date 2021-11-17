package com.exchange.exchangerate;

import com.exchange.model.Currency;
import com.exchange.model.CurrencyExchangeRate;

public interface ApiService {
    CurrencyExchangeRate getExchangeRateForCurrency(Currency currency);
}
