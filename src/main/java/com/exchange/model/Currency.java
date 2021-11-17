package com.exchange.model;

import java.util.Locale;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    ILS("ILS");

    private final String currencyCode;

    Currency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public static Currency from(String currencyString) throws InvalidCurrencyException {
        switch (currencyString.toLowerCase(Locale.ROOT)) {
            case "usd":
                return USD;
            case "eur":
                return EUR;
            case "gbp":
                return GBP;
            case "ils":
                return ILS;
            default:
                throw new InvalidCurrencyException("Currency code=" + currencyString + " is either invalid or not yet supported!");
        }
    }

    public String getCode() {
        return currencyCode;
    }
}
