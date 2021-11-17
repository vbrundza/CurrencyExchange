package com.exchange.exchangerate;

import com.exchange.model.Currency;
import com.exchange.util.ExchangeRateReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ApiClientTest {

    private static ApiClientImpl exchangeApiClient;
    private static HttpClient mockedHttpClient;

    @BeforeAll
    static void setup() {
        mockedHttpClient = Mockito.mock(HttpClient.class);
        exchangeApiClient = new ApiClientImpl();
        exchangeApiClient.setHttpClient(mockedHttpClient);
    }

    @Test
    void getLatestExchangeRateForCurrency_shouldReturnWellFormattedExchange_whenValidRequest() throws Exception {
        //given
        initMocks();

        //when
        var response = exchangeApiClient.getLatestExchangeRateForCurrency(Currency.EUR);
        //then
        assertEquals(Currency.EUR, response.getBase());
        assertEquals(1637020952L, response.getLastUpdateTimeUnix());
        assertEquals(LocalDateTime.of(2021, 11, 16, 0, 2, 32), response.getLastUpdateDateTime());
        assertEquals(1637109182L, response.getNextUpdateTimeUnix());
        assertEquals(LocalDateTime.of(2021, 11, 17, 0, 33, 2), response.getNextUpdateDateTime());
        assertFalse(response.getRates().isEmpty());
        var usdRate = response.getRates().get(Currency.USD.getCode());
        var gbpRate = response.getRates().get(Currency.GBP.getCode());
        var ilsRate = response.getRates().get(Currency.ILS.getCode());
        assertEquals(1.15, usdRate);
        assertEquals(0.857, gbpRate);
        assertEquals(3.58, ilsRate);
    }

    @Test
    void getLatestExchangeRateForCurrency_shouldReturnNull_whenSomeErrorHappens() {
        //given call with no mocks
        //when
        var response = exchangeApiClient.getLatestExchangeRateForCurrency(Currency.EUR);
        //then
        assertNull(response);
    }

    private void initMocks() throws Exception {
        var response = Mockito.mock(HttpResponse.class);
        var responseBody = ExchangeRateReader.readDataFromFile(Currency.EUR);
        when(response.body()).thenReturn(responseBody);
        when(mockedHttpClient.send(any(), any())).thenReturn(response);
    }
}
