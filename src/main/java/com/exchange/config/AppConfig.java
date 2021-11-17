package com.exchange.config;

import com.exchange.controllers.ExchangeController;
import com.exchange.exchangerate.ApiClient;
import com.exchange.exchangerate.ApiClientImpl;
import com.exchange.exchangerate.ApiService;
import com.exchange.exchangerate.ApiServiceImpl;
import com.exchange.services.ExchangeService;
import com.exchange.services.ExchangeServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class AppConfig extends ResourceConfig {

    public static final String EXCHANGE_RATE_URL_PROPERTY = "EXCHANGE_RATE_URL";

    public AppConfig() {
        register(ExchangeController.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ExchangeServiceImpl.class).to(ExchangeService.class);
                bind(ApiServiceImpl.class).to(ApiService.class);
                bind(ApiClientImpl.class).to(ApiClient.class);
            }
        });
        // to send validation messages to the server
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(EXCHANGE_RATE_URL_PROPERTY, "https://open.er-api.com/v6/latest/");
    }
}
