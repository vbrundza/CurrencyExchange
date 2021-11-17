package com.exchange.services;

import com.exchange.model.Currency;
import com.exchange.model.ExchangeResponse;

import java.math.BigInteger;

public interface ExchangeService {
    ExchangeResponse exchangeCurrency(Currency baseCurrency, Currency quoteCurrency, BigInteger baseAmount);
}
