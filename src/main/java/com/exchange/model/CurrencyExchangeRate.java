package com.exchange.model;

import com.exchange.exchangerate.ApiResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
public class CurrencyExchangeRate {
    @Getter
    @Setter
    private Currency baseCurrency;
    @Setter
    private Map<String, Double> exchangeRates;
    @Getter
    @Setter
    private LocalDateTime nextUpdateDateTime;

    public static CurrencyExchangeRate from(ApiResponse apiResponse) {
        var exchangeRate = new CurrencyExchangeRate();
        exchangeRate.setExchangeRates(apiResponse.getRates());
        exchangeRate.setBaseCurrency(apiResponse.getBase());
        exchangeRate.setNextUpdateDateTime(apiResponse.getNextUpdateDateTime());
        return exchangeRate;
    }

    public Double getRateForCurrency(Currency currency) {
        var code = currency.getCode();
        return exchangeRates.get(code);
    }
}
