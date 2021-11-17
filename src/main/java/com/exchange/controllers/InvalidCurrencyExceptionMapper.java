package com.exchange.controllers;

import com.exchange.model.InvalidCurrencyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidCurrencyExceptionMapper implements ExceptionMapper<InvalidCurrencyException> {
    @Override
    public Response toResponse(InvalidCurrencyException e) {
        return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
    }
}
