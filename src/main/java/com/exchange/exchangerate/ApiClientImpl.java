package com.exchange.exchangerate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.exchange.model.Currency;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ServerProperties;

import javax.inject.Inject;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.exchange.config.AppConfig.EXCHANGE_RATE_URL_PROPERTY;

@Slf4j
public class ApiClientImpl implements ApiClient {

    @Context
    private Configuration configuration;

    private final static String DEFAULT_BASE_URL = "https://open.er-api.com/v6/latest/";
    private HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Inject
    public ApiClientImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public ApiResponse getLatestExchangeRateForCurrency(Currency currency) {
        var request = buildRequest(currency);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), ApiResponse.class);
        } catch (Exception ex) {
            log.error("[ExchangeRate] Error occurred while getting exchange rate: {}", ex.getMessage(), ex);
            return null;
        }
    }

    void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private HttpRequest buildRequest(Currency currency) {
        return HttpRequest.newBuilder(URI.create(getServiceUrl() + currency.name())).GET().build();
    }

    private String getServiceUrl() {
        if (configuration == null || configuration.getProperties() == null) return DEFAULT_BASE_URL;
        return ServerProperties.getValue(configuration.getProperties(), EXCHANGE_RATE_URL_PROPERTY, DEFAULT_BASE_URL);
    }
}
