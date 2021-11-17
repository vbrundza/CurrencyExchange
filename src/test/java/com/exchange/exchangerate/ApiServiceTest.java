package com.exchange.exchangerate;

import com.exchange.model.Currency;
import com.exchange.util.ExchangeRateReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ApiServiceTest {

    private static ApiClient apiClient;
    private static ApiService apiService;

    @BeforeAll
    static void setup() {
        apiClient = Mockito.mock(ApiClient.class);
        apiService = new ApiServiceImpl(apiClient);
    }

    @BeforeEach
    void cleanup() {
        Mockito.reset(apiClient);
    }

    @Test
    void getExchangeRateForCurrency_shouldReturnEurExchangeRate_whenApiClientReturnsData() throws Exception {
        //given
        var response = ExchangeRateReader.getApiResponseForCurrency(Currency.EUR);
        when(apiClient.getLatestExchangeRateForCurrency(any(Currency.class))).thenReturn(response);
        //when
        var exchangeRate = apiService.getExchangeRateForCurrency(Currency.EUR);
        //then
        assertNotNull(exchangeRate);
        assertEquals(Currency.EUR, exchangeRate.getBaseCurrency());
        var usdRate = exchangeRate.getRateForCurrency(Currency.USD);
        assertEquals(1.15, usdRate);
        var gbpRate = exchangeRate.getRateForCurrency(Currency.GBP);
        assertEquals(0.857, gbpRate);
        var ilsRate = exchangeRate.getRateForCurrency(Currency.ILS);
        assertEquals(3.58, ilsRate);
    }

    @Test
    void getExchangeRateForCurrency_shouldThrowRuntimeException_whenApiClientReturnsNoData() {
        //given null response from client
        //when
        //then
        assertThrows(RuntimeException.class, () -> apiService.getExchangeRateForCurrency(Currency.EUR));
    }
}
