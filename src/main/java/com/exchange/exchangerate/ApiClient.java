package com.exchange.exchangerate;

import com.exchange.model.Currency;

public interface ApiClient {
    ApiResponse getLatestExchangeRateForCurrency(Currency currency);
}
