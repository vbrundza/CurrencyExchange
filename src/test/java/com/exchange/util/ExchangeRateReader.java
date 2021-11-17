package com.exchange.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.exchange.exchangerate.ApiResponse;
import com.exchange.model.Currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExchangeRateReader {

    public static ApiResponse getApiResponseForCurrency(Currency currency) throws IOException {
        var mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        var exchangeResponse = readDataFromFile(currency);
        return mapper.readValue(exchangeResponse, ApiResponse.class);
    }

    public static String readDataFromFile(Currency currencyToRead) throws IOException {
        var resourceName = "ExchangeRate/" + currencyToRead.getCode() + ".json";
        ClassLoader classLoader = ExchangeRateReader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        return readFromInputStream(inputStream);
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
