package com.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class ExchangeResponse {
    private Double exchangeRate;
    private BigInteger quoteAmount;
}
