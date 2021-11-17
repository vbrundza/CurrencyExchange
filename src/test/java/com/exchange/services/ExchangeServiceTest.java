package com.exchange.services;

import com.exchange.exchangerate.ApiService;
import com.exchange.exchangerate.ApiServiceImpl;
import com.exchange.model.Currency;
import com.exchange.model.CurrencyExchangeRate;
import com.exchange.util.ExchangeRateReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ExchangeServiceTest {

    private static ApiService apiService;
    private static ExchangeService exchangeService;

    @BeforeAll
    static void setup() {
        apiService = Mockito.mock(ApiServiceImpl.class);
        exchangeService = new ExchangeServiceImpl(apiService);
    }

    @BeforeEach
    void cleanup() {
        Mockito.reset(apiService);
    }

    @Test
    void exchangeCurrency_shouldReturnCorrectCalculatedExchangeRateAndAmount_whenRequestedEurToUsd() throws IOException {
        //given
        when(apiService.getExchangeRateForCurrency(Currency.EUR)).thenReturn(getExchangeRate());
        //when
        var exchangeResponse = exchangeService.exchangeCurrency(Currency.EUR, Currency.USD, BigInteger.valueOf(100));
        //then
        assertEquals(1.15, exchangeResponse.getExchangeRate());
        assertEquals(BigInteger.valueOf(115), exchangeResponse.getQuoteAmount());
    }

    @Test
    void exchangeCurrency_shouldReturnCorrectlyRoundedUpAmountAndExchangeRate_whenRequestedEurToGbp() throws IOException {
        //given
        when(apiService.getExchangeRateForCurrency(Currency.EUR)).thenReturn(getExchangeRate());
        //when
        var exchangeResponse = exchangeService.exchangeCurrency(Currency.EUR, Currency.GBP, BigInteger.valueOf(100));
        //then
        assertEquals(0.857, exchangeResponse.getExchangeRate());
        assertEquals(BigInteger.valueOf(86), exchangeResponse.getQuoteAmount());
    }

    @Test
    void exchangeCurrency_shouldReturnCorrectAmounts_whenRequestedToExchange() throws IOException {
        //given
        when(apiService.getExchangeRateForCurrency(Currency.EUR)).thenReturn(getExchangeRate());
        //when
        var responseFor1000 = exchangeService.exchangeCurrency(Currency.EUR, Currency.GBP, BigInteger.valueOf(1000));
        var responseFor1 = exchangeService.exchangeCurrency(Currency.EUR, Currency.GBP, BigInteger.ONE);
        var responseFor0 = exchangeService.exchangeCurrency(Currency.EUR, Currency.GBP, BigInteger.ZERO);
        var responseFor40 = exchangeService.exchangeCurrency(Currency.EUR, Currency.GBP, BigInteger.valueOf(40)); //34.28
        var responseForMaxIntToUsd = exchangeService.exchangeCurrency(Currency.EUR, Currency.USD, BigInteger.valueOf(Integer.MAX_VALUE)); // to cover maximum int values

        //then
        assertEquals(BigInteger.valueOf(857), responseFor1000.getQuoteAmount());
        assertEquals(BigInteger.valueOf(1), responseFor1.getQuoteAmount());
        assertEquals(BigInteger.valueOf(0), responseFor0.getQuoteAmount());
        assertEquals(BigInteger.valueOf(35), responseFor40.getQuoteAmount());
        assertEquals(BigInteger.valueOf(2469606195L), responseForMaxIntToUsd.getQuoteAmount());
    }

    @Test
    void exchangeCurrency_shouldThrowIllegalArgumentException_whenRequestedAmountIsLessThan0() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> exchangeService.exchangeCurrency(Currency.EUR, Currency.USD, BigInteger.valueOf(-1)));
    }

    private CurrencyExchangeRate getExchangeRate() throws IOException {
        return CurrencyExchangeRate.from(ExchangeRateReader.getApiResponseForCurrency(Currency.EUR));
    }
}
