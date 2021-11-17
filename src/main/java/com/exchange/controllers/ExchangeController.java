package com.exchange.controllers;

import com.exchange.model.Currency;
import com.exchange.model.ExchangeResponse;
import com.exchange.model.InvalidCurrencyException;
import com.exchange.services.ExchangeService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;

@Path("exchange")
@Service
public class ExchangeController {
    private final ExchangeService exchangeService;

    @Inject
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public ExchangeResponse exchangeCurrency(@NotBlank(message = "Base currency code (baseCurrency) is required")
                                             @QueryParam(value = "baseCurrency") String baseCurrency,

                                             @NotBlank(message = "Quote currency code (quoteCurrency) is required")
                                             @QueryParam(value = "quoteCurrency") String quoteCurrency,

                                             @NotNull(message = "Amount (baseAmount) must be provided")
                                             @Positive(message = "Amount cannot be 0 or less")
                                             @QueryParam(value = "baseAmount") BigInteger baseAmount) throws InvalidCurrencyException {
        var baseCurrencyVal = Currency.from(baseCurrency);
        var quoteCurrencyVal = Currency.from(quoteCurrency);
        return exchangeService.exchangeCurrency(baseCurrencyVal, quoteCurrencyVal, baseAmount);
    }

}
