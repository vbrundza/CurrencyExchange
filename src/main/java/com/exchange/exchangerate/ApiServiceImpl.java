package com.exchange.exchangerate;

import com.exchange.cache.Cache;
import com.exchange.cache.CacheImpl;
import com.exchange.model.Currency;
import com.exchange.model.CurrencyExchangeRate;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ApiServiceImpl implements ApiService {
    private final ApiClient exchangeApiClient;
    private final Cache<Currency, CurrencyExchangeRate> currencyCache;

    @Inject
    public ApiServiceImpl(ApiClient apiClient) {
        this.exchangeApiClient = apiClient;
        this.currencyCache = new CacheImpl<>(Currency.values().length);
    }

    @Override
    public CurrencyExchangeRate getExchangeRateForCurrency(Currency currency) {
        var exchangeRateOpt = currencyCache.get(currency);
        if (exchangeRateOpt.isEmpty() || isExpiredRate(exchangeRateOpt.get())) {
            var newExchangeRate = CurrencyExchangeRate.from(getRatesFromExchange(currency));
            currencyCache.put(currency, newExchangeRate);
            return newExchangeRate;
        }
        return exchangeRateOpt.get();
    }

    private boolean isExpiredRate(CurrencyExchangeRate exchangeRate) {
        return getCurrentTime().isAfter(exchangeRate.getNextUpdateDateTime());
    }

    private ApiResponse getRatesFromExchange(Currency currency) {
        var apiResponse = exchangeApiClient.getLatestExchangeRateForCurrency(currency);
        if (apiResponse == null) {
            throw new RuntimeException("Cannot proceed with exchange due to no currency rates available");
        }
        return apiResponse;
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
