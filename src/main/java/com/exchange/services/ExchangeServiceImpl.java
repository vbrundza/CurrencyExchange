package com.exchange.services;

import com.exchange.exchangerate.ApiService;
import com.exchange.model.Currency;
import com.exchange.model.ExchangeResponse;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Slf4j
public class ExchangeServiceImpl implements ExchangeService {
    private final ApiService apiService;

    @Inject
    public ExchangeServiceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public ExchangeResponse exchangeCurrency(Currency baseCurrency, Currency quoteCurrency, BigInteger baseAmount) {
        if (BigInteger.ZERO.compareTo(baseAmount) > 0) {
            log.error("[Amount error] Invalid amount received: " + baseAmount);
            throw new IllegalArgumentException("Base amount cannot be less than 0");
        }
        var quoteCurrencyRate = getCurrencyRate(baseCurrency, quoteCurrency);
        var amountToReturn = calculateAmount(quoteCurrencyRate, baseAmount);

        return new ExchangeResponse(quoteCurrencyRate.doubleValue(), amountToReturn);
    }

    private BigDecimal getCurrencyRate(Currency baseCurrency, Currency quoteCurrency) {
        var exchangeRates = apiService.getExchangeRateForCurrency(baseCurrency);
        var currencyRate = exchangeRates.getRateForCurrency(quoteCurrency);
        return BigDecimal.valueOf(currencyRate).setScale(3, RoundingMode.HALF_UP);
    }

    private BigInteger calculateAmount(BigDecimal quoteCurrencyRate, BigInteger baseAmount) {
        var decimalAmount = new BigDecimal(baseAmount);
        return quoteCurrencyRate.multiply(decimalAmount).setScale(0, RoundingMode.UP).toBigInteger();
    }

}
